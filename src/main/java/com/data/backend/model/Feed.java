package com.data.backend.model;

import java.util.Date;
import java.util.List;

import com.data.backend.model.dto.Creator;

import lombok.Data;

@Data
public class Feed {
    private String id;
    private Creator creator;
    private String title;
    private String description;
    private Date dateCreated;
    private long upVotes;
    private long downVotes;
    private List<ArgumentFeed> args;

    public Feed(String id, Creator creator, String title, String description, Date dateCreated, long upVotes,
            long downVotes, List<ArgumentFeed> arguments) {
        this.id = id;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.args = arguments;
    }
}
