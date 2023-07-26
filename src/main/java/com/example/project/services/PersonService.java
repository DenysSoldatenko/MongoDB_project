package com.example.project.services;

import com.example.project.collections.Person;
import java.util.List;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing Person objects.
 */
public interface PersonService {

  String save(Person person);

  List<Person> getPersonStartWith(String name);

  void delete(String id);

  List<Person> getByPersonAge(Integer minAge, Integer maxAge);

  Page<Person> search(String name, Integer minAge, Integer maxAge, String city, Pageable pageable);

  List<Document> getOldestPersonByCities();

  Document getOldestPersonByCity(String city);

  List<Document> getPopulationByCity();
}
