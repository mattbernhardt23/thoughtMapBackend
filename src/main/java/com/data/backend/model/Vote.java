package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "votes")
@CompoundIndex(name = "user_topic_idx", def = "{'user_Id': 1, 'topic_Id': 1}", unique = true)
public class Vote {
    @Id
    private String id;
    private String user_Id; // Reference to the user who voted
    private String topic_Id; // Reference to the topic being voted on
    private String voteType; // Either "up" or "down"
    private Date votedAt = new Date();
}
