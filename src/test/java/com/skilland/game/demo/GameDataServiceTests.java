package com.skilland.game.demo;


import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.TaskJsonEntity;
import com.skilland.game.demo.model.gameroom.TaskJsonEntityWrapper;
import com.skilland.game.demo.model.gameroom.TopicLevel;
import com.skilland.game.demo.repository.*;
import com.skilland.game.demo.repository.game.GameFileStorage;
import com.skilland.game.demo.repository.game.GameRepository;
import com.skilland.game.demo.repository.student.StudentRepository;
import com.skilland.game.demo.service.GameDataService;
import com.skilland.game.demo.service.UserService;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.service.userDataReceiver.StudentDataReceiver;
import com.skilland.game.demo.service.userDataReceiver.TeacherDataReceiver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceSetUpConfig.class)
public class GameDataServiceTests {

    GameRepository gameRepository;

    CourseRepository courseRepository;

    SubjectTopicRepository subjectTopicRepository;

    GameDataService dataService;

    GameFileStorage gameFileStorage;


    Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;


    @Before
    public void setUp(){
        courseRepository = mock(CourseRepository.class);
        gameRepository = mock(GameRepository.class);
        subjectTopicRepository = new SubjectTopicRepository();
        subjectTopicRepository.setPathStart("src/test/recourses/Предметы/");
        gameFileStorage = new GameFileStorage();
        dataReceiverByUserAuthorityMap = new HashMap<>();
        dataReceiverByUserAuthorityMap.put("ROLE_STUDENT", new StudentDataReceiver(mock(StudentRepository.class)));
        dataReceiverByUserAuthorityMap.put("ROLE_TEACHER", new TeacherDataReceiver(mock(TeacherRepository.class)));
        UserService userService = new UserService(mock(UserRepository.class), mock(UserAuthorityRepository.class), mock(PasswordEncoder.class),
                dataReceiverByUserAuthorityMap);
        dataService = new GameDataService(userService, gameRepository, courseRepository,
                 subjectTopicRepository, dataReceiverByUserAuthorityMap, gameFileStorage);
    }

    @Test
    public void getSubjectAndTopicTests() throws IOException {
        String presentSubjectName = "Алгебра";
        String absentSubjectName = "123Алгебра";
        SubjectEntity subjectEntity = this.subjectTopicRepository.findSubjectByName(presentSubjectName).get();
        String presentTopicName = "Многочлены";
        String absentTopicName = "123Многочлены";
        TopicEntity topicEntity = this.subjectTopicRepository.findTopicByName(presentSubjectName, presentTopicName).get();
        this.dataService.getSubjectByName(presentSubjectName);
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
        String level1 = "1";
        String level2 = "2";
        Map<TopicLevel, List<String>> topicTask = new HashMap<>();
        topicTask.put(new TopicLevel(topicNames.get(0), level1), new ArrayList<>());
        topicTask.put(new TopicLevel(topicNames.get(1), level1), new ArrayList<>());
        topicTask.put(new TopicLevel(topicNames.get(0), level2), new ArrayList<>());
        topicTask.put(new TopicLevel(topicNames.get(1), level2), new ArrayList<>());
        while(true){
            Optional<TaskJsonEntityWrapper>taskEntity = this.subjectTopicRepository.getRandomTaskOfComplexity(subjectName, topicNames, level1, topicTask);
            if(taskEntity.isEmpty()) break;
            TaskJsonEntityWrapper taskJsonEntityWrapper = taskEntity.get();
            TopicLevel topicLevel = new TopicLevel(taskJsonEntityWrapper.getTopicName(), taskJsonEntityWrapper.getLevel());
            topicTask.get(topicLevel).add(taskJsonEntityWrapper.getTaskName());
            System.out.println("CURRENT MAP STATE:" + topicTask);
            System.out.println(taskJsonEntityWrapper.getTaskJsonEntity());
        }
        while(true){
            Optional<TaskJsonEntityWrapper>taskEntity = this.subjectTopicRepository.getRandomTaskOfComplexity(subjectName, topicNames, level2, topicTask);
            if(taskEntity.isEmpty()) break;
            TaskJsonEntityWrapper taskJsonEntityWrapper = taskEntity.get();
            TopicLevel topicLevel = new TopicLevel(taskJsonEntityWrapper.getTopicName(), taskJsonEntityWrapper.getLevel());
            topicTask.get(topicLevel).add(taskJsonEntityWrapper.getTaskName());
            System.out.println("CURRENT MAP STATE:" + topicTask);
            System.out.println(taskJsonEntityWrapper.getTaskJsonEntity().getTips());
        }
    }

    @Test
    public void createNewSubjectTest() {
       /* String absentSubjectName = "math";
        String presentSubjectName = "physics";
        Subject subject = new Subject();
        subject.setTitle(absentSubjectName);
        Subject presentSubject = new Subject();
        presentSubject.setTitle(presentSubjectName);
        when(subjectTopicRepository.findSubjectByTitle(absentSubjectName)).thenReturn(Optional.empty());
        when(subjectTopicRepository.findSubjectByTitle(presentSubjectName)).thenReturn(Optional.of(presentSubject));
        when(subjectTopicRepository.findSubjectByTitle(absentSubjectName)).thenReturn(Optional.empty());
        when(subjectTopicRepository.save(same(subject))).thenReturn(subject);
        NewSubjectResponse subjectResponse = this.dataService.createNewSubject(absentSubjectName);
        assertThat(subjectResponse.getSubjectName()).isEqualTo(absentSubjectName);

        try {
            this.dataService.createNewSubject(presentSubjectName);

        }catch (ResponseStatusException e){

            Assert.assertNotEquals("", e.getMessage());
        }*/
        //}

  /*
    public void createNewTopicTest(){
        String absentSubjectName = "math";
        String presentSubjectName = "physics";
        Subject subject = new Subject();
        subject.setTitle(absentSubjectName);
        Subject presentSubject = new Subject();
        presentSubject.setTitle(presentSubjectName);
        String topicName = "Linear Algebra";
        Topic topic = new Topic();
        topic.setTitle(topicName);
        when(subjectTopicRepository.findSubjectByTitle(absentSubjectName)).thenReturn(Optional.empty());
        when(subjectTopicRepository.findSubjectByTitle(presentSubjectName)).thenReturn(Optional.of(presentSubject));
        when(subjectTopicRepository.findSubjectByTitle(absentSubjectName)).thenReturn(Optional.empty());
        when(subjectTopicRepository.save(same(subject))).thenReturn(subject);
        when(subjectTopicRepository.save(same(presentSubject))).thenReturn(presentSubject);
        when(topicRepository.save(same(topic))).thenReturn(topic);

        NewTopicResponse topicResponse = this.dataService.createNewTopic(topicName, presentSubjectName);
        assertThat(topicResponse.getSubjectName()).isEqualTo(presentSubjectName);
        assertThat(topicResponse.getTopicName()).isEqualTo(topicName);

        try {
            this.dataService.createNewTopic(topicName, absentSubjectName);

        }catch (ResponseStatusException e){

            Assert.assertNotEquals("", e.getMessage());
        }

        try {
            this.dataService.createNewTopic(topicName, presentSubjectName);

        }catch (ResponseStatusException e){

            Assert.assertNotEquals("", e.getMessage());
        }
    }*/

    /*private void eraseTask(List<String> stringList){
        stringList.remove(0);
    }

    @Test
    public void listDataTest(){
        List<String>stringList = new LinkedList<>();
        stringList.add("1");
        stringList.add("2");
        System.out.println(stringList);
        eraseTask(stringList);
        System.out.println(stringList);
    }*/
    }
}
