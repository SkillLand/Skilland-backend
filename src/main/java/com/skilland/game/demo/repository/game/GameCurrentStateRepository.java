package com.skilland.game.demo.repository.game;

import com.skilland.game.demo.model.gameroom.jsonb.GameCurrentStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GameCurrentStateRepository extends JpaRepository<GameCurrentStateEntity, Long> {

    @Query(
            value = "SELECT g FROM GameCurrentStateEntity g WHERE g.gameId = :game_id " +
                    "and g.createdAt in (SELECT max(g.createdAt) from GameCurrentStateEntity g)")
    List<GameCurrentStateEntity> findLatestRecordByGameId(@Param("game_id") Long gameId);
}

