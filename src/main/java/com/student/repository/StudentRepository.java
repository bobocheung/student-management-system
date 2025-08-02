package com.student.repository;

import com.student.entity.Student;
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
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    Optional<Student> findByStudentNumber(String studentNumber);
    
    Optional<Student> findByIdNumber(String idNumber);
    
    Optional<Student> findByPhone(String phone);
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findByClassInfoId(Long classId);
    
    List<Student> findByStatus(Student.StudentStatus status);
    
    List<Student> findByGender(Student.Gender gender);
    
    List<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword% OR s.studentNumber LIKE %:keyword%")
    List<Student> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT s FROM Student s WHERE s.classInfo.grade = :grade")
    List<Student> findByGrade(@Param("grade") Integer grade);
    
    @Query("SELECT s FROM Student s WHERE s.classInfo.id = :classId AND s.status = :status")
    List<Student> findByClassIdAndStatus(@Param("classId") Long classId, @Param("status") Student.StudentStatus status);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.status = :status")
    Long countByStatus(@Param("status") Student.StudentStatus status);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.gender = :gender")
    Long countByGender(@Param("gender") Student.Gender gender);
    
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name%")
    Page<Student> findByNameContaining(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.studentNumber LIKE %:studentNumber%")
    Page<Student> findByStudentNumberContaining(@Param("studentNumber") String studentNumber, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.classInfo.name LIKE %:className%")
    Page<Student> findByClassNameContaining(@Param("className") String className, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.status = :status")
    Page<Student> findByStatus(@Param("status") Student.StudentStatus status, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.classInfo.id = :classId")
    Page<Student> findByClassId(@Param("classId") Long classId, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.enrollmentDate >= :startDate AND s.enrollmentDate <= :endDate")
    Page<Student> findByEnrollmentDateBetween(@Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate, 
                                             Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.birthDate >= :startDate AND s.birthDate <= :endDate")
    Page<Student> findByBirthDateBetween(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate, 
                                        Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:studentNumber IS NULL OR s.studentNumber LIKE %:studentNumber%) AND " +
           "(:classId IS NULL OR s.classInfo.id = :classId) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:gender IS NULL OR s.gender = :gender)")
    Page<Student> findByAdvancedSearch(@Param("name") String name,
                                      @Param("studentNumber") String studentNumber,
                                      @Param("classId") Long classId,
                                      @Param("status") Student.StudentStatus status,
                                      @Param("gender") Student.Gender gender,
                                      Pageable pageable);
} 