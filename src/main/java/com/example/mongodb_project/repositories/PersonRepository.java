package com.example.mongodb_project.repositories;

import com.example.mongodb_project.collections.Person;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Person objects in MongoDB.
 */
@Repository
public interface PersonRepository extends MongoRepository<Person, String> {

  List<Person> findByFirstNameStartsWith(String name);

  //Default method
  //List<Person> findByAgeBetween(Integer min, Integer max);

  //Custom method
  @Query(value = "{ 'age' : { $gt : ?0, $lt : ?1}}", fields = "{addresses:  0}")
  List<Person> findPersonByAgeBetween(Integer min, Integer max);
}
