package com.example.project.collections;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A class representing a Photo object.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "photo")
@JsonInclude(NON_NULL)
public class Photo {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(
      description = "Title of the photo",
      example = "Vacation Photo"
  )
  private String title;

  @Schema(
      description = "Binary data of the photo"
  )
  private Binary photo;
}
