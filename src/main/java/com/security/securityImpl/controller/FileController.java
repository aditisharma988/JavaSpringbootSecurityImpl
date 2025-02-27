package com.security.securityImpl.controller;

import com.security.securityImpl.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/imageOrFile")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/saveToFileSystem")
    public String saveOrUploadImageToFileSystem(@RequestParam("file") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("type") String type) throws IOException {
        return fileService.saveImageToFileSystem(multipartFile, name, description, type);
    }

    @GetMapping("downloadToFiles/{fileName}")
    public ResponseEntity<byte[]> downloadImageToFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData = fileService.downloadFilesFromSystem(fileName).getBody();

//        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.IMAGE_PNG);
//        headers.setContentType(MediaType.valueOf("video/mp4"));
//        headers.setContentLength(imageData.length);

        return new ResponseEntity<>(imageData,HttpStatus.OK);
    }

}