package com.skilland.game.demo.model.user;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("ST")
@Getter
@Setter
public class StudentDAO extends GameUserDAO{
    @ManyToMany(mappedBy = "students")
    private List<CourseEntity> coursesStudents;
}
