package com.skilland.game.demo.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OutputMessage {

    private String from;
    private String text;
    private String date;
    private String chatId;




}
