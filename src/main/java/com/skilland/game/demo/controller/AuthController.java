package com.skilland.game.demo.controller;

import com.skilland.game.demo.model.auth.UserLoginRequest;
import com.skilland.game.demo.model.auth.UserLoginResponse;
import com.skilland.game.demo.service.JwtAuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class AuthController {

    private final JwtAuthService authService;

    public AuthController(JwtAuthService authService) {
        this.authService = authService;
    }



    /*
     * JWTAuthenticationFilter sets the prixnciple (user-details from UserService) using auth manager
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestBody(content = @Content(schema = @Schema(implementation = UserLoginRequest.class)))
    public UserLoginResponse login(@AuthenticationPrincipal UserDetails userDetails) {
        return authService.getToken(userDetails);
    }

}