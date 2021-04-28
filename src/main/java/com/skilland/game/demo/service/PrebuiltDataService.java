package com.skilland.game.demo.service;


import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrebuiltDataService {

    private final GameRepository gameRepository;

    private final CourseRepository courseRepository;

    private final SubjectTopicRepository subjectTopicRepository;


    @Autowired
    public PrebuiltDataService(GameRepository gameRepository, CourseRepository courseRepository,
                                SubjectTopicRepository subjectTopicRepository) {
        this.gameRepository = gameRepository;
        this.courseRepository = courseRepository;
        this.subjectTopicRepository = subjectTopicRepository;
    }



    public SubjectEntity getSubjectByName(String title){
        return this.subjectTopicRepository.findSubjectByName(title).orElseThrow(() -> GameDataException.subjectNotFound(title));
    }


}
