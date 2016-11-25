package com.boco.miboy.model;

import android.graphics.Bitmap;

public class Question {
    private String question;
    private Bitmap [] images;

    public Question(String question, Bitmap[] images) {
        this.question = question;
        this.images = images;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Bitmap[] getImages() {
        return images;
    }

    public void setImages(Bitmap[] images) {
        this.images = images;
    }
}
