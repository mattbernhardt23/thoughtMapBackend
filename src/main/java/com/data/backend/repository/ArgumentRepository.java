package com.data.backend.repository;

import com.data.backend.model.Argument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgumentRepository extends MongoRepository<Argument, String> {
    // You can define custom query methods here if needed
}
