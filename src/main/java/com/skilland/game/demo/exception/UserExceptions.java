package com.skilland.game.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GameUserExceptions {


    public static ResponseStatusException authorityNotFound(String value){
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User authority "+value+" not found");
    }

    public static ResponseStatusException userNotFound(String value){
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email  "+value+" not found");
    }

    public static ResponseStatusException duplicateEmail(String email) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + email + " already taken");
    }

    public static ResponseStatusException wrongPassword() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is incorrect");
    }
}
