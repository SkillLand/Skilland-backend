package com.skilland.game.demo.repository;

import com.skilland.game.demo.model.user.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {
    Optional<TeacherEntity> findByEmail(String email);
}
