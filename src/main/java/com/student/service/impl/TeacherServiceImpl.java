package com.student.service.impl;

import com.student.entity.Teacher;
import com.student.repository.TeacherRepository;
import com.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherServiceImpl implements TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Override
    public Teacher saveTeacher(Teacher teacher) {
        if (teacher.getId() == null) {
            teacher.setCreatedAt(LocalDateTime.now());
        }
        teacher.setUpdatedAt(LocalDateTime.now());
        return teacherRepository.save(teacher);
    }
    
    @Override
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }
    
    @Override
    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }
    
    @Override
    public Page<Teacher> findAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }
    
    @Override
    public Page<Teacher> findByAdvancedSearch(String name, String teacherNumber, String department, Pageable pageable) {
        if (name != null && !name.trim().isEmpty()) {
            return teacherRepository.findByNameContaining(name, pageable);
        } else if (teacherNumber != null && !teacherNumber.trim().isEmpty()) {
            return teacherRepository.findByTeacherNumberContaining(teacherNumber, pageable);
        } else {
            return teacherRepository.findAll(pageable);
        }
    }
    
    @Override
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
    
    @Override
    public void uploadPhoto(Long teacherId, MultipartFile photo) {
        try {
            Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
            if (teacherOpt.isPresent()) {
                Teacher teacher = teacherOpt.get();
                
                // 創建上傳目錄
                String uploadDir = "uploads/teachers/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // 生成文件名
                String originalFilename = photo.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + fileExtension;
                
                // 保存文件
                Path filePath = uploadPath.resolve(filename);
                Files.copy(photo.getInputStream(), filePath);
                
                // 更新教師照片路徑
                teacher.setPhotoPath("/uploads/teachers/" + filename);
                teacherRepository.save(teacher);
            }
        } catch (IOException e) {
            throw new RuntimeException("照片上傳失敗", e);
        }
    }
    
    @Override
    public void deletePhoto(Long teacherId) {
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            teacher.setPhotoPath(null);
            teacherRepository.save(teacher);
        }
    }
    
    @Override
    public Optional<Teacher> findByTeacherNumber(String teacherNumber) {
        return teacherRepository.findByTeacherNumber(teacherNumber);
    }
    
    @Override
    public List<Teacher> findByDepartment(String department) {
        // 由於Teacher實體沒有department字段，返回空列表
        return List.of();
    }
    
    @Override
    public long countTeachers() {
        return teacherRepository.count();
    }
    
    @Override
    public long countByDepartment(String department) {
        // 由於Teacher實體沒有department字段，返回0
        return 0;
    }
    
    @Override
    public List<Teacher> findAllActiveTeachers() {
        return teacherRepository.findByStatus(Teacher.TeacherStatus.ACTIVE);
    }
} 