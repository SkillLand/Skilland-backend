package com.skilland.game.demo.model.gameroom;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
@Getter
@Setter
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="title")
    private String title;

   /* @Column(name = "durability")
    private int durabilityMinutes;

    @Column(name = "max_task_level")
    private int maxTaskLevel;

    @Column(name = "min_task_level")
    private int minTaskLevel;

    @Column(name = "start_date")
    private String startDate;*/

    @ManyToMany
    @JoinTable(name = "games_courses", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private Set<CourseEntity> course = new HashSet<>();

   /* @ManyToMany
    @JoinTable(name = "games_topics", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"))
    private Set<Topic> topics = new HashSet<>();*/

    public Game() {
    }
}
