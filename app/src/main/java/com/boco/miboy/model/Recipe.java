package com.boco.miboy.model;

import java.util.List;

public class Recipe {
    private int time;
    private String title;
    private String instruction;
    private String image;
    private List<List<String>> ingredients;
    private List<String> buy;

    public Recipe(int time, String title, String instruction, String image, List<List<String>> ingredients, List<String> buy) {
        this.time = time;
        this.title = title;
        this.instruction = instruction;
        this.image = image;
        this.ingredients = ingredients;
        this.buy = buy;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<List<String>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<List<String>> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getBuy() {
        return buy;
    }

    public void setBuy(List<String> buy) {
        this.buy = buy;
    }

    @Override
    public String toString() {
        return "{" +
                "\"time\"=" + time +
                ", \"title\"='" + title + '\'' +
                ", \"instruction\"='" + instruction + '\'' +
                ", \"image\"='" + image + '\'' +
                ", \"ingredients\"=" + ingredients +
                ", \"buy\"=" + buy +
                '}';
    }
}