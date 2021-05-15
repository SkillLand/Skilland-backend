package com.skilland.game.demo.model.gameroom;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TakenTaskEntity implements Serializable {

    private String taskId;
    private String topicName;
    private String level;
}
