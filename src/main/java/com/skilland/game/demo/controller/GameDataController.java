package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.gameroom.req.AddingStudentToCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateCourseRequest;
import com.skilland.game.demo.model.gameroom.req.CreateGameRequest;
import com.skilland.game.demo.model.gameroom.resp.BriefCourseResponse;
import com.skilland.game.demo.model.gameroom.resp.CourseResponse;
import com.skilland.game.demo.model.gameroom.resp.GameResponse;
import com.skilland.game.demo.model.gameroom.resp.NewCourseResponse;
import com.skilland.game.demo.model.user.KnownAuthority;
import com.skilland.game.demo.model.user.resp.BriefUserResponse;
import com.skilland.game.demo.service.GameDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GameDataController {

    private final GameDataService dataService;

    @Autowired
    public GameDataController(GameDataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/courses")
    @ResponseStatus(HttpStatus.CREATED)
    public NewCourseResponse createCourse(@AuthenticationPrincipal String email, @RequestBody CreateCourseRequest request) {
        return this.dataService.createNewCourse(email, request.getCourseName());
    }


    @PostMapping("/courses/games")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@AuthenticationPrincipal String email, @RequestBody CreateGameRequest request) throws IOException {
        return this.dataService.createNewGame(email, request);
    }

    @GetMapping("/courses/games")
    public List<GameResponse> getAllGamesForUser(@AuthenticationPrincipal String email, @RequestParam String courseName){
        String authority = ((KnownAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]).getAuthority();
        return this.dataService.getAllGamesOfUserInCourse(email, courseName, authority);
    }

    @DeleteMapping("/courses/participants")
    public CourseResponse deleteMemberFromCourse(@AuthenticationPrincipal String email, @RequestParam String participantEmail, @RequestParam Long courseId){
        return this.dataService.deleteStudentFromCourse(email, participantEmail, courseId);
    }


    @PostMapping("/courses/participants")
    public void joinNewCourseByStudent(@AuthenticationPrincipal String email, @RequestBody AddingStudentToCourseRequest request){
        this.dataService.addStudentToCourse(email, request.getCourseId());
    }

    @GetMapping("/courses/participants")
    public List<BriefUserResponse> getParticipantsOfCurrentCourse(@AuthenticationPrincipal String email, @RequestParam Long courseId){
        return dataService.getCourseParticipantsOfTeacher(email, courseId);
    }

    @GetMapping("/courses")
    public List<BriefCourseResponse> getAllAvailableCourses(@AuthenticationPrincipal String email){
        String authority = ((KnownAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0]).getAuthority();
        return this.dataService.getAllAvailableCourses(email, authority);
    }

    @DeleteMapping("/courses")
    public void deleteCourseOfTeacher(@AuthenticationPrincipal String email, @RequestParam Long courseId){
        this.dataService.deleteCourseByTeacherAndCourseId(email, courseId);
    }


}
