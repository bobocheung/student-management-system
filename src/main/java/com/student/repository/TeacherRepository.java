package com.student.repository;

import com.student.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByTeacherNumber(String teacherNumber);
    
    Optional<Teacher> findByIdNumber(String idNumber);
    
    Optional<Teacher> findByPhone(String phone);
    
    Optional<Teacher> findByEmail(String email);
    
    List<Teacher> findByStatus(Teacher.TeacherStatus status);
    
    List<Teacher> findByGender(Teacher.Gender gender);
    
    List<Teacher> findByTitle(Teacher.Title title);
    
    List<Teacher> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT t FROM Teacher t WHERE t.name LIKE %:keyword% OR t.teacherNumber LIKE %:keyword%")
    List<Teacher> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT t FROM Teacher t WHERE t.status = :status")
    Page<Teacher> findByStatus(@Param("status") Teacher.TeacherStatus status, Pageable pageable);
    
    @Query("SELECT t FROM Teacher t WHERE t.name LIKE %:name%")
    Page<Teacher> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT t FROM Teacher t WHERE t.teacherNumber LIKE %:teacherNumber%")
    Page<Teacher> findByTeacherNumberContaining(@Param("teacherNumber") String teacherNumber, Pageable pageable);
    
    @Query("SELECT t FROM Teacher t WHERE t.title = :title")
    Page<Teacher> findByTitle(@Param("title") Teacher.Title title, Pageable pageable);
    
    @Query("SELECT t FROM Teacher t WHERE t.hireDate >= :startDate AND t.hireDate <= :endDate")
    Page<Teacher> findByHireDateBetween(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate, 
                                       Pageable pageable);
    
    @Query("SELECT t FROM Teacher t WHERE " +
           "(:name IS NULL OR t.name LIKE %:name%) AND " +
           "(:teacherNumber IS NULL OR t.teacherNumber LIKE %:teacherNumber%) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:title IS NULL OR t.title = :title)")
    Page<Teacher> findByAdvancedSearch(@Param("name") String name,
                                      @Param("teacherNumber") String teacherNumber,
                                      @Param("status") Teacher.TeacherStatus status,
                                      @Param("title") Teacher.Title title,
                                      Pageable pageable);
} 