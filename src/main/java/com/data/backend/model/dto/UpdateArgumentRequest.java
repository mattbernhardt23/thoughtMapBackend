package com.data.backend.model.dto;

public class UpdateArgumentRequest {
    private String argumentId;
    private String userId;
    private String title;
    private String description;

    // Getters and Setters
    public String getArgumentId() {
        return argumentId;
    }

    public void setArgumentId(String argumentId) {
        this.argumentId = argumentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
