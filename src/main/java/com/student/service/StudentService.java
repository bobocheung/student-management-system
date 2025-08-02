package com.student.service;

import com.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {
    
    // 基本CRUD操作
    Student saveStudent(Student student);
    
    Optional<Student> findById(Long id);
    
    Optional<Student> findByStudentNumber(String studentNumber);
    
    List<Student> findAllStudents();
    
    Page<Student> findAllStudents(Pageable pageable);
    
    void deleteStudent(Long id);
    
    boolean existsByStudentNumber(String studentNumber);
    
    boolean existsByIdNumber(String idNumber);
    
    // 查詢操作
    List<Student> findByClassId(Long classId);
    
    List<Student> findByStatus(Student.StudentStatus status);
    
    List<Student> findByGender(Student.Gender gender);
    
    List<Student> findByGrade(Integer grade);
    
    List<Student> findByKeyword(String keyword);
    
    List<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // 分頁查詢
    Page<Student> findByNameContaining(String name, Pageable pageable);
    
    Page<Student> findByStudentNumberContaining(String studentNumber, Pageable pageable);
    
    Page<Student> findByClassNameContaining(String className, Pageable pageable);
    
    Page<Student> findByStatus(Student.StudentStatus status, Pageable pageable);
    
    Page<Student> findByClassId(Long classId, Pageable pageable);
    
    Page<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Page<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    // 高級搜索
    Page<Student> findByAdvancedSearch(String name, String studentNumber, Long classId, 
                                      Student.StudentStatus status, Student.Gender gender, Pageable pageable);
    
    // 統計操作
    Long countByStatus(Student.StudentStatus status);
    
    Long countByGender(Student.Gender gender);
    
    Long countByClassId(Long classId);
    
    Long countByGrade(Integer grade);
    
    // 業務操作
    Student enrollStudent(Student student);
    
    Student transferStudent(Long studentId, Long newClassId);
    
    Student suspendStudent(Long studentId, String reason);
    
    Student resumeStudent(Long studentId);
    
    Student graduateStudent(Long studentId);
    
    Student withdrawStudent(Long studentId, String reason);
    
    // 文件操作
    String uploadPhoto(Long studentId, MultipartFile file);
    
    void deletePhoto(Long studentId);
    
    // 批量操作
    List<Student> importStudentsFromExcel(MultipartFile file);
    
    byte[] exportStudentsToExcel(List<Student> students);
    
    // 驗證操作
    boolean validateStudentNumber(String studentNumber);
    
    boolean validateIdNumber(String idNumber);
    
    boolean validatePhone(String phone);
    
    boolean validateEmail(String email);
} 