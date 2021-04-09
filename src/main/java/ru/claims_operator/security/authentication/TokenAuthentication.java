/*
 * 44.Rest_API_Spring_Boot. 19.12.2020, 12:37 / 19.12.2020, 12:38   @A.Alexandr
 * Copyright (c)
 */
package ru.claims_operator.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class TokenAuthentication implements Authentication  {
    private UserDetails userDetails;
    private String token;
    private Boolean isAuthenticated = false;


    public TokenAuthentication(String token) {
        this.token = token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuth) throws IllegalArgumentException {
        this.isAuthenticated = isAuth;
    }

    @Override
    public String getName() {
        return token;
    }


    public void setToken(String token) {// set-тер на token
        this.token = token;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
