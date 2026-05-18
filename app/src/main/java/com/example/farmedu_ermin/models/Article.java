package com.example.farmedu_ermin.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Article implements Serializable {

    private String title;
    private String description;
    private String time;
    private String category;
    private String image;

    private ArrayList<String> tags;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}