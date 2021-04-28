package com.skilland.game.demo.model.gameroom;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class GameJsonEntity {
    private List<String> topicNames;
    private int minTaskLevel;
    private int maxTaskLevel;
    private int durabilityMinutes;
}
