package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.gameroom.message.HelloMessage;
import com.skilland.game.demo.model.gameroom.message.Message;
import com.skilland.game.demo.model.gameroom.resp.TaskResponse;
import com.skilland.game.demo.service.GameRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class GameRoomController {

    private final GameRoomService gameRoomService;

    private final SimpMessagingTemplate messagingTemplate;

    public GameRoomController(GameRoomService gameRoomService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomService = gameRoomService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/task")
    public ResponseEntity<?> getRandomTaskOfComplexity(@RequestParam Long studentId, @RequestParam Long gameId, @RequestParam Integer level) throws IOException {
        Optional<TaskResponse> taskResponse = this.gameRoomService.getRandomTaskOfComplexity(studentId, gameId, level.toString());
        if(taskResponse.isEmpty()){
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }else
            return new ResponseEntity<>(taskResponse.get(), HttpStatus.OK);
    }

}
