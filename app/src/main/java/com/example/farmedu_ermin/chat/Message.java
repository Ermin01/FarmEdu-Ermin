package com.example.farmedu_ermin.chat;

public class Message {

    private String senderId;

    private String receiverId;

    private String message;

    private long timestamp;

    public Message() {
    }

    public Message(
            String senderId,
            String receiverId,
            String message,
            long timestamp
    ) {

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // =====================================
    // GETTERS
    // =====================================

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // =====================================
    // SETTERS
    // =====================================

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}