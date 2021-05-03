package com.skilland.game.demo.model.gameroom.resp;

import com.skilland.game.demo.model.user.resp.BriefUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private String title;
    private List<BriefUserResponse> students;
}
