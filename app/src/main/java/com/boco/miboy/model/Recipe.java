package com.boco.miboy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(time);
        parcel.writeString(title);
        parcel.writeString(instruction);
        parcel.writeString(image);
        parcel.writeStringList(buy);
    }

    protected Recipe(Parcel in) {
        time = in.readInt();
        title = in.readString();
        instruction = in.readString();
        image = in.readString();
        buy = in.createStringArrayList();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}