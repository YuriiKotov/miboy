package com.boco.miboy.backend;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    private String data;
    @SerializedName("error_code")
    private String errorCode;

    public ServerResponse() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
