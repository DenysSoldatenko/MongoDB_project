package com.example.project.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

import com.example.project.collections.Photo;
import com.example.project.services.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing Photo objects.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
@Tag(name = "Photo Controller", description = "Endpoints for managing photos")
public class PhotoController {

  private final PhotoService photoService;

  @PostMapping
  @Operation(
      summary = "Upload a new photo",
      description = "Uploads a photo and saves it with the given title.",
      responses = {
        @ApiResponse(responseCode = "201", description = "Photo successfully uploaded"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or empty file"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  @ResponseStatus(CREATED)
  public String addPhoto(@RequestParam("image") MultipartFile image) throws IOException {
    return photoService.addPhoto(image.getOriginalFilename(), image);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Download a photo by ID",
      description = "Retrieves a photo by its ID and returns it as a downloadable file.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Photo successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Photo not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  public ResponseEntity<Resource> downloadPhoto(@PathVariable String id) {
    Photo photo = photoService.getPhoto(id);
    Resource resource = new ByteArrayResource(photo.getPhoto().getData());

    return ResponseEntity.ok()
       .header(CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getTitle() + "\"")
       .contentType(APPLICATION_OCTET_STREAM)
       .body(resource);
  }
}
