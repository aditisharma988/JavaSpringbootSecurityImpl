package com.security.securityImpl.service.impl;

import com.security.securityImpl.entity.Image;
import com.security.securityImpl.repository.ImageRepository;
import com.security.securityImpl.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    public String saveImageToDb(MultipartFile multipartFile, String name, String description) throws IOException {

        Image image = new Image();
        image.setImage(multipartFile.getBytes());
        image.setName(name);
        image.setDescription(description);

        imageRepository.save(image);

        return "image saved successfully";


    }


}
