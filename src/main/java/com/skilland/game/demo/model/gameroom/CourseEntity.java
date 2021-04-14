package com.skilland.game.demo.model.gameroom;

import com.skilland.game.demo.model.user.StudentDAO;
import com.skilland.game.demo.model.user.TeacherDAO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
public class CourseDAO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherDAO teacher;

    @ManyToMany
    @JoinTable(name = "courses_students", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<StudentDAO> students = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Game> games = new HashSet<>();

    @Column(name = "subject_name")
    private String subjectName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDAO courseDAO = (CourseDAO) o;
        return title.equals(courseDAO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}
