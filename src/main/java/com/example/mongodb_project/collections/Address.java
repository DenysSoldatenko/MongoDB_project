package com.example.mongodb_project.collections;

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
public class Address {

  private String address1;
  private String address2;
  private String city;
}
