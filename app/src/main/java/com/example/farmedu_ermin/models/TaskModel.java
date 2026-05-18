package com.example.farmedu_ermin.models;

import java.io.Serializable;

public class TaskModel implements Serializable {

    private String title;
    private String description;
    private String date;
    private String time;
    private String reminder;
    private String repeat;
    private String icon;

    public TaskModel(String title,
                     String description,
                     String date,
                     String time,
                     String reminder,
                     String repeat,
                     String icon) {

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.reminder = reminder;
        this.repeat = repeat;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getReminder() {
        return reminder;
    }

    public String getRepeat() {
        return repeat;
    }

    public String getIcon() {
        return icon;
    }
}