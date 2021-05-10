package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.Message;
import com.skilland.game.demo.model.OutputMessage;
import com.skilland.game.demo.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameRoomService gameRoomService;

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate, GameRoomService gameRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.gameRoomService = gameRoomService;
    }

    /*@MessageMapping("/game.addUser")
    public void addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        gameRoomService.addUser(message.getFrom());
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", message.getFrom());



    }*/

    @MessageMapping("/game")
    public void processMessage(@Payload Message message) {
        String chatId = message.getChatId();
        this.messagingTemplate.convertAndSend("/topic/"+chatId, message);



    }

    /*@MessageMapping("/conversation")
    @SendTo("/topic/anotherMessages")
    public OutputMessage sendAnother(Message message) {
        System.out.println("message");
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }*/
}
