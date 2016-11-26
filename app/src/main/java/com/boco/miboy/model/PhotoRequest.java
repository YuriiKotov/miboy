package com.boco.miboy.model;

public class PhotoRequest {
    private String image;
    private String token;

    public PhotoRequest() {
    }

    public PhotoRequest(String token, String image) {
        this.token = token;
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}