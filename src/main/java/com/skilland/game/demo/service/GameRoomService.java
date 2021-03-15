package com.skilland.game.demo.service;

import com.skilland.game.demo.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class GameRoomService {

    private final Set<User> users = new HashSet();

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

    public User addUser(String email){
        User user = new User();
        user.setEmail(email);
        users.add(user);
        return user;
    }

    public User addUserToRoom(String email, String roomId){
        User user = new User();
        user.setEmail(email);
        if(users.contains(user)){
            User finalUser = user;
            user = users.stream().filter((finalUser::equals)).findFirst().get();
        }
        if (user.getGameRooms().contains(roomId)){
            return user;
        }
        user.getGameRooms().add(roomId);
        return user;
    }
}
