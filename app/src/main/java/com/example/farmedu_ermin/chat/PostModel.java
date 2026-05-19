package com.example.farmedu_ermin.chat;

import java.util.ArrayList;
import java.util.List;

public class PostModel {

    // =====================================
    // BASIC
    // =====================================

    private String userId;

    private String postId;

    private String name;

    private String time;

    private String description;

    private int avatar;

    // =====================================
    // STATS
    // =====================================

    private int likes;

    private int comments;

    private boolean liked;

    // =====================================
    // SINGLE IMAGE (OLD SUPPORT)
    // =====================================

    private String imageUri;

    // =====================================
    // MULTIPLE IMAGES
    // =====================================

    private List<String> imageUrls;

    // =====================================
    // LIKES
    // =====================================

    private List<String> likedUsers;

    // =====================================
    // EMPTY CONSTRUCTOR
    // =====================================

    public PostModel() {

        this.userId = "";

        this.postId = "";

        this.name = "";

        this.time = "";

        this.description = "";

        this.avatar = 0;

        this.likes = 0;

        this.comments = 0;

        this.liked = false;

        this.imageUri = "";

        this.imageUrls = new ArrayList<>();

        this.likedUsers = new ArrayList<>();
    }

    // =====================================
    // CONSTRUCTOR
    // =====================================

    public PostModel(
            String userId,
            String postId,
            String name,
            String time,
            String description,
            int avatar
    ) {

        this.userId = userId;

        this.postId = postId;

        this.name = name;

        this.time = time;

        this.description = description;

        this.avatar = avatar;

        this.likes = 0;

        this.comments = 0;

        this.liked = false;

        this.imageUri = "";

        this.imageUrls = new ArrayList<>();

        this.likedUsers = new ArrayList<>();
    }

    // =====================================
    // GETTERS
    // =====================================

    public String getUserId() {
        return userId;
    }

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

    // =====================================
    // OLD IMAGE
    // =====================================

    public String getImageUri() {
        return imageUri;
    }

    public String getImageUrl() {
        return imageUri;
    }

    // =====================================
    // MULTIPLE IMAGES
    // =====================================

    public List<String> getImageUrls() {
        return imageUrls;
    }

    // =====================================
    // LIKES
    // =====================================

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    // =====================================
    // SETTERS
    // =====================================

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    // =====================================
    // OLD IMAGE
    // =====================================

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUri = imageUrl;
    }

    // =====================================
    // MULTIPLE IMAGES
    // =====================================

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    // =====================================
    // LIKES
    // =====================================

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers = likedUsers;
    }
}