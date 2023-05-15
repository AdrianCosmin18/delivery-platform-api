package com.example.deliveryapp.DTOs;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthenticationResponse {
    public Long userId;
    public String email;
    public String firstName;
    public String token;
    public Collection<? extends GrantedAuthority> authorities;


    public AuthenticationResponse(Long userId, String email, String firstName, String token, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.token = token;
        this.authorities = authorities;
    }
}
