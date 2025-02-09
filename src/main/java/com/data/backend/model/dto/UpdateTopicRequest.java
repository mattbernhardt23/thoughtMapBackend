package com.data.backend.model.dto;

public class UpdateTopicRequest {
    private String topicId;
    private String userId;
    private String title;
    private String description;

    // Getters and Setters
    public String getTopic_id() {
        return topicId;
    }

    public void setTopic_id(String topic_id) {
        this.topicId = topic_id;
    }

    public String getUser_id() {
        return userId;
    }

    public void setUser_id(String user_id) {
        this.userId = user_id;
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
