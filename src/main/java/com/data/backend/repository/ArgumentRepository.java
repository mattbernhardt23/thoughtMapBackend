package com.data.backend.repository;

import com.data.backend.model.Argument;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgumentRepository extends MongoRepository<Argument, String> {
    // Retrive a list of user ids
    // Query to retrieve only the IDs (_id field)
    @Query(value = "{}", fields = "{_id: 1}")
    List<String> findAllIds();

    // Delete All By TopicId
    void deleteAllByTopicId(String topicId);

    // Find By TopicId
    List<Argument> findByTopicId(String topicId);
}
