package com.example.project.services.impl;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.ROOT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.example.project.collections.Person;
import com.example.project.repositories.PersonRepository;
import com.example.project.services.PersonService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
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
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepository;

  private final MongoTemplate mongoTemplate;

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

    List<Criteria> criteria = new ArrayList<>();

    if (name != null && !name.isEmpty()) {
      criteria.add(where("firstName").regex(name, "i"));
    }

    if (minAge != null && maxAge != null) {
      criteria.add(where("age").gte(minAge).lte(maxAge));
    }

    if (city != null && !city.isEmpty()) {
      criteria.add(where("addresses.city").is(city));
    }

    Query query = new Query().with(pageable);

    if (!criteria.isEmpty()) {
      query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
    }

    return PageableExecutionUtils.getPage(
      mongoTemplate.find(query, Person.class),
      pageable,
      () -> mongoTemplate.count(query.skip(0).limit(0), Person.class)
    );
  }

  @Override
  public List<Document> getOldestPersonByCities() {
    UnwindOperation unwindOperation = unwind("addresses");
    SortOperation sortOperation = sort(DESC, "age");
    GroupOperation groupOperation = group("addresses.city").first(ROOT).as("oldestPerson");
    Aggregation aggregation = newAggregation(unwindOperation, sortOperation, groupOperation);
    return mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
  }

  @Override
  public Document getOldestPersonByCity(String cityName) {
    UnwindOperation unwindOperation = unwind("addresses");
    MatchOperation matchOperation = match(where("addresses.city").is(cityName));
    SortOperation sortOperation = sort(DESC, "age");
    GroupOperation groupOperation = group("addresses.city").first(ROOT).as("oldestPerson");

    Aggregation aggregation = newAggregation(
        unwindOperation, matchOperation,
        sortOperation, groupOperation
    );

    return mongoTemplate.aggregate(aggregation, Person.class, Document.class)
        .getMappedResults()
        .get(0);
  }

  @Override
  public List<Document> getPopulationByCity() {
    UnwindOperation unwindOperation = unwind("addresses");
    GroupOperation groupOperation = group("addresses.city").count().as("popCount");
    SortOperation sortOperation = sort(DESC, "popCount");

    ProjectionOperation projectionOperation = project()
        .andExpression("_id")
        .as("city")
        .andExpression("popCount")
        .as("count")
        .andExclude("_id");

    Aggregation aggregation = newAggregation(
        unwindOperation, groupOperation,
        sortOperation, projectionOperation
    );

    return mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
  }
}
