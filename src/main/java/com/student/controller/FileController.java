package com.student.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/uploads")
public class FileController {
    
    @GetMapping("/teachers/{filename:.+}")
    public ResponseEntity<Resource> serveTeacherPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/teachers/" + filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            }
        } catch (IOException e) {
            // 記錄錯誤但不拋出異常
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/students/{filename:.+}")
    public ResponseEntity<Resource> serveStudentPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/students/" + filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            }
        } catch (IOException e) {
            // 記錄錯誤但不拋出異常
        }
        
        return ResponseEntity.notFound().build();
    }
} 