package com.data.backend.repository;

import com.data.backend.model.Vote;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    Optional<Vote> findByUserIdAndTopicId(String userId, String topicId);

    void deleteAllByTopicId(String topicId);

    long countByTopicIdAndVoteType(String id, String string);
}
