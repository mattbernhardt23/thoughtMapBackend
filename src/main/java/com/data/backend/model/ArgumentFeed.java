package com.data.backend.model;

import java.util.Date;

import com.data.backend.model.dto.Creator;

import lombok.Data;

@Data
public class ArgumentFeed {
    private String id;
    private Creator creator;
    private String title;
    private String description;
    private Date dateCreated;
    private long upVotes;
    private long downVotes;
    private Boolean supporting;

    public ArgumentFeed(String id, Creator creator, String title, String description, Date dateCreated, long upVotes,
            long downVotes, Boolean supporting) {

        this.id = id;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.supporting = supporting;
    }
}
