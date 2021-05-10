package com.skilland.game.demo.service;

import com.skilland.game.demo.exception.GameDataException;
import com.skilland.game.demo.model.gameroom.*;
import com.skilland.game.demo.model.gameroom.resp.TaskResponse;
import com.skilland.game.demo.repository.game.GameFileStorage;
import com.skilland.game.demo.repository.game.GameRepository;
import com.skilland.game.demo.repository.student.StudentProgressRepository;
import com.skilland.game.demo.repository.SubjectTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
public class GameRoomService {

    private final GameRepository gameRepository;

    private final GameFileStorage gameFileStorage;

    private final SubjectTopicRepository subjectTopicRepository;

    private final StudentProgressRepository studentProgressRepository;

    @Autowired
    public GameRoomService(GameRepository gameRepository, GameFileStorage gameFileStorage, SubjectTopicRepository subjectTopicRepository, StudentProgressRepository studentProgressRepository) {
        this.gameRepository = gameRepository;
        this.gameFileStorage = gameFileStorage;
        this.subjectTopicRepository = subjectTopicRepository;
        this.studentProgressRepository = studentProgressRepository;
    }


    public GameComplexEntity getGameById(Long gameId){
        GameEntity gameEntity = gameRepository.getGameEntityById(gameId).orElseThrow(() -> GameDataException.gameNotFound(gameId.toString()));
        GameJsonEntity gameJsonEntity = gameFileStorage.findGameBySubjectAndId(gameEntity.getSubjectName(), gameId.toString())
                .orElseThrow(() -> GameDataException.gameNotFound(gameId.toString()));
        return new GameComplexEntity(gameEntity.getId(), gameEntity.getDateTime(), gameEntity.getSubjectName(),
                gameJsonEntity.getTopicNames(), gameJsonEntity.getMinTaskLevel(), gameJsonEntity.getMaxTaskLevel(), gameJsonEntity.getDurabilityMinutes());
    }

    @Transactional
    public void addStudentProgressRecord(Long studentId, Long gameId, TakenTaskEntity takenTask){
        StudentProgressEntity studentProgressEntity = this.studentProgressRepository.findStudentProgressEntityByStudentIdAndGameId(studentId, gameId)
                .orElseGet(StudentProgressEntity::new);
        if(studentProgressEntity.getId() == null){
            studentProgressEntity.setStudentId(studentId);
            studentProgressEntity.setGameId(gameId);
        }
        studentProgressEntity.getTakenTasks().add(takenTask);
        this.studentProgressRepository.save(studentProgressEntity);
    }

    private Map<TopicLevel, List<String>> getCompletedTasksByStudent(Long studentId, Long gameId){
        Map<TopicLevel, List<String>> completedTasks = new HashMap<>();
        Optional<StudentProgressEntity> optionalStudentProgressEntity = this.studentProgressRepository.findStudentProgressEntityByStudentIdAndGameId(studentId, gameId);
        if(optionalStudentProgressEntity.isEmpty()){
            return completedTasks;
        }
        StudentProgressEntity studentProgressEntity = optionalStudentProgressEntity.get();
        for (TakenTaskEntity item: studentProgressEntity.getTakenTasks()){
            TopicLevel topicLevel = new TopicLevel(item.getTopicName(), item.getLevel());
            if(!completedTasks.containsKey(topicLevel)){
                completedTasks.put(topicLevel, new ArrayList<>());
            }
            completedTasks.get(topicLevel).add(item.getTaskId());
        }
        return completedTasks;

    }


    private void initExtraTopicLevelsForCompletedTasks( Map<TopicLevel, List<String>> completedTasks, String level, List<String> topicNames){
        for(String topicName: topicNames){
            TopicLevel topicLevel = new TopicLevel(topicName, level);
            if(!completedTasks.containsKey(topicLevel)){
                completedTasks.put(topicLevel, new ArrayList<>());
            }
        }
    }

    public Optional<TaskResponse> getRandomTaskOfComplexity(Long studentId, Long gameId, String level) throws IOException {
        Map<TopicLevel, List<String>> completedTasks = this.getCompletedTasksByStudent(studentId, gameId);
        GameComplexEntity gameComplexEntity = this.getGameById(gameId);
        initExtraTopicLevelsForCompletedTasks(completedTasks, level, gameComplexEntity.getTopicNames());
        Optional<TaskJsonEntityWrapper>taskEntityOptional = this.subjectTopicRepository.getRandomTaskOfComplexity(gameComplexEntity.getSubjectName(), gameComplexEntity.getTopicNames(),
                level, completedTasks);
        if(taskEntityOptional.isEmpty()) return Optional.empty();
        TaskJsonEntityWrapper taskJsonEntityWrapper = taskEntityOptional.get();
        TaskJsonEntity taskJsonEntity = taskJsonEntityWrapper.getTaskJsonEntity();
        this.addStudentProgressRecord(studentId, gameId, new TakenTaskEntity(taskJsonEntityWrapper.getTaskName(), taskJsonEntityWrapper.getTopicName(), level));
        TaskResponse taskResponse = new TaskResponse(taskJsonEntityWrapper.getTaskName(), taskJsonEntity.getQuestion(), taskJsonEntity.getTask_images(),
                taskJsonEntity.getAnswers());
        return Optional.of(taskResponse);
    }



    /*private final Set<GameUserEntity> gameUserEntities = new HashSet();

    private final Set<String>roomIds = new HashSet();

    public GameRoomService() {
        roomIds.add("123");
        roomIds.add("456");
        roomIds.add("789");
    }

    public String createRoomId(){
        int length = 20;
        boolean useLetters = true;
        boolean useNumbers = true;
        String newRoomId = RandomStringUtils.random(length, useLetters, useNumbers);
        while(roomIds.contains(newRoomId)){
            newRoomId = RandomStringUtils.random(length, useLetters, useNumbers);
        }
        roomIds.add(newRoomId);
        return newRoomId;
    }

    public GameUserEntity addUser(String email){
        GameUserEntity gameUserEntity = new StudentEntity();
        gameUserEntity.setEmail(email);
        gameUserEntities.add(gameUserEntity);
        return gameUserEntity;
    }*/

    /*public GameUser addUserToRoom(String email, String roomId){
        GameUser gameUser = new GameUser();
        gameUser.setEmail(email);
        if(gameUsers.contains(gameUser)){
            GameUser finalGameUser = gameUser;
            gameUser = gameUsers.stream().filter((finalGameUser::equals)).findFirst().get();
        }
        if (gameUser.getGameRooms().contains(roomId)){
            return gameUser;
        }
        gameUser.getGameRooms().add(roomId);
        return gameUser;
    }*/
}
