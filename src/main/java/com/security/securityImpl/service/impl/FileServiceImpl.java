package com.security.securityImpl.service.impl;


import com.security.securityImpl.entity.FileData;
import com.security.securityImpl.repository.FileRepository;
import com.security.securityImpl.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;
    private final String FOLDER_PATH = "C:\\Users\\aditi\\Desktop\\JavaFileSave\\";


    public String saveImageToFileSystem(MultipartFile multipartFile, String name, String description, String type) throws IOException {

        String filePath = FOLDER_PATH + multipartFile.getOriginalFilename();
        FileData fileData = new FileData();
        fileData.setName(name);
        fileData.setDescription(description);
        fileData.setType(type);
        fileData.setFilePath(filePath);

        multipartFile.transferTo(new File(filePath));

        fileRepository.save(fileData);  // Saving to database

        return "File uploaded successfully: " + filePath;
    }

    public ResponseEntity<byte[]> downloadFilesFromSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileRepository.findByName(fileName.trim());

        if (fileData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String filePath = fileData.get().getFilePath();
        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        String contentType = Files.probeContentType(file.toPath());

        if (contentType == null) {
            if (fileName.endsWith(".mp4")) {
                contentType = "video/mp4";
            } else if (fileName.endsWith(".png")) {
                contentType = "image/png";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(fileBytes.length);

        return ResponseEntity.ok().headers(headers).body(fileBytes);
    }
}

//
//    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
//        Optional<FileData> fileData = fileRepository.findByName(fileName);
//
//        //get filepath
//        String filePath = fileData.get().getFilePath();
//        //converting to bytes
//        byte[] images = Files.readAllBytes(new File(filePath).toPath());
//        return images;
//
//    }
