package com.data.backend.repository;

import com.data.backend.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    // You can define custom query methods here if needed
}
