package com.skilland.game.demo.model.gameroom.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private long gameId;
    private String gameName;
    private String subjectName;
    private List<String> topicNames;
    private int minTaskLevel;
    private int maxTaskLevel;
    private int durabilityMinutes;
    private String startDate;
}
