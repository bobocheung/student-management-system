package com.student.repository;

import com.student.entity.Class;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Long> {
    
    Optional<Class> findByCode(String code);
    
    List<Class> findByGrade(Integer grade);
    
    List<Class> findByStatus(Class.ClassStatus status);
    
    List<Class> findByTeacherId(Long teacherId);
    
    @Query("SELECT c FROM Class c WHERE c.name LIKE %:keyword% OR c.code LIKE %:keyword%")
    List<Class> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Class c WHERE c.grade = :grade AND c.status = :status")
    List<Class> findByGradeAndStatus(@Param("grade") Integer grade, @Param("status") Class.ClassStatus status);
    
    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId AND c.status = :status")
    List<Class> findByTeacherIdAndStatus(@Param("teacherId") Long teacherId, @Param("status") Class.ClassStatus status);
    
    @Query("SELECT c FROM Class c WHERE c.name LIKE %:name%")
    Page<Class> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT c FROM Class c WHERE c.code LIKE %:code%")
    Page<Class> findByCodeContaining(@Param("code") String code, Pageable pageable);
    
    @Query("SELECT c FROM Class c WHERE c.grade = :grade")
    Page<Class> findByGrade(@Param("grade") Integer grade, Pageable pageable);
    
    @Query("SELECT c FROM Class c WHERE c.status = :status")
    Page<Class> findByStatus(@Param("status") Class.ClassStatus status, Pageable pageable);
    
    @Query("SELECT c FROM Class c WHERE c.teacher.id = :teacherId")
    Page<Class> findByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);
    
    @Query("SELECT c FROM Class c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:code IS NULL OR c.code LIKE %:code%) AND " +
           "(:grade IS NULL OR c.grade = :grade) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:teacherId IS NULL OR c.teacher.id = :teacherId)")
    Page<Class> findByAdvancedSearch(@Param("name") String name,
                                    @Param("code") String code,
                                    @Param("grade") Integer grade,
                                    @Param("status") Class.ClassStatus status,
                                    @Param("teacherId") Long teacherId,
                                    Pageable pageable);
    
    // 統計方法
    Long countByStatus(Class.ClassStatus status);
    
    Long countByGrade(Integer grade);
    
    Long countByTeacherId(Long teacherId);
} 