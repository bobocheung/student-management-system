package com.student.service;

import com.student.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    
    Attendance saveAttendance(Attendance attendance);
    
    Optional<Attendance> findById(Long id);
    
    List<Attendance> findAllAttendance();
    
    Page<Attendance> findAllAttendance(Pageable pageable);
    
    Page<Attendance> findByAdvancedSearch(String studentName, String courseName, LocalDate date, Pageable pageable);
    
    List<Attendance> findByStudentId(Long studentId);
    
    List<Attendance> findByCourseId(Long courseId);
    
    List<Attendance> findByDate(LocalDate date);
    
    List<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date);
    
    void deleteAttendance(Long id);
    
    long countAttendance();
    
    long countByStatus(Attendance.AttendanceStatus status);
    
    double getAttendanceRateByStudent(Long studentId);
    
    double getAttendanceRateByCourse(Long courseId);
} 