package com.data.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "argumentVotes")
@CompoundIndex(name = "user_topic_idx", def = "{'user_Id': 1, 'topic_Id': 1}", unique = true)
public class ArgumentVote {
    @Id
    private String id;
    private String user_id; // Reference to the user who voted
    private String argument_id; // Reference to the argument being voted on
    private String voteType; // Either "up" or "down"
    private Date votedAt = new Date();
}
