package com.boco.miboy.model;

import android.graphics.drawable.Drawable;

import java.util.List;

public class Question {
    private int number;
    private String question;
    private List<Drawable> images;

    public Question(int number, String question, List<Drawable> images) {
        this.number = number;
        this.question = question;
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Drawable> getImages() {
        return images;
    }

    public void setImages(List<Drawable> images) {
        this.images = images;
    }
}
