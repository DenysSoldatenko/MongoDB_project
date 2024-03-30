package com.example.project.controllers;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.example.project.collections.Person;
import com.example.project.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Person objects.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
@Tag(name = "Person Controller", description = "Endpoints for managing persons")
public class PersonController {

  private final PersonService personService;

  @Operation(summary = "Add a new person")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Person added successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @PostMapping
  @ResponseStatus(CREATED)
  public String addPerson(@RequestBody Person person) {
    return personService.save(person);
  }

  @Operation(summary = "Get persons whose name starts with the given string")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of persons retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Persons not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping
  @ResponseStatus(OK)
  public List<Person> getPersonStartWith(@RequestParam("name") String name) {
    return personService.getPersonStartWith(name);
  }

  @Operation(summary = "Remove a person by ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Person removed successfully"),
    @ApiResponse(responseCode = "404", description = "Person not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public void removePerson(@PathVariable String id) {
    personService.delete(id);
  }

  @Operation(summary = "Get persons by age range")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of persons retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Persons not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/age")
  @ResponseStatus(OK)
  public List<Person> getByPersonAge(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
    return personService.getByPersonAge(minAge, maxAge);
  }

  @Operation(summary = "Search for persons based on criteria")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of persons retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Persons not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/search")
  @ResponseStatus(OK)
  public Page<Person> searchPerson(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer minAge,
      @RequestParam(required = false) Integer maxAge,
      @RequestParam(required = false) String city,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    return personService.search(name, minAge, maxAge, city, pageable);
  }

  @Operation(summary = "Get the oldest person in each city")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List of oldest persons by city retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Persons not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/oldestPersonByCities")
  @ResponseStatus(OK)
  public List<Document> getOldestPersonByCities() {
    return personService.getOldestPersonByCities();
  }

  @Operation(summary = "Get the oldest person in a specific city")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Oldest person by city retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Person not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/oldestPersonByCity")
  @ResponseStatus(OK)
  public Document getOldestPersonByCity(@RequestParam("name") String name) {
    return personService.getOldestPersonByCity(name);
  }

  @Operation(summary = "Get the population count for each city")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Population count by city retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "Data not found", content = @Content),
    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
  })
  @GetMapping("/populationByCity")
  @ResponseStatus(OK)
  public List<Document> getPopulationByCity() {
    return personService.getPopulationByCity();
  }
}
