package com.skilland.game.demo.model.gameroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TakenTaskEntity {

    private String taskId;
    private String topicName;
    private String level;
}
