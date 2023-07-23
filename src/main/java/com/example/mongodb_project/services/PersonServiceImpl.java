package com.example.mongodb_project.services;

import com.example.mongodb_project.collections.Person;
import com.example.mongodb_project.repositories.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing Person objects.
 */
@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  private final MongoTemplate mongoTemplate;

  @Autowired
  public PersonServiceImpl(PersonRepository personRepository, MongoTemplate mongoTemplate) {
    this.personRepository = personRepository;
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public String save(Person person) {
    return personRepository.save(person).getPersonId();
  }

  @Override
  public List<Person> getPersonStartWith(String name) {
    return personRepository.findByFirstNameStartsWith(name);
  }

  @Override
  public void delete(String id) {
    personRepository.deleteById(id);
  }

  @Override
  public List<Person> getByPersonAge(Integer minAge, Integer maxAge) {
    return personRepository.findPersonByAgeBetween(minAge, maxAge);
  }

  @Override
  public Page<Person> search(String name, Integer minAge, Integer maxAge,
                             String city, Pageable pageable) {

    Query query = new Query().with(pageable);
    List<Criteria> criteria = new ArrayList<>();

    if (name != null && !name.isEmpty()) {
      criteria.add(Criteria.where("firstName").regex(name, "i"));
    }

    if (minAge != null && maxAge != null) {
      criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
    }

    if (city != null && !city.isEmpty()) {
      criteria.add(Criteria.where("addresses.city").is(city));
    }

    if (!criteria.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
    }

    return PageableExecutionUtils.getPage(
      mongoTemplate.find(query, Person.class),
      pageable,
      () -> mongoTemplate.count(query.skip(0).limit(0), Person.class));
  }
}
