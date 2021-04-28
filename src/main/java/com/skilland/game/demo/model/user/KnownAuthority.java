package com.skilland.game.demo.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum KnownAuthority implements GrantedAuthority {
    ROLE_STUDENT,
    ROLE_ADMIN,
    ROLE_TEACHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
