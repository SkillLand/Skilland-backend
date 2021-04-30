package com.skilland.game.demo;


import com.skilland.game.demo.repository.*;
import com.skilland.game.demo.service.GamaDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class GamaDataServiceTests {


    GameRepository gameRepository;

    CourseRepository courseRepository;


    SubjectTopicRepository subjectTopicRepository;

    GamaDataService dataService;


    @Before
    public void setUp(){
        courseRepository = mock(CourseRepository.class);
        gameRepository = mock(GameRepository.class);
        subjectTopicRepository = new SubjectTopicRepository();
        dataService = new GamaDataService(userService, gameRepository, courseRepository,
                 subjectTopicRepository, dataReceiverByUserAuthorityMap, gameFileStorage);
    }

    @Test
    public void createNewSubjectTest(){
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
    }

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

    private void eraseTask(List<String> stringList){
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
    }

}
