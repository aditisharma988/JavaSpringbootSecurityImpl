package com.security.securityImpl.service;

import com.security.securityImpl.entity.FileData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    String saveImageToFileSystem(MultipartFile multipartFile, String name, String description, String type) throws IOException;

    ResponseEntity<byte[]> downloadFilesFromSystem(String fileName) throws IOException;

    ResponseEntity<List<FileData>> getAllFiles();
}
