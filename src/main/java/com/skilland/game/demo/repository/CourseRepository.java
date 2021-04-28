package com.skilland.game.demo.repository;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>{
    Optional<CourseEntity> findByTitle(String string);


}