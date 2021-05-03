package com.skilland.game.demo.model.gameroom.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BriefCourseResponse {

    private Long courseId;
    public String courseName;
}
