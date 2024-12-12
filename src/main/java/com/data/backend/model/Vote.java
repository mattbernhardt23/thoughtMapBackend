package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "votes")
@CompoundIndex(name = "user_topic_idx", def = "{'userId': 1, 'topicId': 1}", unique = true)
public class Vote {
    @Id
    private String id;
    private String userId; // Reference to the user who voted
    private String topicId; // Reference to the topic being voted on
    private String voteType; // Either "up" or "down"
    private Date votedAt = new Date();
}
