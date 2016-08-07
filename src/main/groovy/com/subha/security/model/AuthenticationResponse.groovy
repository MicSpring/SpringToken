package com.subha.security.model

/**
 * Created by user on 8/7/2016.
 */
public class AuthenticationResponse {

    private String token;

    public AuthenticationResponse() {
        super();
    }

    public AuthenticationResponse(String token) {
        this.setToken(token);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

