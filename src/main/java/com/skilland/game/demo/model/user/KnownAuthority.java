package com.skilland.game.demo.model;

import org.springframework.security.core.GrantedAuthority;

public enum KnownAuthority implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_REGULAR_TEACHER,
    ROLE_ADVANCED_TEACHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
