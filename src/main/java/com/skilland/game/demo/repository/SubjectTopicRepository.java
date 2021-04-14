package com.skilland.game.demo.repository;

import java.io.File;

public class SubjectRepository {
    
    private final String pathStart = "src/main/resources/Предметы/";
    
    public boolean existsByName(String subjectName){
        File f = new File("src/test/recourses/testFolder");
        return f.exists();
    }

    public
}
