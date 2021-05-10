package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.gameroom.resp.TaskResponse;
import com.skilland.game.demo.service.GameRoomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class GameRoomController {

    private final GameRoomService gameRoomService;

    public GameRoomController(GameRoomService gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @GetMapping("/task")
    public void getRandomTaskOfComplexity(@RequestParam Long studentId, @RequestParam Long gameId, @RequestParam Integer level) throws IOException {
        Optional<TaskResponse> taskResponse = this.gameRoomService.getRandomTaskOfComplexity(studentId, gameId, level.toString());
    }
}
