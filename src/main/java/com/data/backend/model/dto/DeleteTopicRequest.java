package com.data.backend.model.dto;

public class DeleteTopicRequest {
    private String topic_id;
    private String user_id;

    // Getters and Setters
    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
