package ru.claims_operator.models;

import org.springframework.security.core.GrantedAuthority;

public enum State implements GrantedAuthority {
    DRAFT,
    SENDED,
    ACCEPTED,
    REJECTED;

    @Override
    public String getAuthority() {
        return name();
    }
}