package com.example.project.services;

import com.example.project.collections.Photo;
import com.example.project.repositories.PhotoRepository;
import java.io.IOException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service implementation for managing Photo objects.
 */
@Service
public class PhotoServiceImpl implements PhotoService {

  private final PhotoRepository photoRepository;

  @Autowired
  public PhotoServiceImpl(PhotoRepository photoRepository) {
    this.photoRepository = photoRepository;
  }

  @Override
  public String addPhoto(String originalFilename, MultipartFile image) throws IOException {
    Photo photo = new Photo();
    photo.setTitle(originalFilename);
    photo.setPhoto(new Binary(BsonBinarySubType.BINARY, image.getBytes()));
    return photoRepository.save(photo).getId();
  }

  @Override
  public Photo getPhoto(String id) {
    return photoRepository.findById(id)
    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
