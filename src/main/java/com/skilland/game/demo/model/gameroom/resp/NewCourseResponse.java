package com.skilland.game.demo.model.gameroom.resp;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import lombok.Data;

@Data
public class NewCourseResponse {

    private String courseName;

    private String ownerEmail;

    public static NewCourseResponse fromCourse(CourseEntity courseEntity){
        NewCourseResponse response = new NewCourseResponse();
        response.setCourseName(courseEntity.getTitle());
        response.setOwnerEmail(courseEntity.getTeacher().getEmail());
        return response;
    }

}
