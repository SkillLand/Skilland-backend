package com.skilland.game.demo.repository;

import com.skilland.game.demo.model.user.GameUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<GameUserEntity, Long> {
    Optional<GameUserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
