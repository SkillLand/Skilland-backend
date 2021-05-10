package com.skilland.game.demo.model.gameroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskJsonEntityWrapper {
    TaskJsonEntity taskJsonEntity;
    String topicName;
    String level;
    String taskName;

}
