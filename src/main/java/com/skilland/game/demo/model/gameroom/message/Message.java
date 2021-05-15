package com.skilland.game.demo.model.gameroom.message;

import java.util.Date;
import java.util.List;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {

    private Boolean timeOver;
    private Long gameId;
    private List<MessageUserData> members;

}

