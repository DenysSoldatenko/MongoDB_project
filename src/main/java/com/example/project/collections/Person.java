package com.example.project.collections;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A class representing a Person object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "person")
@JsonInclude(NON_NULL)
@Schema(description = "Person object containing personal details")
public class Person {

  @Id
  @Schema(hidden = true)
  private String personId;

  @Schema(
      description = "First name of the person",
      example = "John"
  )
  private String firstName;

  @Schema(
      description = "Last name of the person",
      example = "Doe"
  )
  private String lastName;

  @Schema(
      description = "Age of the person",
      example = "30"
  )
  private Integer age;

  @Schema(
      description = "List of hobbies the person enjoys",
      example = "[\"Reading\", \"Traveling\", \"Cooking\"]"
  )
  private List<String> hobbies;

  @Schema(
      description = "List of addresses associated with the person",
      example = "[{\"address1\": \"123 Main St\", \"address2\": \"Apt 4B\", \"city\": \"New York\"}]"
  )
  private List<Address> addresses;
}
