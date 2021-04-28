package com.skilland.game.demo.model.gameroom;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskEntity {

    private String question;
    private String[]task_images;
    private String[]answers;
    private int correct_answer;
    private String [] tips;
}
