package com.security.securityImpl.controller;


import com.security.securityImpl.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/imageOrFile")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/save")
    public String saveOrUploadImage(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("description") String description) throws IOException {
        return imageService.saveImageToDb(multipartFile, name, description);
    }

}
