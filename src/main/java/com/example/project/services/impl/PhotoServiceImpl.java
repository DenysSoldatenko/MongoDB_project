package com.example.project.services.impl;

import static org.bson.BsonBinarySubType.BINARY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.project.collections.Photo;
import com.example.project.repositories.PhotoRepository;
import com.example.project.services.PhotoService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service implementation for managing Photo objects.
 */
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  private final PhotoRepository photoRepository;

  @Override
  public String addPhoto(String originalFilename, MultipartFile image) throws IOException {
    Photo photo = new Photo();
    photo.setTitle(originalFilename);
    photo.setPhoto(new Binary(BINARY, image.getBytes()));
    return photoRepository.save(photo).getId();
  }

  @Override
  public Photo getPhoto(String id) {
    return photoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
  }
}
