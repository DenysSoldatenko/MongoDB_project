package com.example.project.controllers;

import com.example.project.collections.Photo;
import com.example.project.services.PhotoService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing Photo objects.
 */
@RestController
@RequestMapping("/photo")
public class PhotoController {

  private final PhotoService photoService;

  @Autowired
  public PhotoController(PhotoService photoService) {
    this.photoService = photoService;
  }

  @PostMapping
  public String addPhoto(@RequestParam("image") MultipartFile image) throws IOException {
    return photoService.addPhoto(image.getOriginalFilename(), image);
  }

  /**
   * Endpoint to download a photo by its ID.
   *
   * @param id the ID of the photo to download
   * @return a ResponseEntity containing the photo as a Resource to download
   */
  @GetMapping("/{id}")
  public ResponseEntity<Resource> downloadPhoto(@PathVariable String id) {
    Photo photo = photoService.getPhoto(id);
    Resource resource = new ByteArrayResource(photo.getPhoto().getData());

    return ResponseEntity.ok()
       .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getTitle() + "\"")
       .contentType(MediaType.APPLICATION_OCTET_STREAM)
       .body(resource);
  }
}
