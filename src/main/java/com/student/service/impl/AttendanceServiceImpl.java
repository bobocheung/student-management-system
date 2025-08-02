package com.student.service.impl;

import com.student.entity.Attendance;
import com.student.repository.AttendanceRepository;
import com.student.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Override
    public Attendance saveAttendance(Attendance attendance) {
        if (attendance.getId() == null) {
            attendance.setCreatedAt(LocalDateTime.now());
        }
        attendance.setUpdatedAt(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }
    
    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }
    
    @Override
    public List<Attendance> findAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    @Override
    public Page<Attendance> findAllAttendance(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }
    
    @Override
    public Page<Attendance> findByAdvancedSearch(String studentName, String courseName, LocalDate date, Pageable pageable) {
        if (studentName != null && !studentName.trim().isEmpty()) {
            return attendanceRepository.findByStudentNameContaining(studentName, pageable);
        } else if (courseName != null && !courseName.trim().isEmpty()) {
            return attendanceRepository.findByCourseNameContaining(courseName, pageable);
        } else if (date != null) {
            return attendanceRepository.findByDate(date, pageable);
        } else {
            return attendanceRepository.findAll(pageable);
        }
    }
    
    @Override
    public List<Attendance> findByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<Attendance> findByCourseId(Long courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }
    
    @Override
    public List<Attendance> findByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
    
    @Override
    public List<Attendance> findByStudentIdAndDate(Long studentId, LocalDate date) {
        return attendanceRepository.findByStudentIdAndDate(studentId, date);
    }
    
    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }
    
    @Override
    public long countAttendance() {
        return attendanceRepository.count();
    }
    
    @Override
    public long countByStatus(Attendance.AttendanceStatus status) {
        return attendanceRepository.countByStatus(status);
    }
    
    @Override
    public double getAttendanceRateByStudent(Long studentId) {
        List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
        if (attendances.isEmpty()) {
            return 0.0;
        }
        
        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();
        
        return (double) presentCount / attendances.size() * 100;
    }
    
    @Override
    public double getAttendanceRateByCourse(Long courseId) {
        List<Attendance> attendances = attendanceRepository.findByCourseId(courseId);
        if (attendances.isEmpty()) {
            return 0.0;
        }
        
        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();
        
        return (double) presentCount / attendances.size() * 100;
    }
} 