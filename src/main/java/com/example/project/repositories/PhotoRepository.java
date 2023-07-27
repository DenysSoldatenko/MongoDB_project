package com.example.project.repositories;

import com.example.project.collections.Photo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Photo objects in MongoDB.
 */
@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> { }
