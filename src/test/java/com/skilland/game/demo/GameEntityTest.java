package com.skilland.game.demo;

import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.TaskEntity;
import com.skilland.game.demo.repository.*;
import com.skilland.game.demo.service.UserService;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class GameEntityTest {


    UserRepository userRepository;

    UserAuthorityRepository authorityRepository;

    PasswordEncoder passwordEncoder;

    GameRepository gameRepository;

    CourseRepository courseRepository;

    SubjectTopicRepository subjectTopicRepository;

    UserService userService;

    StudentRepository studentRepository;

    TeacherRepository teacherRepository;

    GameFileStorage gameFileStorage;

    @Autowired
    Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;


    @Before
    public void setUp(){
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        authorityRepository = mock(UserAuthorityRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        gameRepository = mock(GameRepository.class);
        subjectTopicRepository = new SubjectTopicRepository();
        subjectTopicRepository.setPathStart("src/test/recourses/Предметы");
        gameFileStorage = new GameFileStorage();
        gameFileStorage.setPathStart("src/test/resources/Игры");
        studentRepository = mock(StudentRepository.class);
        teacherRepository = mock(TeacherRepository.class);
        userService = new UserService(userRepository, authorityRepository, passwordEncoder,
                gameRepository, courseRepository, gameFileStorage, subjectTopicRepository, studentRepository, teacherRepository, dataReceiverByUserAuthorityMap);
    }

    @Test
    public void addNewCourseTest() {
       /* String email = "teacher1@gmail.com";
        long id = 123;
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(id);
        teacher.setEmail(email);

        String subjectName = "math";
        Subject subject = new Subject();
        subject.setTitle(subjectName);
        subject.setId(1);

        String absentCourseName = "math 6 grade";
        CourseEntity absentCourseEntity = new CourseEntity();
        absentCourseEntity.setTitle(absentCourseName);
        absentCourseEntity.setId(1);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(teacher));
        when(subjectTopicRepository.findSubjectByTitle(subjectName)).thenReturn(Optional.of(subject));
        when(courseRepository.save(same(absentCourseEntity))).thenReturn(absentCourseEntity);
        when(userRepository.save(same(teacher))).thenReturn(teacher);



        NewCourseResponse resultCourse = userService.createNewCourse(email, absentCourseName, subjectName);

        assertThat(resultCourse.getCourseName()).isEqualTo(absentCourseName);
        assertThat(teacher.getCoursesTeachers()).contains(absentCourseEntity);
        assertThat(teacher.getCoursesTeachers().size()).isEqualTo(1);

        try {
            userService.createNewCourse(email, absentCourseName, subjectName);
            Assert.fail("Expected response status exception");
        }catch (ResponseStatusException e){
            Assert.assertNotEquals("", e.getMessage());
        }*/


    }

    @Test
    public void getSubjectAndTopicTests() throws IOException {
        String presentSubjectName = "Алгебра";
        String absentSubjectName = "123Алгебра";
        SubjectEntity subjectEntity = this.subjectTopicRepository.findSubjectByName(presentSubjectName).get();
        String presentTopicName = "Многочлены";
        String absentTopicName = "123Многочлены";
        TopicEntity topicEntity = this.subjectTopicRepository.findTopicByName(presentSubjectName, presentTopicName).get();
        this.userService.getSubjectByName(presentSubjectName);
        assertThat(this.subjectTopicRepository.existsSubjectByName(presentSubjectName)).isEqualTo(true);
        assertThat(this.subjectTopicRepository.existsSubjectByName(absentSubjectName)).isEqualTo(false);
        assertThat(this.subjectTopicRepository.existsTopicByName(presentSubjectName, presentTopicName)).isEqualTo(true);
        assertThat(this.subjectTopicRepository.existsTopicByName(presentSubjectName, absentTopicName)).isEqualTo(false);
        assertThat(this.subjectTopicRepository.existsTopicByName(absentSubjectName, absentTopicName)).isEqualTo(false);

    }



    @Test
    public void getRandomTaskOfComplexityTest() throws IOException {
        String subjectName = "Алгебра";
        List<String> topicNames = new ArrayList<>();
        topicNames.add("Многочлены");
        topicNames.add("Уравнения");
        String level = "1";
        Map<String, List<String>> topicTask = new HashMap<>();
        topicTask.put(topicNames.get(0), new ArrayList<>());
        topicTask.put(topicNames.get(1), new ArrayList<>());
        while(true){
            Optional<TaskEntity>taskEntity = this.subjectTopicRepository.getRandomTaskOfComplexity(subjectName, topicNames, level, topicTask);
            if(taskEntity.isEmpty()) break;
            System.out.println(taskEntity.get());
        }
    }
}












