package com.skilland.game.demo.service.userDataReceiver;

import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.user.GameUserEntity;
import com.skilland.game.demo.model.user.TeacherEntity;
import com.skilland.game.demo.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component("ROLE_TEACHER")
public class TeacherDataReceiver implements DataReceiverByUserAuthority{

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherDataReceiver(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }


    @Override
    public GameUserEntity createEmptyUser() {
        return new TeacherEntity();
    }

    @Override
    public GameUserEntity getUserByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> UserExceptions.userNotFound(email));
    }

    @Override
    public Set<CourseEntity> getAllCoursesOfUser(String email) {
        TeacherEntity teacherEntity = (TeacherEntity) this.getUserByEmail(email);
        return teacherEntity.getCoursesTeachers();
    }

    @Override
    public Optional<CourseEntity> getCourseByEmailAndCourseName(String email, String courseName) {
        TeacherEntity teacherEntity = this.teacherRepository.findByEmail(email).orElse(null);
        if(teacherEntity == null){
            return Optional.empty();
        }
        return teacherEntity.getCoursesTeachers().stream().filter((item) -> item.getTitle().equals(courseName)).findFirst();
    }
}
