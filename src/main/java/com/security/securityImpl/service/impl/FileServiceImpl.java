package com.security.securityImpl.service.impl;


import com.security.securityImpl.entity.FileData;
import com.security.securityImpl.repository.FileRepository;
import com.security.securityImpl.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
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


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileRepository.findByName(fileName);

        //get filepath
        String filePath = fileData.get().getFilePath();
        //converting to bytes
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;

    }
}
