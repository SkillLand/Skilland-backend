package com.skilland.game.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilland.game.demo.model.SubjectEntity;
import com.skilland.game.demo.model.TopicEntity;
import com.skilland.game.demo.model.gameroom.TaskJsonEntity;
import com.skilland.game.demo.model.gameroom.TaskJsonEntityWrapper;
import com.skilland.game.demo.model.gameroom.TopicLevel;
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

        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setTopicName(topicName);
        topicEntity.setSubjectName(subjectName);
        return Optional.of(topicEntity);
    }


    public Optional<TaskJsonEntity> findTaskByNameAndLevel(String subjectName, String topicName, String level, String name) throws IOException {
        File taskFile  = new File(this.pathStart+"/"+subjectName+"/"+topicName + "/" + level + "/"+ name);
        if (!taskFile.exists()){
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TaskJsonEntity taskJsonEntity = objectMapper.readValue(taskFile, TaskJsonEntity.class);
        return Optional.of(taskJsonEntity);

    }

    private Optional<String []> findTasksOfLevel(String subjectName, String topicName, String level){
        File tasks = new File(this.pathStart+"/"+subjectName+"/"+topicName+"/"+level);
        if (!tasks.exists()){
            return Optional.empty();
        }
        return Optional.of(tasks.list());
    }

    public Optional<TaskJsonEntityWrapper> getRandomTaskOfComplexity(String subjectName, List<String> topicNames, String level,
                                                                     Map<TopicLevel, List<String>> excludeTopicTasks) throws IOException {
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
                TopicLevel topicLevel = new TopicLevel(topicName, level);
                if (excludeTopicTasks.containsKey(topicLevel) && excludeTopicTasks.get(topicLevel).contains(taskName)){
                    tasks.remove(taskName);
                }else {
                    return Optional.of(new TaskJsonEntityWrapper(this.findTaskByNameAndLevel(subjectName, topicName, level, taskName).get(), topicName, level, taskName));}
            }
            topicNamesCopy.remove(topicNumber);

        }
        return Optional.empty();

    }


}
