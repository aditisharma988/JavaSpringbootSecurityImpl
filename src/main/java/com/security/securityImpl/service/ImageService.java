package com.security.securityImpl.service;

import com.security.securityImpl.entity.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ImageService {

   String  saveImageToDb(MultipartFile multipartFile, String name, String description) throws IOException;

}
