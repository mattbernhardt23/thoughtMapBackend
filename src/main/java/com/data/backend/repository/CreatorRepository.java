package com.data.backend.repository;

import com.data.backend.model.dto.Creator;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatorRepository extends MongoRepository<Creator, String> {

}
