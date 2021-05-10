package com.skilland.game.demo.model.gameroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameComplexEntity {
    private long id;
    private Timestamp dateTime;
    private String subjectName;
    private List<String> topicNames;
    private int minTaskLevel;
    private int maxTaskLevel;
    private int durabilityMinutes;
}
