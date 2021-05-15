package com.skilland.game.demo;

import com.skilland.game.demo.model.gameroom.TakenTaskEntity;
import com.skilland.game.demo.model.gameroom.jsonb.StudentProgressEntity;
import com.skilland.game.demo.repository.student.StudentProgressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
class StudentProgressRepositoryTest {

    StudentProgressRepository studentProgressRepository;

    StudentProgressRepositoryTest(){
        studentProgressRepository = mock(StudentProgressRepository.class);
    }

    @Test
    public void saveStudentProgressTest(){
        StudentProgressEntity studentProgressEntity = new StudentProgressEntity();
        studentProgressEntity.setGameId((long)1);
        studentProgressEntity.setStudentId((long)2);
        List<TakenTaskEntity> takenTaskEntities = new ArrayList<>();
        takenTaskEntities.add(new TakenTaskEntity("1.JSON", "Многочлены", "1"));
        studentProgressEntity.setTakenTasks(takenTaskEntities);
        when(studentProgressRepository.save(same(studentProgressEntity))).thenReturn(studentProgressEntity);
        StudentProgressEntity result = this.studentProgressRepository.save(studentProgressEntity);
        System.out.println(result);
    }

}