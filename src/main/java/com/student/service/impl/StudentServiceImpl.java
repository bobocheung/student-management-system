package com.student.service.impl;

import com.student.entity.Student;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Value("${app.upload.path}")
    private String uploadPath;
    
    @Override
    public Student saveStudent(Student student) {
        // 驗證學號唯一性
        if (student.getId() == null && existsByStudentNumber(student.getStudentNumber())) {
            throw new RuntimeException("學號已存在");
        }
        
        // 驗證身份證號唯一性
        if (student.getId() == null && existsByIdNumber(student.getIdNumber())) {
            throw new RuntimeException("身份證號已存在");
        }
        
        return studentRepository.save(student);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
    
    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdNumber(String idNumber) {
        return studentRepository.findByIdNumber(idNumber).isPresent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByClassId(Long classId) {
        return studentRepository.findByClassInfoId(classId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByStatus(Student.StudentStatus status) {
        return studentRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByGender(Student.Gender gender) {
        return studentRepository.findByGender(gender);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByGrade(Integer grade) {
        return studentRepository.findByGrade(grade);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByKeyword(String keyword) {
        return studentRepository.findByKeyword(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findByBirthDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findByEnrollmentDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByNameContaining(String name, Pageable pageable) {
        return studentRepository.findByNameContaining(name, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByStudentNumberContaining(String studentNumber, Pageable pageable) {
        return studentRepository.findByStudentNumberContaining(studentNumber, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByClassNameContaining(String className, Pageable pageable) {
        return studentRepository.findByClassNameContaining(className, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByStatus(Student.StudentStatus status, Pageable pageable) {
        return studentRepository.findByStatus(status, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByClassId(Long classId, Pageable pageable) {
        return studentRepository.findByClassId(classId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByEnrollmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return studentRepository.findByEnrollmentDateBetween(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByBirthDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return studentRepository.findByBirthDateBetween(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findByAdvancedSearch(String name, String studentNumber, Long classId, 
                                             Student.StudentStatus status, Student.Gender gender, Pageable pageable) {
        return studentRepository.findByAdvancedSearch(name, studentNumber, classId, status, gender, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(Student.StudentStatus status) {
        return studentRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByGender(Student.Gender gender) {
        return studentRepository.countByGender(gender);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByClassId(Long classId) {
        return Long.valueOf(studentRepository.findByClassInfoId(classId).size());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long countByGrade(Integer grade) {
        return Long.valueOf(studentRepository.findByGrade(grade).size());
    }
    
    @Override
    public Student enrollStudent(Student student) {
        student.setStatus(Student.StudentStatus.ENROLLED);
        student.setEnrollmentDate(LocalDate.now());
        return saveStudent(student);
    }
    
    @Override
    public Student transferStudent(Long studentId, Long newClassId) {
        // 這裡需要注入ClassRepository來實現轉班邏輯
        // 暫時返回null，實際實現時需要完善
        return null;
    }
    
    @Override
    public Student suspendStudent(Long studentId, String reason) {
        Optional<Student> studentOpt = findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus(Student.StudentStatus.SUSPENDED);
            return saveStudent(student);
        }
        throw new RuntimeException("學生不存在");
    }
    
    @Override
    public Student resumeStudent(Long studentId) {
        Optional<Student> studentOpt = findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus(Student.StudentStatus.ENROLLED);
            return saveStudent(student);
        }
        throw new RuntimeException("學生不存在");
    }
    
    @Override
    public Student graduateStudent(Long studentId) {
        Optional<Student> studentOpt = findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus(Student.StudentStatus.GRADUATED);
            student.setGraduationDate(LocalDate.now());
            return saveStudent(student);
        }
        throw new RuntimeException("學生不存在");
    }
    
    @Override
    public Student withdrawStudent(Long studentId, String reason) {
        Optional<Student> studentOpt = findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setStatus(Student.StudentStatus.WITHDRAWN);
            return saveStudent(student);
        }
        throw new RuntimeException("學生不存在");
    }
    
    @Override
    public String uploadPhoto(Long studentId, MultipartFile file) {
        try {
            Optional<Student> studentOpt = findById(studentId);
            if (!studentOpt.isPresent()) {
                throw new RuntimeException("學生不存在");
            }
            
            if (file.isEmpty()) {
                throw new RuntimeException("文件為空");
            }
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + "." + extension;
            
            // 創建目錄
            Path uploadDir = Paths.get(uploadPath, "students", "photos");
            Files.createDirectories(uploadDir);
            
            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            
            // 更新學生照片路徑
            Student student = studentOpt.get();
            student.setPhotoPath("students/photos/" + filename);
            saveStudent(student);
            
            return student.getPhotoPath();
        } catch (IOException e) {
            throw new RuntimeException("文件上傳失敗", e);
        }
    }
    
    @Override
    public void deletePhoto(Long studentId) {
        Optional<Student> studentOpt = findById(studentId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (student.getPhotoPath() != null) {
                try {
                    Path photoPath = Paths.get(uploadPath, student.getPhotoPath());
                    Files.deleteIfExists(photoPath);
                    student.setPhotoPath(null);
                    saveStudent(student);
                } catch (IOException e) {
                    throw new RuntimeException("刪除照片失敗", e);
                }
            }
        }
    }
    
    @Override
    public List<Student> importStudentsFromExcel(MultipartFile file) {
        // Excel導入邏輯，需要實現
        return null;
    }
    
    @Override
    public byte[] exportStudentsToExcel(List<Student> students) {
        // Excel導出邏輯，需要實現
        return null;
    }
    
    @Override
    public boolean validateStudentNumber(String studentNumber) {
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            return false;
        }
        // 學號格式驗證：年份+4位數字，例如20240001
        return studentNumber.matches("\\d{8}");
    }
    
    @Override
    public boolean validateIdNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return false;
        }
        // 身份證號格式驗證：18位數字
        return idNumber.matches("\\d{18}");
    }
    
    @Override
    public boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // 手機號格式驗證：11位數字
        return phone.matches("\\d{11}");
    }
    
    @Override
    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // 郵箱格式驗證
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
} 