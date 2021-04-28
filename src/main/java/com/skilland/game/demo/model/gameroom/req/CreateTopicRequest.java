package com.skilland.game.demo.model.gameroom.req;

import lombok.Data;

@Data
public class CreateTopicRequest {
    private String topicName;
    private String subjectName;
}
