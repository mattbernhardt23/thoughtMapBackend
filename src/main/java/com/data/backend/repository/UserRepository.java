package com.data.backend.repository;

import com.data.backend.model.User;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Retrive a list of user ids
    // Query to retrieve only the IDs (_id field)
    @Query(value = "{}", fields = "{_id: 1}")
    List<String> findAllIds();
}
