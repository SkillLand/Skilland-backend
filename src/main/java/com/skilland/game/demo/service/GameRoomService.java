package com.skilland.game.demo.service;

import com.skilland.game.demo.model.user.GameUserEntity;
import com.skilland.game.demo.model.user.StudentEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GameRoomService {

    private final Set<GameUserEntity> gameUserEntities = new HashSet();

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
    }

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
