package com.boco.miboy.backend;

public class Registration {
    private String token;

    public Registration() {
    }

    public Registration(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
