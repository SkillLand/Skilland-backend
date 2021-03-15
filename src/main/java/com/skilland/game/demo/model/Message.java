package com.skilland.game.demo.model;

import java.util.Date;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Message {

    private String from;
    private String text;
    private String chatId;




}

