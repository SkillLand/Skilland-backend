package com.skilland.game.demo.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.skilland.game.demo.config.security.SecurityConstants;
import com.skilland.game.demo.config.security.properties.JWTProperties;
import com.skilland.game.demo.config.security.properties.SecurityProperties;
import com.skilland.game.demo.model.auth.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtAuthService {

    private final JWTProperties jwtProperties;

    @Autowired
    public JwtAuthService(SecurityProperties jwtProperties) {

        this.jwtProperties = jwtProperties.getJwt();
    }

    public UserLoginResponse getToken(UserDetails user){
        long now = System.currentTimeMillis();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + jwtProperties.getExpireIn()))
                .withArrayClaim(SecurityConstants.AUTHORITIES_CLAIM, user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret().getBytes()));

        return new UserLoginResponse(token, jwtProperties.getExpireIn());
    }


}