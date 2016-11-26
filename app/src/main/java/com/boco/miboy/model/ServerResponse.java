package com.boco.miboy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {
    private List<Recipe> data;
    @SerializedName("error_code")
    private int errorCode;

    public ServerResponse() {
    }

    public List<Recipe> getData() {
        return data;
    }

    public void setData(List<Recipe> data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "{" +
                "data='" + data + '\'' +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}