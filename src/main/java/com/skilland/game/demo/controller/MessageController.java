package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.gameroom.message.HelloMessage;
import com.skilland.game.demo.model.gameroom.message.Message;
import com.skilland.game.demo.service.GameRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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
        String chatId = message.getGameId().toString();
        this.messagingTemplate.convertAndSend("/topic/"+chatId, message);
    }

    @MessageMapping("/game")
    public void processMessage(@Payload HelloMessage helloMessage) {
        String chatId = helloMessage.getGameId().toString();
        this.messagingTemplate.convertAndSend("/topic/"+chatId, helloMessage);
    }

    /*@MessageMapping("/conversation")
    @SendTo("/topic/anotherMessages")
    public OutputMessage sendAnother(Message message) {
        System.out.println("message");
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }*/
}
