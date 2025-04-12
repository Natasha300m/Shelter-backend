package com.shelter.mykyda.database.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    VOLUNTEER,
    MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
