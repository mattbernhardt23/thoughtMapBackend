package com.data.backend.repository;

import com.data.backend.model.Topic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {

    Optional<Topic> findByTitle(String topicTitle);

    // Retrive a list of user ids
    // Query to retrieve only the IDs (_id field)
    @Query(value = "{}", fields = "{_id: 1}")
    List<String> findAllIds();
}
