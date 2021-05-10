package com.skilland.game.demo.repository.student;

import com.skilland.game.demo.model.gameroom.StudentProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProgressRepository extends JpaRepository<StudentProgressEntity, Long> {
    Optional<StudentProgressEntity> findStudentProgressEntityByStudentIdAndGameId(Long studentId, Long gameId);
}
