package com.example.project.collections;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A class representing an Address object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Address object containing detailed information about a location")
public class Address {

  @Schema(
      description = "The first line of the address, typically including street number and name",
      example = "123 Main St"
  )
  private String address1;

  @Schema(
      description = "The second line of the address, often used for apartment or unit number",
      example = "Apt 4B"
  )
  private String address2;

  @Schema(
      description = "The city where the address is located",
      example = "New York"
  )
  private String city;
}
