package com.student.service;

import com.student.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    
    Grade saveGrade(Grade grade);
    
    Optional<Grade> findById(Long id);
    
    List<Grade> findAllGrades();
    
    Page<Grade> findAllGrades(Pageable pageable);
    
    Page<Grade> findByAdvancedSearch(String studentName, String courseName, String semester, Pageable pageable);
    
    List<Grade> findByStudentId(Long studentId);
    
    List<Grade> findByCourseId(Long courseId);
    
    List<Grade> findBySemester(String semester);
    
    void deleteGrade(Long id);
    
    long countGrades();
    
    double getAverageGradeByStudent(Long studentId);
    
    double getAverageGradeByCourse(Long courseId);
    
    boolean existsByStudentAndCourse(Long studentId, Long courseId);
} 