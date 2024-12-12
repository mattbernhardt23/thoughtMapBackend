package com.data.backend.repository;

import com.data.backend.model.ArgumentVote;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgumentVoteRepository extends MongoRepository<ArgumentVote, String> {
    // You can define custom query methods here if needed
    void deleteAllByArgumentIdIn(Iterable<String> argumentIds);

    // Delete all votes for a specific argument
    void deleteAllByArgumentId(String argumentId);

    Optional<ArgumentVote> findByUserIdAndArgumentId(String userId, String argumentId);

    long countByArgumentIdAndVoteType(String id, String string);

}
