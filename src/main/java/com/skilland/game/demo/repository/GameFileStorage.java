package com.skilland.game.demo.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilland.game.demo.model.gameroom.GameJsonEntity;
import com.skilland.game.demo.model.gameroom.TaskEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
@Getter
@Setter
public class GameFileStorage {

    private String pathStart = "src/main/resources/Игры";

    public synchronized void save(GameJsonEntity gameJsonEntity, String subjectName, String gameId) throws IOException {
        String pathToGame = this.pathStart+"/"+subjectName+"/"+gameId+".json";
        File gameFile  = new File(pathToGame);
        if(!gameFile.exists()){
            gameFile.createNewFile();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(gameFile, gameJsonEntity);
    }

    public synchronized Optional<GameJsonEntity> findGameBySubjectAndId(String subjectName, String gameId) {
        try {
            String pathToGame = this.pathStart + "/" + subjectName + "/" + gameId + ".json";
            File gameFile = new File(pathToGame);
            if (!gameFile.exists()) {
                return Optional.empty();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            GameJsonEntity gameEntity = objectMapper.readValue(gameFile, GameJsonEntity.class);
            return Optional.of(gameEntity);
        }catch (IOException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
