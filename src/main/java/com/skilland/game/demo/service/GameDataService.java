package com.skilland.game.demo.service;


import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.gameroom.GameEntity;
import com.skilland.game.demo.model.gameroom.GameJsonEntity;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.BriefCourseResponse;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.KnownAuthority;
import com.skilland.game.demo.model.user.StudentEntity;
import com.skilland.game.demo.model.user.TeacherEntity;
import com.skilland.game.demo.model.user.resp.BriefUserResponse;
import com.skilland.game.demo.repository.*;
import com.skilland.game.demo.repository.game.GameFileStorage;
import com.skilland.game.demo.repository.game.GameRepository;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.util.GrigorianCalendarDateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameDataService {

    private final UserService userService;

    private final GameRepository gameRepository;

    private final CourseRepository courseRepository;

    private final SubjectTopicRepository subjectTopicRepository;

    private final Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;

    private final GameFileStorage gameFileStorage;


    @Autowired
    public GameDataService(UserService userService, GameRepository gameRepository, CourseRepository courseRepository,
                           SubjectTopicRepository subjectTopicRepository, Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap, GameFileStorage gameFileStorage) {
        this.userService = userService;
        this.gameRepository = gameRepository;
        this.courseRepository = courseRepository;
        this.subjectTopicRepository = subjectTopicRepository;
        this.dataReceiverByUserAuthorityMap = dataReceiverByUserAuthorityMap;
        this.gameFileStorage = gameFileStorage;
    }

    private DataReceiverByUserAuthority getDataReceiver(String authority){
        DataReceiverByUserAuthority dataReceiver = this.dataReceiverByUserAuthorityMap.get(authority);
        if(dataReceiver == null){
            throw UserExceptions.authorityNotFound(authority);
        }
        return dataReceiver;
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
        userService.save(teacher);

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
        this.getDataReceiver(KnownAuthority.ROLE_STUDENT.getAuthority()).save(studentEntity);
        courseEntity.getStudents().add(studentEntity);
        this.courseRepository.save(courseEntity);
    }

    public List<BriefCourseResponse> getAllAvailableCourses(String email, String authority){
        DataReceiverByUserAuthority dataReceiver = this.getDataReceiver(authority);
        Set<CourseEntity> courseEntities = dataReceiver.getAllCoursesOfUser(email);
        return courseEntities.stream().map((item)->new BriefCourseResponse(item.getId(), item.getTitle())).collect(Collectors.toList());
    }


    @Transactional
    public CourseResponse deleteStudentFromCourse(String teacherEmail, String studentEmail, Long courseId){
        DataReceiverByUserAuthority studentDataReceiver = getDataReceiver(KnownAuthority.ROLE_STUDENT.getAuthority());
        DataReceiverByUserAuthority teacherDataReceiver = getDataReceiver(KnownAuthority.ROLE_TEACHER.getAuthority());
        StudentEntity studentEntity = (StudentEntity) studentDataReceiver.getUserByEmail(studentEmail);
        TeacherEntity teacherEntity = (TeacherEntity) teacherDataReceiver.getUserByEmail(teacherEmail);
        CourseResponse courseResponse = teacherEntity.getCoursesTeachers().stream()
                .filter((item) -> item.getId() == courseId)
                .peek((item) -> item.getStudents().remove(studentEntity))
                .map((item) -> {
                    List<BriefUserResponse> students = item.getStudents().stream().map(BriefUserResponse::fromUserEntity).collect(Collectors.toList());
                    return new CourseResponse(item.getTitle(), students);
                })
                .findFirst()
                .orElseThrow(()->GameDataException.courseNotFound(courseId.toString()));
        this.userService.save(teacherEntity);
        return courseResponse;
    }

    @Transactional
    public void deleteCourseByTeacherAndCourseId(String teacherEmail, Long courseId){
        CourseEntity courseEntity = this.getCourseById(courseId);
        if (courseEntity.getTeacher().getEmail().equals(teacherEmail)){
            this.courseRepository.deleteCourseEntityById(courseId);
        }else throw GameDataException.courseNotFound(courseId.toString());
    }

    public List<BriefUserResponse> getCourseParticipantsOfTeacher(String teacherEmail, Long courseId){
        CourseEntity courseEntity = this.getCourseById(courseId);
        if(!courseEntity.getTeacher().getEmail().equals(teacherEmail)){
            throw GameDataException.courseNotFound(courseId.toString());
        }else
            return courseEntity.getStudents().stream()
                .map((item) -> new BriefUserResponse(item.getEmail(), item.getFirstName()))
                .collect(Collectors.toList());

    }




}
