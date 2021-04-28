package com.skilland.game.demo.model.user;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorValue("STUD")
@Getter
@Setter
public class StudentEntity extends GameUserEntity {
    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private Set<CourseEntity> coursesStudents;
}
