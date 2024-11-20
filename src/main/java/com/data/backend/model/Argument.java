package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "arguments")
public class Argument {
    @Id
    private String id;
    private String creator_id;
    private String topic_id;
    private boolean supporting;
    private String title;
    private String description;
    private Date dateCreated = new Date();
}
