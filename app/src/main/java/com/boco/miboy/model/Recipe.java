package com.boco.miboy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private int time;
    private String title;
    private String instructions;
    private String image;
    private String video;
    private List<Ingredient> ingredients;
    private List<String> buy;

    public Recipe(int time, String title, String instructions, String image, List<Ingredient> ingredients, List<String> buy) {
        this.time = time;
        this.title = title;
        this.instructions = instructions;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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
                ", \"instructions\"='" + instructions + '\'' +
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
        parcel.writeString(instructions);
        parcel.writeString(image);
        parcel.writeString(video);
        parcel.writeStringList(buy);
        parcel.writeTypedList(ingredients);
    }

    protected Recipe(Parcel in) {
        time = in.readInt();
        title = in.readString();
        instructions = in.readString();
        image = in.readString();
        video = in.readString();
        buy = in.createStringArrayList();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, Ingredient.CREATOR);
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