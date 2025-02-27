package com.security.securityImpl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String saveImageToFileSystem(MultipartFile multipartFile, String name, String description, String type) throws IOException;

    ResponseEntity<byte[]> downloadFilesFromSystem(String fileName) throws IOException;
}
