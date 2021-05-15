package com.skilland.game.demo.model.gameroom;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TakenTaskEntity {

    private String taskId;
    private String topicName;
    private String level;
}
