package com.skilland.game.demo.service.userDataReceiver;


import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.user.GameUserEntity;
import com.skilland.game.demo.model.user.StudentEntity;
import com.skilland.game.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component("ROLE_STUDENT")
public class StudentDataReceiver implements DataReceiverByUserAuthority{

    private final StudentRepository studentRepository;

    @Autowired
    public StudentDataReceiver(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentEntity createEmptyUser() {
        return new StudentEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentEntity getUserByEmail(String email) {
            return studentRepository.findByEmail(email)
                    .orElseThrow(() -> UserExceptions.userNotFound(email));
    }

    @Override
    public Set<CourseEntity> getAllCoursesOfUser(String email) {
        StudentEntity studentEntity = this.getUserByEmail(email);
        return studentEntity.getCoursesStudents();
    }

    @Override
    public Optional<CourseEntity> getCourseByEmailAndCourseName(String email, String courseName) {
        StudentEntity studentEntity = this.studentRepository.findByEmail(email).orElse(null);
        if(studentEntity == null){
            return Optional.empty();
        }
        return studentEntity.getCoursesStudents().stream().filter((item) -> item.getTitle().equals(courseName)).findFirst();
    }

    @Override
    public StudentEntity save(GameUserEntity studentEntity) {
        return this.studentRepository.save((StudentEntity) studentEntity);
    }
}
