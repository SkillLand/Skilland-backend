package com.skilland.game.demo.controller;


import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.gameroom.req.AddingStudentToCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.KnownAuthority;
import com.skilland.game.demo.model.user.resp.UserResponse;
import com.skilland.game.demo.model.user.request.SaveUserRequest;
import com.skilland.game.demo.service.GameDataService;
import com.skilland.game.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal String email) {
        return userService.findByEmail(email).orElseThrow(() -> UserExceptions.userNotFound(email));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid SaveUserRequest request){
        return userService.create(request);
    }

}
