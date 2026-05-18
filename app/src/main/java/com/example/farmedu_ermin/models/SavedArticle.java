package com.example.farmedu_ermin.models;

public class SavedArticle {

    public String id;

    public String title;

    public String category;

    public String description;

    public String time;

    public String image;

    public SavedArticle() {
    }

    public SavedArticle(
            String id,
            String title,
            String category,
            String description,
            String time,
            String image
    ) {

        this.id = id;

        this.title = title;

        this.category = category;

        this.description = description;

        this.time = time;

        this.image = image;
    }
}