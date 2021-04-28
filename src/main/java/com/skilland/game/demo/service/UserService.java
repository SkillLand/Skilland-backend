package com.skilland.game.demo.service;


import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.gameroom.GameEntity;
import com.skilland.game.demo.model.gameroom.GameJsonEntity;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.*;
import com.skilland.game.demo.model.user.request.SaveUserRequest;
import com.skilland.game.demo.model.user.resp.UserResponse;
import com.skilland.game.demo.repository.*;
//import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.util.GrigorianCalendarDateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserAuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final GameRepository gameRepository;

    private final CourseRepository courseRepository;

    private final GameFileStorage gameFileStorage;

    private final SubjectTopicRepository subjectTopicRepository;

    private final StudentRepository studentRepository;

    private final TeacherRepository teacherRepository;

    private Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;


    @Autowired
    public UserService(UserRepository userRepository, UserAuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder, GameRepository gameRepository,
                       CourseRepository courseRepository, GameFileStorage gameFileStorage, SubjectTopicRepository subjectTopicRepository,
                       StudentRepository studentRepository, TeacherRepository teacherRepository, Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.gameRepository = gameRepository;
        this.courseRepository = courseRepository;
        this.gameFileStorage = gameFileStorage;
        this.subjectTopicRepository = subjectTopicRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.dataReceiverByUserAuthorityMap = dataReceiverByUserAuthorityMap;
    }

    @Transactional
    public UserResponse create(SaveUserRequest request) {
        validateUniqueFields(request);
        Map<KnownAuthority, UserAuthority> authorities = null;
        String authority = request.getAuthority();
        if(authority.equals("teacher")){
            authorities = this.getTeacherAuthorities();
        }else if (authority.equals("student")){
            authorities = this.getStudentAuthorities();
        }else {
            throw UserExceptions.authorityNotFound(request.getAuthority());
        }

        return UserResponse.fromUser(save(request, authorities));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        GameUserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User with mail " + email + " not found"));
        Set<KnownAuthority> grantedAuthorities = EnumSet.copyOf(user.getAuthorities().keySet());
        return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserResponse::fromUser);
    }


    private void validateUniqueFields(SaveUserRequest request) {
        String email = request.getEmail();
        System.out.println(email);
        if (userRepository.existsByEmail(email)) {
            throw UserExceptions.duplicateEmail(email);
        }
    }

    private DataReceiverByUserAuthority getDataReceiver(String authority){
        DataReceiverByUserAuthority dataReceiver = this.dataReceiverByUserAuthorityMap.get(authority);
        if(dataReceiver == null){
            throw UserExceptions.authorityNotFound(authority);
        }
        return dataReceiver;
    }


    private GameUserEntity save(SaveUserRequest request, Map<KnownAuthority, UserAuthority> authorities) {
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(authorities.keySet().stream().findFirst().get().getAuthority());
        GameUserEntity user = dataReceiver.createEmptyUser();
        user.getAuthorities().putAll(authorities);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        return user;
    }

    private Map<KnownAuthority, UserAuthority> getStudentAuthorities() {
        UserAuthority authority = authorityRepository
                .findByValue(KnownAuthority.ROLE_STUDENT)
                .orElseThrow(() -> UserExceptions.authorityNotFound(KnownAuthority.ROLE_STUDENT.name()));

        Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_STUDENT, authority);
        return authorities;
    }

    private Map<KnownAuthority, UserAuthority> getTeacherAuthorities() {
        UserAuthority authority = authorityRepository
                .findByValue(KnownAuthority.ROLE_TEACHER)
                .orElseThrow(() -> UserExceptions.authorityNotFound(KnownAuthority.ROLE_TEACHER.name()));

        Map<KnownAuthority, UserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_TEACHER, authority);
        return authorities;
    }

    public SubjectEntity getSubjectByName(String title){
        return this.subjectTopicRepository.findSubjectByName(title).orElseThrow(() -> GameDataException.subjectNotFound(title));
    }

    @Transactional
    public NewCourseResponse createNewCourse(String teacherEmail, String courseName){
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(KnownAuthority.ROLE_TEACHER.getAuthority());
        TeacherEntity teacher = (TeacherEntity) dataReceiver.getUserByEmail(teacherEmail);

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setTitle(courseName);
        boolean existsCourse = teacher.getCoursesTeachers().contains(courseEntity);
        if(existsCourse){
            throw GameDataException.courseAlreadyExists(courseName);
        }

        courseEntity.setTeacher(teacher);
        courseRepository.save(courseEntity);
        teacher.getCoursesTeachers().add(courseEntity);
        userRepository.save(teacher);

        return NewCourseResponse.fromCourse(courseEntity);
    }

    private TopicEntity findTopicByName(String subjectName, String topicName) throws IOException {
        return this.subjectTopicRepository.findTopicByName(subjectName, topicName).orElseThrow(
                () -> GameDataException.topicNotFound(topicName));
    }

    @Transactional(propagation = Propagation.NEVER)
    public GameEntity saveGame(GameEntity gameEntity){
        return gameRepository.save(gameEntity);
    }


    public GameResponse createNewGame(String teacherEmail, CreateGameRequest request) throws IOException {

        String courseName = request.getCourseName();
        String [] topicsNames = request.getTopicNames();
        String gameName = request.getGameName();
        String subjectName = request.getSubjectName();
        int durabilityMinutes = request.getGameDurabilityMinutes();
        int maxTaskLevel = request.getMaxTaskLevel();
        int minTaskLevel = request.getMinTaskLevel();
        String startDate = request.getStartDate();
        if(!subjectTopicRepository.existsSubjectByName(subjectName)){
        throw GameDataException.subjectNotFound(subjectName);
        }

        Timestamp gameDate = new Timestamp(GrigorianCalendarDateParser.parseDateFromString(startDate).getTime());
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(KnownAuthority.ROLE_TEACHER.getAuthority());
        TeacherEntity teacher = (TeacherEntity) dataReceiver.getUserByEmail(teacherEmail);
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setTitle(courseName);
        Set<CourseEntity> course = teacher.getCoursesTeachers();

        boolean existsCourse = course.contains(courseEntity);
        if(!existsCourse){
            throw GameDataException.courseNotFound(courseName);
        }
        for(CourseEntity courseEntityItem : course){
            if (courseEntity.equals(courseEntityItem)){
                System.out.println(courseEntity.getTitle());
                courseEntity = courseEntityItem;
                break;
            }
        }
        gameName = generateGameName(gameName, courseEntity.getGames());
        GameEntity gameEntity = new GameEntity();
        gameEntity.setTitle(gameName);
        gameEntity.setDateTime(gameDate);
        gameEntity.setSubjectName(subjectName);
        gameEntity.setCourse(courseEntity);
        Long savedGameId = this.saveGame(gameEntity).getId();
        this.createAndSaveGameJsonEntity(Arrays.asList(topicsNames), minTaskLevel, maxTaskLevel, durabilityMinutes, subjectName, savedGameId.toString());
        return new GameResponse(savedGameId, gameName, subjectName, Arrays.asList(topicsNames), minTaskLevel, maxTaskLevel, durabilityMinutes, startDate);
    }

    private void createAndSaveGameJsonEntity(List<String> topicNames, int minTaskLevel,
                                             int maxTaskLevel, int durabilityMinutes,
                                             String subjectName, String gameId) throws IOException {
        GameJsonEntity gameJsonEntity = new GameJsonEntity();
        gameJsonEntity.setDurabilityMinutes(durabilityMinutes);
        gameJsonEntity.setMaxTaskLevel(maxTaskLevel);
        gameJsonEntity.setMinTaskLevel(minTaskLevel);
        gameJsonEntity.setTopicNames(topicNames);
        this.gameFileStorage.save(gameJsonEntity, subjectName, gameId);

    }

    private String generateGameName(String gameName, Set<GameEntity> gameEntities){
        if(gameEntities.isEmpty()) return gameName;
        Set<String> gameNames = gameEntities.stream()
                .map(GameEntity::getTitle)
                .collect(Collectors.toSet());
        int firstIndex = 1;
        while (gameNames.contains(gameName)){
            gameName += firstIndex++;
        }
        return gameName;
    }

    public synchronized List<GameResponse> getAllGamesOfUserInCourse(String email, String courseName, String authority){

        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(authority);
        Optional<CourseEntity> optionalCourseEntity = dataReceiver.getCourseByEmailAndCourseName(email, courseName);
        return optionalCourseEntity.get().getGames().stream().map((item) -> {
            GameResponse gameResponse = new GameResponse();
            Long gameId = item.getId();
            gameResponse.setGameId(gameId);
            gameResponse.setSubjectName(item.getSubjectName());
            gameResponse.setGameName(item.getTitle());
            gameResponse.setStartDate(item.getDateTime().toString());
            GameJsonEntity gameJsonEntity = this.gameFileStorage.findGameBySubjectAndId(item.getSubjectName(), gameId.toString())
                    .orElseThrow(() -> GameDataException.gameNotFound(gameId.toString()));
            gameResponse.setDurabilityMinutes(gameJsonEntity.getDurabilityMinutes());
            gameResponse.setMaxTaskLevel(gameJsonEntity.getMaxTaskLevel());
            gameResponse.setMinTaskLevel(gameJsonEntity.getMinTaskLevel());
            gameResponse.setTopicNames(gameJsonEntity.getTopicNames());
            return gameResponse;
        }).collect(Collectors.toList());
    }



    private CourseEntity getCourseById(Long courseId){
        return this.courseRepository.findById(courseId).orElseThrow(() -> GameDataException.courseNotFound(courseId.toString()));
    }

    @Transactional
    public void addStudentToCourse(String email, long courseId){
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(KnownAuthority.ROLE_STUDENT.getAuthority());
        StudentEntity studentEntity = (StudentEntity) dataReceiver.getUserByEmail(email);
        CourseEntity courseEntity = this.getCourseById(courseId);
        studentEntity.getCoursesStudents().add(courseEntity);
        this.studentRepository.save(studentEntity);
        courseEntity.getStudents().add(studentEntity);
        this.courseRepository.save(courseEntity);
    }

    public List<CourseResponse> getAllAvailableCourses(String email, String authority){
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(authority);
        Set<CourseEntity> courseEntities = dataReceiver.getAllCoursesOfUser(email);
        return courseEntities.stream().map((item)->new CourseResponse(item.getTitle())).collect(Collectors.toList());
    }



}
