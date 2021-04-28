package com.skilland.game.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.TaskEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
@Component
public class SubjectTopicRepository {
    
    private String pathStart = "src/main/resources/Предметы/";
    
    public synchronized boolean existsSubjectByName(String subjectName){
        File f = new File(this.pathStart+"/"+subjectName);
        return f.exists();
    }

    public synchronized Optional<SubjectEntity> findSubjectByName(String subjectName){
        File subj = new File(this.pathStart+"/"+subjectName);

        if (!subj.exists()){
            return Optional.empty();
        }
        String [] arrPathNames = subj.list();
        List<String> topicNamesList = new ArrayList<>();
        if(arrPathNames != null){
            topicNamesList = Arrays.asList(arrPathNames);
        }
        SubjectEntity subjectEntity = new SubjectEntity();
        subjectEntity.setTitle(subjectName);
        subjectEntity.setTopicNames(topicNamesList);
        return Optional.of(subjectEntity);
    }

    public synchronized List<String> findAllSubjectNames(){
        File subj = new File(this.pathStart);
        List<SubjectEntity> subjects = new ArrayList<>();
        if (!subj.exists() || subj.list()==null){
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(subj.list()));
    }

    public synchronized boolean existsTopicByName(String subjectName, String topicName){
        File f = new File(this.pathStart+"/"+subjectName+"/"+topicName);
        return f.exists();
    }

    public synchronized Optional<TopicEntity> findTopicByName(String subjectName, String topicName) throws IOException {
        File topicFile  = new File(this.pathStart+"/"+subjectName+"/"+topicName);
        if (!topicFile.exists()){
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TopicEntity topicEntity = objectMapper.readValue(topicFile, TopicEntity.class);
        return Optional.of(topicEntity);
    }


    public Optional<TaskEntity> findTaskByNameAndLevel(String subjectName, String topicName, String level, String name) throws IOException {
        File taskFile  = new File(this.pathStart+"/"+subjectName+"/"+topicName + "/" + level + "/"+ name);
        if (!taskFile.exists()){
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TaskEntity taskEntity = objectMapper.readValue(taskFile, TaskEntity.class);
        return Optional.of(taskEntity);

    }

    private Optional<String []> findTasksOfLevel(String subjectName, String topicName, String level){
        File tasks = new File(this.pathStart+"/"+subjectName+"/"+topicName+"/"+level);
        if (!tasks.exists()){
            return Optional.empty();
        }
        return Optional.of(tasks.list());
    }

    public Optional<TaskEntity> getRandomTaskOfComplexity(String subjectName, List<String> topicNames, String level,
                                                          Map<String, List<String>> excludeTopicTasks) throws IOException {
        Random random = new Random();
        List<String>topicNamesCopy = new ArrayList<>(topicNames);
        while (!topicNamesCopy.isEmpty()) {

            int maxTopicNumber = topicNamesCopy.size();
            int topicNumber = random.nextInt(maxTopicNumber);
            String topicName = topicNamesCopy.get(topicNumber);
            TopicEntity topicEntity = this.findTopicByName(subjectName, topicName).orElse(null);
            if (topicEntity == null) return Optional.empty();
            List<String> tasks = new ArrayList<>(Arrays.asList(this.findTasksOfLevel(subjectName, topicName, level).orElseGet(() -> new String[0])));
            while(tasks.size()!=0){
                int taskNumber = random.nextInt(tasks.size());
                String taskName = tasks.get(taskNumber);
                if (excludeTopicTasks.containsKey(topicName) && excludeTopicTasks.get(topicName).contains(taskName)){
                    tasks.remove(taskName);
                }else {
                    excludeTopicTasks.get(topicName).add(taskName);
                    return this.findTaskByNameAndLevel(subjectName, topicName, level, taskName);}
            }
            topicNamesCopy.remove(topicNumber);

        }
        return Optional.empty();

    }


}
