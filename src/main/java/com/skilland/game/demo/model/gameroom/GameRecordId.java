package com.skilland.game.demo.model.gameroom;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameRecordId implements Serializable {
    @Column(name = "student_id")
    private long studentId;

    @Column(name = "game_id")
    private long gameId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRecordId that = (GameRecordId) o;
        return studentId == that.studentId && gameId == that.gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, gameId);
    }
}
