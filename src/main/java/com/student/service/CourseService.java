package com.student.service;

import com.student.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    
    // 基本CRUD操作
    Course saveCourse(Course course);
    
    Optional<Course> findById(Long id);
    
    Optional<Course> findByCode(String code);
    
    List<Course> findAllCourses();
    
    Page<Course> findAllCourses(Pageable pageable);
    
    void deleteCourse(Long id);
    
    boolean existsByCode(String code);
    
    // 查詢操作
    List<Course> findByStatus(Course.CourseStatus status);
    
    List<Course> findByTeacherId(Long teacherId);
    
    List<Course> findByKeyword(String keyword);
    
    // 分頁查詢
    Page<Course> findByNameContaining(String name, Pageable pageable);
    
    Page<Course> findByCodeContaining(String code, Pageable pageable);
    
    Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable);
    
    Page<Course> findByTeacherId(Long teacherId, Pageable pageable);
    
    // 高級搜索
    Page<Course> findByAdvancedSearch(String name, String code, 
                                     Course.CourseStatus status, Pageable pageable);
    
    // 統計操作
    Long countByStatus(Course.CourseStatus status);
    
    Long countByTeacherId(Long teacherId);
    
    // 業務操作
    Course activateCourse(Long courseId);
    
    Course deactivateCourse(Long courseId);
    
    // 驗證操作
    boolean validateCourseCode(String code);
    
    boolean validateCourseName(String name);
} 