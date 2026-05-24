package com.example.farmedu_ermin.models;

public class NoteModel {

    private String noteId;
    private String title;
    private String content;

    private long timestamp;

    // PREMIUM FEATURES

    private boolean pinned;
    private boolean locked;
    private boolean checklist;

    // LOCK PIN

    private String pinCode;

    // EXTRA

    private String noteColor;
    private String imageUrl;
    private String voiceUrl;
    private String category;

    public NoteModel() {
    }

    public NoteModel(String noteId,
                     String title,
                     String content,
                     long timestamp,
                     boolean pinned,
                     boolean locked,
                     boolean checklist,
                     String pinCode,
                     String noteColor,
                     String imageUrl,
                     String voiceUrl,
                     String category) {

        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;

        this.pinned = pinned;
        this.locked = locked;
        this.checklist = checklist;

        this.pinCode = pinCode;

        this.noteColor = noteColor;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
        this.category = category;
    }

    // =========================================
    // NOTE ID
    // =========================================

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    // =========================================
    // TITLE
    // =========================================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // =========================================
    // CONTENT
    // =========================================

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // =========================================
    // TIMESTAMP
    // =========================================

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // =========================================
    // PINNED
    // =========================================

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    // =========================================
    // LOCKED
    // =========================================

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    // =========================================
    // CHECKLIST
    // =========================================

    public boolean isChecklist() {
        return checklist;
    }

    public void setChecklist(boolean checklist) {
        this.checklist = checklist;
    }

    // =========================================
    // PIN CODE
    // =========================================

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    // =========================================
    // NOTE COLOR
    // =========================================

    public String getNoteColor() {
        return noteColor;
    }

    public void setNoteColor(String noteColor) {
        this.noteColor = noteColor;
    }

    // =========================================
    // IMAGE URL
    // =========================================

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // =========================================
    // VOICE URL
    // =========================================

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    // =========================================
    // CATEGORY
    // =========================================

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}