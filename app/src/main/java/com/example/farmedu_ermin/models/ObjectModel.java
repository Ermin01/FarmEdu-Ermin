package com.example.farmedu_ermin.models;

public class ObjectModel {
    String title;
    String desc;   // 🔥 NOVO
    int image;

    public ObjectModel(String title, String desc, int image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {   // 🔥 NOVO
        return desc;
    }

    public int getImage() {
        return image;
    }
}