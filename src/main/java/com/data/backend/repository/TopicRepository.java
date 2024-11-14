package com.data.backend.repository;

import com.data.backend.model.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends MongoRepository<Topic, String> {
    // You can define custom query methods here if needed

}
