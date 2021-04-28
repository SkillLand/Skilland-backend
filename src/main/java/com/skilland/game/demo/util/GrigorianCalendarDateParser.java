package com.skilland.game.demo.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GrigorianCalendarDateParser {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static Date parseDateFromString(String dateString){
        try {
            return format.parse(dateString);
        }catch (ParseException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "wrong date format "+dateString);
        }
    }

}
