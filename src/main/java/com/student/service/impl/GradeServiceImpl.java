package com.student.service.impl;

import com.student.entity.Grade;
import com.student.repository.GradeRepository;
import com.student.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {
    
    @Autowired
    private GradeRepository gradeRepository;
    
    @Override
    public Grade saveGrade(Grade grade) {
        if (grade.getId() == null) {
            grade.setCreatedAt(LocalDateTime.now());
        }
        grade.setUpdatedAt(LocalDateTime.now());
        return gradeRepository.save(grade);
    }
    
    @Override
    public Optional<Grade> findById(Long id) {
        return gradeRepository.findById(id);
    }
    
    @Override
    public List<Grade> findAllGrades() {
        return gradeRepository.findAll();
    }
    
    @Override
    public Page<Grade> findAllGrades(Pageable pageable) {
        return gradeRepository.findAll(pageable);
    }
    
    @Override
    public Page<Grade> findByAdvancedSearch(String studentName, String courseName, String semester, Pageable pageable) {
        if (studentName != null && !studentName.trim().isEmpty()) {
            return gradeRepository.findByStudentNameContaining(studentName, pageable);
        } else if (courseName != null && !courseName.trim().isEmpty()) {
            return gradeRepository.findByCourseNameContaining(courseName, pageable);
        } else if (semester != null && !semester.trim().isEmpty()) {
            return gradeRepository.findBySemester(semester, pageable);
        } else {
            return gradeRepository.findAll(pageable);
        }
    }
    
    @Override
    public List<Grade> findByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<Grade> findByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }
    
    @Override
    public List<Grade> findBySemester(String semester) {
        return gradeRepository.findBySemester(semester);
    }
    
    @Override
    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
    
    @Override
    public long countGrades() {
        return gradeRepository.count();
    }
    
    @Override
    public double getAverageGradeByStudent(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if (grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream()
                .mapToDouble(Grade::getTotalScore)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public double getAverageGradeByCourse(Long courseId) {
        List<Grade> grades = gradeRepository.findByCourseId(courseId);
        if (grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream()
                .mapToDouble(Grade::getTotalScore)
                .average()
                .orElse(0.0);
    }
    
    @Override
    public boolean existsByStudentAndCourse(Long studentId, Long courseId) {
        return gradeRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
} 