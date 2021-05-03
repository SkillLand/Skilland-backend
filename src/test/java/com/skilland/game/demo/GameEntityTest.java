package com.skilland.game.demo;

import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.TaskEntity;
import com.skilland.game.demo.model.user.GameUserEntity;
import com.skilland.game.demo.model.user.KnownAuthority;
import com.skilland.game.demo.model.user.TeacherEntity;
import com.skilland.game.demo.model.user.UserAuthority;
import com.skilland.game.demo.model.user.request.SaveUserRequest;
import com.skilland.game.demo.model.user.resp.UserResponse;
import com.skilland.game.demo.repository.*;
import com.skilland.game.demo.service.UserService;
import com.skilland.game.demo.service.userDataReceiver.DataReceiverByUserAuthority;
import com.skilland.game.demo.service.userDataReceiver.StudentDataReceiver;
import com.skilland.game.demo.service.userDataReceiver.TeacherDataReceiver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class GameEntityTest {

    UserService userService;

    UserRepository userRepository;

    UserAuthorityRepository authorityRepository;

    PasswordEncoder passwordEncoder;

    Map<String, DataReceiverByUserAuthority> dataReceiverByUserAuthorityMap;



    @Before
    public void setUp(){
        userRepository = mock(UserRepository.class);
        authorityRepository = mock(UserAuthorityRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        dataReceiverByUserAuthorityMap = new HashMap<>();
        dataReceiverByUserAuthorityMap.put("ROLE_STUDENT", new StudentDataReceiver(mock(StudentRepository.class)));
        dataReceiverByUserAuthorityMap.put("ROLE_TEACHER", new TeacherDataReceiver(mock(TeacherRepository.class)));
        userService = new UserService(userRepository, authorityRepository, passwordEncoder,
                dataReceiverByUserAuthorityMap);
    }

    @Test
    public void createUserTest(){
        SaveUserRequest saveUserRequest = new SaveUserRequest("tester1@gmail.com", "12345678", "Sonia", "teacher");
        GameUserEntity user = new TeacherEntity();
        user.setEmail("tester1@gmail.com");
        user.setFirstName("Sonia");
        user.setId((long)1);
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setId(1);
        userAuthority.setValue(KnownAuthority.ROLE_TEACHER);
        when(userRepository.save(same(user))).thenReturn(user);
        when(authorityRepository.findByValue(KnownAuthority.ROLE_TEACHER)).thenReturn(Optional.of(userAuthority));
        UserResponse userResponse = this.userService.create(saveUserRequest);
        System.out.println(userResponse);

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


}












