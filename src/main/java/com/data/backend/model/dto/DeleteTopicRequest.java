package com.data.backend.model.dto;

public class DeleteTopicRequest {
    private String topicId;
    private String userId;

    // Getters and Setters
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
