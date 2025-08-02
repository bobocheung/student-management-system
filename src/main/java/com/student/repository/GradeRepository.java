package com.student.repository;

import com.student.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    List<Grade> findByStudentId(Long studentId);
    
    List<Grade> findByCourseId(Long courseId);
    
    List<Grade> findBySemester(String semester);
    
    @Query("SELECT g FROM Grade g WHERE g.student.name LIKE %:studentName%")
    Page<Grade> findByStudentNameContaining(@Param("studentName") String studentName, Pageable pageable);
    
    @Query("SELECT g FROM Grade g WHERE g.course.name LIKE %:courseName%")
    Page<Grade> findByCourseNameContaining(@Param("courseName") String courseName, Pageable pageable);
    
    @Query("SELECT g FROM Grade g WHERE g.semester = :semester")
    Page<Grade> findBySemester(@Param("semester") String semester, Pageable pageable);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    @Query("SELECT AVG(g.totalScore) FROM Grade g WHERE g.student.id = :studentId")
    Double getAverageScoreByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT AVG(g.totalScore) FROM Grade g WHERE g.course.id = :courseId")
    Double getAverageScoreByCourseId(@Param("courseId") Long courseId);
} 