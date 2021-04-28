package com.skilland.game.demo.service.userDataReceiver;


import com.skilland.game.demo.model.gameroom.CourseEntity;
import com.skilland.game.demo.model.user.GameUserEntity;

import java.util.Optional;
import java.util.Set;

public interface DataReceiverByUserAuthority {

    GameUserEntity createEmptyUser();

    GameUserEntity getUserByEmail(String email);

    Set<CourseEntity> getAllCoursesOfUser(String email);

    Optional<CourseEntity> getCourseByEmailAndCourseName(String email, String courseName);

}
