package com.student.service;

import com.student.entity.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClassService {
    
    // 基本CRUD操作
    Class saveClass(Class classEntity);
    
    Optional<Class> findById(Long id);
    
    Optional<Class> findByCode(String code);
    
    List<Class> findAllClasses();
    
    Page<Class> findAllClasses(Pageable pageable);
    
    void deleteClass(Long id);
    
    boolean existsByCode(String code);
    
    // 查詢操作
    List<Class> findByGrade(Integer grade);
    
    List<Class> findByStatus(Class.ClassStatus status);
    
    List<Class> findByTeacherId(Long teacherId);
    
    List<Class> findByKeyword(String keyword);
    
    // 分頁查詢
    Page<Class> findByNameContaining(String name, Pageable pageable);
    
    Page<Class> findByCodeContaining(String code, Pageable pageable);
    
    Page<Class> findByGrade(Integer grade, Pageable pageable);
    
    Page<Class> findByStatus(Class.ClassStatus status, Pageable pageable);
    
    Page<Class> findByTeacherId(Long teacherId, Pageable pageable);
    
    // 高級搜索
    Page<Class> findByAdvancedSearch(String name, String code, Integer grade, 
                                    Class.ClassStatus status, Pageable pageable);
    
    // 統計操作
    Long countByStatus(Class.ClassStatus status);
    
    Long countByGrade(Integer grade);
    
    Long countByTeacherId(Long teacherId);
    
    // 業務操作
    Class activateClass(Long classId);
    
    Class deactivateClass(Long classId);
    
    // 驗證操作
    boolean validateClassCode(String code);
    
    boolean validateClassName(String name);
} 