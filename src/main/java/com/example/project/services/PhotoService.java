package com.example.project.services;

import com.example.project.collections.Photo;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing Photo objects.
 */
public interface PhotoService {

  String addPhoto(String originalFilename, MultipartFile image) throws IOException;

  Photo getPhoto(String id);
}
