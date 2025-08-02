package com.student.repository;

import com.student.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    List<Attendance> findByStudentId(Long studentId);
    
    List<Attendance> findByCourseId(Long courseId);
    
    List<Attendance> findByDate(LocalDate date);
    
    List<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.student.name LIKE %:studentName%")
    Page<Attendance> findByStudentNameContaining(@Param("studentName") String studentName, Pageable pageable);
    
    @Query("SELECT a FROM Attendance a WHERE a.course.name LIKE %:courseName%")
    Page<Attendance> findByCourseNameContaining(@Param("courseName") String courseName, Pageable pageable);
    
    @Query("SELECT a FROM Attendance a WHERE a.date = :date")
    Page<Attendance> findByDate(@Param("date") LocalDate date, Pageable pageable);
    
    long countByStatus(Attendance.AttendanceStatus status);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.status = 'PRESENT'")
    long countPresentByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.course.id = :courseId AND a.status = 'PRESENT'")
    long countPresentByCourseId(@Param("courseId") Long courseId);
} 