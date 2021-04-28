package com.skilland.game.demo.controller;


import com.skilland.game.demo.exception.UserExceptions;
import com.skilland.game.demo.model.gameroom.req.AddingStudentToCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.resp.UserResponse;
import com.skilland.game.demo.model.user.request.SaveUserRequest;
import com.skilland.game.demo.service.PrebuiltDataService;
import com.skilland.game.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final PrebuiltDataService dataService;

    public UserController(UserService userService, PrebuiltDataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
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

    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public NewCourseResponse createCourse(@AuthenticationPrincipal String email, @RequestBody CreateCourseRequest request) {
        return this.userService.createNewCourse(email, request.getCourseName());
    }


    @PostMapping("/courses/games")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@AuthenticationPrincipal String email, @RequestBody CreateGameRequest request) throws IOException {
        return this.userService.createNewGame(email, request.getCourseName(), request.getTopicNames(), request.getGameName(),
                request.getSubjectName(), request.getGameDurabilityMinutes(), request.getMaxTaskLevel(), request.getMinTaskLevel(), request.getStartDate());
    }

    @GetMapping("/courses/games")
    public List<GameResponse> getAllGamesForUser(@AuthenticationPrincipal String email, @AuthenticationPrincipal String [] authorities, @RequestParam String courseName){
        return this.userService.getAllGamesOfUserInCourse(email, courseName, authorities[0]);
    }

    @PatchMapping("/courses")
    public void joinNewCourseByStudent(@AuthenticationPrincipal String email, @RequestBody AddingStudentToCourseRequest request){
        this.userService.addStudentToCourse(email, request.getCourseId());
    }

    @GetMapping("/courses/")
    public List<CourseResponse> getAllAvailableCourses(@AuthenticationPrincipal String email, @AuthenticationPrincipal String [] authorities){
        return this.userService.getAllAvailableCourses(email, authorities[0]);
    }




}
