package com.example.farmedu_ermin.chat;

public class ChatUser {

    private String uid;
    private String name;
    private int avatar;

    // PRAZAN KONSTRUKTOR
    public ChatUser() {
    }

    // KONSTRUKTOR
    public ChatUser(
            String uid,
            String name,
            int avatar
    ) {

        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
    }

    // GETTERS

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getAvatar() {
        return avatar;
    }

    // SETTERS

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}