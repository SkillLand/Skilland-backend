package com.skilland.game.demo.repository.game;

import com.skilland.game.demo.model.gameroom.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    Optional<GameEntity> getGameEntityById(Long id);
}
