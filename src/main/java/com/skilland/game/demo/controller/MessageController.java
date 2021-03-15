package com.skilland.game.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@MessageMapping("/chat")
@SendTo("/topic/messages")
public class MessageController {
}
