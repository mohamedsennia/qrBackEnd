package com.example.demo.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
public class FileController {
    private FileService fileService;
    @Autowired
    public FileController(FileService fileService){
        this.fileService=fileService;
    }
    @PostMapping
        public ResponseEntity<Image> uploadFile(MultipartFile multipartFile){
        return ResponseEntity.ok(new Image(this.fileService.dealWithFile(multipartFile)));
    }
}
