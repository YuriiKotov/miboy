package com.boco.miboy.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class History extends RealmObject {
    @PrimaryKey
    private long timestamp;
    private byte [] photo;
    private String json;

    public History() {
    }

    public History(long timestamp, byte[] photo, String json) {
        this.timestamp = timestamp;
        this.photo = photo;
        this.json = json;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
