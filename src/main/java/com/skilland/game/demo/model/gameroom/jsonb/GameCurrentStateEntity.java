package com.skilland.game.demo.model.gameroom.jsonb;

import com.skilland.game.demo.model.gameroom.message.MessageUserData;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
@Table(name = "game_current_state")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCurrentStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_over")
    private Boolean timeOver;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "game_id")
    private Long gameId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "members")
    @Basic(fetch = FetchType.EAGER)
    private List<MessageUserData> members;
}
