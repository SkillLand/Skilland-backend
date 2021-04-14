package com.skilland.game.demo.model.user;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("TEACH")
@Getter
@Setter
public class TeacherDAO extends GameUserDAO{
    @OneToMany(mappedBy = "teacher")
    private Set<CourseEntity> coursesTeachers = new HashSet<>();
}
