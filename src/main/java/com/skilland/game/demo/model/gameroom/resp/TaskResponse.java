package com.skilland.game.demo.model.gameroom.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private String taskId;
    private String question;
    private String[]task_images;
    private String[]answers;

}
