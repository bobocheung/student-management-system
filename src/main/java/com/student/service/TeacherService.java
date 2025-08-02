package com.student.service;

import com.student.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    
    /**
     * 保存教師信息
     */
    Teacher saveTeacher(Teacher teacher);
    
    /**
     * 根據ID查找教師
     */
    Optional<Teacher> findById(Long id);
    
    /**
     * 查找所有教師
     */
    List<Teacher> findAllTeachers();
    
    /**
     * 分頁查找所有教師
     */
    Page<Teacher> findAllTeachers(Pageable pageable);
    
    /**
     * 高級搜索教師
     */
    Page<Teacher> findByAdvancedSearch(String name, String teacherNumber, String department, Pageable pageable);
    
    /**
     * 刪除教師
     */
    void deleteTeacher(Long id);
    
    /**
     * 上傳教師照片
     */
    void uploadPhoto(Long teacherId, MultipartFile photo);
    
    /**
     * 刪除教師照片
     */
    void deletePhoto(Long teacherId);
    
    /**
     * 根據教師編號查找教師
     */
    Optional<Teacher> findByTeacherNumber(String teacherNumber);
    
    /**
     * 根據部門查找教師
     */
    List<Teacher> findByDepartment(String department);
    
    /**
     * 統計教師總數
     */
    long countTeachers();
    
    /**
     * 根據部門統計教師數量
     */
    long countByDepartment(String department);
    
    /**
     * 查找所有在職教師
     */
    List<Teacher> findAllActiveTeachers();
} 