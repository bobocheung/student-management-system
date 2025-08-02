package com.student.service.impl;

import com.student.entity.Course;
import com.student.repository.CourseRepository;
import com.student.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }
    
    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
    
    @Override
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCourseCode(code);
    }
    
    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
    
    @Override
    public Page<Course> findAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }
    
    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByCode(String code) {
        return courseRepository.findByCourseCode(code).isPresent();
    }
    
    @Override
    public List<Course> findByStatus(Course.CourseStatus status) {
        return courseRepository.findByStatus(status);
    }
    
    @Override
    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }
    
    @Override
    public List<Course> findByKeyword(String keyword) {
        return courseRepository.findByKeyword(keyword);
    }
    
    @Override
    public Page<Course> findByNameContaining(String name, Pageable pageable) {
        return courseRepository.findByNameContaining(name, pageable);
    }
    
    @Override
    public Page<Course> findByCodeContaining(String code, Pageable pageable) {
        return courseRepository.findByCourseCodeContaining(code, pageable);
    }
    
    @Override
    public Page<Course> findByStatus(Course.CourseStatus status, Pageable pageable) {
        return courseRepository.findByStatus(status, pageable);
    }
    
    @Override
    public Page<Course> findByTeacherId(Long teacherId, Pageable pageable) {
        return courseRepository.findByTeacherId(teacherId, pageable);
    }
    
    @Override
    public Page<Course> findByAdvancedSearch(String name, String code, 
                                            Course.CourseStatus status, Pageable pageable) {
        if (name != null && !name.trim().isEmpty()) {
            return courseRepository.findByNameContaining(name, pageable);
        } else if (code != null && !code.trim().isEmpty()) {
            return courseRepository.findByCourseCodeContaining(code, pageable);
        } else if (status != null) {
            return courseRepository.findByStatus(status, pageable);
        } else {
            return courseRepository.findAll(pageable);
        }
    }
    
    @Override
    public Long countByStatus(Course.CourseStatus status) {
        return courseRepository.countByStatus(status);
    }
    
    @Override
    public Long countByTeacherId(Long teacherId) {
        return courseRepository.countByTeacherId(teacherId);
    }
    
    @Override
    public Course activateCourse(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setStatus(Course.CourseStatus.ACTIVE);
            return courseRepository.save(course);
        }
        throw new RuntimeException("課程不存在");
    }
    
    @Override
    public Course deactivateCourse(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setStatus(Course.CourseStatus.INACTIVE);
            return courseRepository.save(course);
        }
        throw new RuntimeException("課程不存在");
    }
    
    @Override
    public boolean validateCourseCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return code.matches("^[A-Z0-9]{2,10}$");
    }
    
    @Override
    public boolean validateCourseName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() >= 2 && name.length() <= 100;
    }
} 