package com.skilland.game.demo.model.gameroom;

import com.skilland.game.demo.model.user.StudentEntity;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "game_record")
@Getter
@Setter
public class GameRecordEntity {

    @EmbeddedId
    private GameRecordId gameRecordId;

    @ManyToOne
    @MapsId("studentId")
    private StudentEntity student;

    @ManyToOne
    @MapsId("gameId")
    private GameEntity game;

    @Column(name = "date_time", nullable = false)
    private Timestamp dateTime;

    @Column(name = "student_score")
    private int studentScore;

    @Column(name = "max_game_score")
    private int maxPossibleGameScore;

}
