package com.data.backend.repository;

import com.data.backend.model.ArgumentVote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgumentVoteRepository extends MongoRepository<ArgumentVote, String> {
    // You can define custom query methods here if needed
}
