package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "arguments")
public class Argument {
    @Id
    private String id;
    private User creator;
    private boolean supporting;
    private String title;
    private int upVotes = 0;
    private int downVotes = 0;
    private String description;
    private List<Argument> supports;
    private List<Argument> objections;
    private Date dateCreated = new Date();
}
