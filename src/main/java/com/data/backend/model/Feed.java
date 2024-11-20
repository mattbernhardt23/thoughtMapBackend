package com.data.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class Feed {
    private Creator creator;
    private int upVotes;
    private int downVotes;
    private String title;
    private String description;
    private List<ArgumentFeed> args;

    @Data
    public static class ArgumentFeed {
        private Creator creator;
        private int upVotes;
        private int downVotes;
        private boolean supporting;
        private String title;
        private String description;
    }
}
