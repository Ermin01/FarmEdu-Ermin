package com.example.farmedu_ermin.chat;

import java.util.ArrayList;
import java.util.List;

public class PostModel {

    private String postId;

    private String name;

    private String time;

    private String description;

    private int avatar;

    private int likes;

    private int comments;

    private boolean liked;

    // =====================================
    // IMAGE URI / URL
    // =====================================

    private String imageUri;

    private List<String> likedUsers;

    // =====================================
    // CONSTRUCTORS
    // =====================================

    public PostModel() {

        likedUsers = new ArrayList<>();
    }

    public PostModel(
            String name,
            String time,
            String description,
            int avatar
    ) {

        this.name = name;

        this.time = time;

        this.description = description;

        this.avatar = avatar;

        this.likes = 0;

        this.comments = 0;

        this.liked = false;

        this.imageUri = "";

        this.likedUsers = new ArrayList<>();
    }

    // =====================================
    // GETTERS
    // =====================================

    public String getPostId() {
        return postId;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getAvatar() {
        return avatar;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getImageUri() {
        return imageUri;
    }

    // DODANO
    public String getImageUrl() {
        return imageUri;
    }

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    // =====================================
    // SETTERS
    // =====================================

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // DODANO
    public void setImageUrl(String imageUrl) {
        this.imageUri = imageUrl;
    }

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers = likedUsers;
    }
}