package com.skilland.game.demo.model.user;

import com.skilland.game.demo.model.gameroom.CourseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("TECH")
@Getter
@Setter
public class TeacherEntity extends GameUserEntity {
    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<CourseEntity> coursesTeachers = new HashSet<>();
}
