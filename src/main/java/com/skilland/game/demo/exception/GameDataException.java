package com.skilland.game.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GameDataException {
    public static ResponseStatusException gameNotFound(String gameId){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game with id "+gameId+" not found");
    }

    public static ResponseStatusException courseNotFound(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course "+value+" not found");
    }

    public static ResponseStatusException subjectNotFound(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject "+value+" not found");
    }

    public static ResponseStatusException topicNotFound(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic "+value+" not found");
    }

    public static ResponseStatusException courseAlreadyExists(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course "+value+" alreadyExists");
    }

    public static ResponseStatusException subjectAlreadyExists(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subject "+value+" alreadyExists");
    }

    public static ResponseStatusException topicAlreadyExists(String value){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Topic "+value+" alreadyExists");
    }
}
