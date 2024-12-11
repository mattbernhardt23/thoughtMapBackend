package com.data.backend.model.dto;

public class DeleteArgumentRequest {
    private String argumentId;
    private String userId;

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
}
