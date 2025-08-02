package com.student.repository;

import com.student.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    Optional<Course> findByCourseCode(String courseCode);
    
    List<Course> findByTeacherId(Long teacherId);
    
    List<Course> findByStatus(Course.CourseStatus status);
    
    List<Course> findByType(Course.CourseType type);
    
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword% OR c.courseCode LIKE %:keyword%")
    List<Course> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId AND c.status = :status")
    List<Course> findByTeacherIdAndStatus(@Param("teacherId") Long teacherId, @Param("status") Course.CourseStatus status);
    
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:name%")
    Page<Course> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.courseCode LIKE %:courseCode%")
    Page<Course> findByCourseCodeContaining(@Param("courseCode") String courseCode, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.status = :status")
    Page<Course> findByStatus(@Param("status") Course.CourseStatus status, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.type = :type")
    Page<Course> findByType(@Param("type") Course.CourseType type, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId")
    Page<Course> findByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);
    
    @Query("SELECT c FROM Course c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:courseCode IS NULL OR c.courseCode LIKE %:courseCode%) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:type IS NULL OR c.type = :type) AND " +
           "(:teacherId IS NULL OR c.teacher.id = :teacherId)")
    Page<Course> findByAdvancedSearch(@Param("name") String name,
                                     @Param("courseCode") String courseCode,
                                     @Param("status") Course.CourseStatus status,
                                     @Param("type") Course.CourseType type,
                                     @Param("teacherId") Long teacherId,
                                     Pageable pageable);
    
    // 統計方法
    Long countByStatus(Course.CourseStatus status);
    
    Long countByTeacherId(Long teacherId);
} 