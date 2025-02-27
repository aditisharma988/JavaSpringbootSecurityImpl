package com.security.securityImpl.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String saveImageToFileSystem(MultipartFile multipartFile, String name, String description, String type) throws IOException;

    byte[] downloadImageFromFileSystem(String fileName) throws IOException;
}
