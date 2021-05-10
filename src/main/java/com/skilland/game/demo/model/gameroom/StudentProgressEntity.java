package com.skilland.game.demo.model.gameroom;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "game_id")
    private Long gameId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "taken_tasks")
    @Basic(fetch = FetchType.EAGER)
    private List<TakenTaskEntity> takenTasks = new ArrayList<>();
}
