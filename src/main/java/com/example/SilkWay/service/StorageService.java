package com.example.SilkWay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class StorageService {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    public static final Path rootLocation = Paths.get("upload-dir");

    public void store(MultipartFile file){
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            log.error("FAIL! Such file already exist", e);
        }
    }

    public Resource loadFile(String filename) {
        try {
            System.out.println(filename);
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                log.error("FAIL! could not find file");
                return resource;
            }
        } catch (MalformedURLException e) {
            log.error("Error while loading file", e);
        }
        return null;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage!", e);
        }
    }
}