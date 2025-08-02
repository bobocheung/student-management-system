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
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;

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
        List<Student> students = new ArrayList<>();
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // 跳過標題行，從第二行開始讀取
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Student student = new Student();
                
                // 讀取各列數據
                Cell studentNumberCell = row.getCell(0);
                if (studentNumberCell != null) {
                    student.setStudentNumber(getCellStringValue(studentNumberCell));
                }
                
                Cell nameCell = row.getCell(1);
                if (nameCell != null) {
                    student.setName(getCellStringValue(nameCell));
                }
                
                Cell genderCell = row.getCell(2);
                if (genderCell != null) {
                    String genderStr = getCellStringValue(genderCell);
                    if ("男".equals(genderStr)) {
                        student.setGender(Student.Gender.MALE);
                    } else if ("女".equals(genderStr)) {
                        student.setGender(Student.Gender.FEMALE);
                    }
                }
                
                Cell birthDateCell = row.getCell(3);
                if (birthDateCell != null && birthDateCell.getCellType() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(birthDateCell)) {
                        student.setBirthDate(birthDateCell.getLocalDateTimeCellValue().toLocalDate());
                    }
                }
                
                Cell idNumberCell = row.getCell(4);
                if (idNumberCell != null) {
                    student.setIdNumber(getCellStringValue(idNumberCell));
                }
                
                Cell phoneCell = row.getCell(5);
                if (phoneCell != null) {
                    student.setPhone(getCellStringValue(phoneCell));
                }
                
                Cell emailCell = row.getCell(6);
                if (emailCell != null) {
                    student.setEmail(getCellStringValue(emailCell));
                }
                
                Cell addressCell = row.getCell(7);
                if (addressCell != null) {
                    student.setAddress(getCellStringValue(addressCell));
                }
                
                // 設置默認值
                student.setStatus(Student.StudentStatus.ENROLLED);
                student.setEnrollmentDate(LocalDate.now());
                
                // 驗證並保存
                if (validateStudent(student)) {
                    try {
                        students.add(saveStudent(student));
                    } catch (Exception e) {
                        // 記錄錯誤但繼續處理其他學生
                        System.err.println("保存學生失敗: " + student.getName() + ", 錯誤: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel導入失敗: " + e.getMessage(), e);
        }
        
        return students;
    }
    
    @Override
    public byte[] exportStudentsToExcel(List<Student> students) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("學生信息");
            
            // 創建標題行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"學號", "姓名", "性別", "出生日期", "身份證號", "電話", "郵箱", "地址", "班級", "狀態", "入學日期"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                
                // 設置標題樣式
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充數據
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                Row row = sheet.createRow(i + 1);
                
                row.createCell(0).setCellValue(student.getStudentNumber() != null ? student.getStudentNumber() : "");
                row.createCell(1).setCellValue(student.getName() != null ? student.getName() : "");
                row.createCell(2).setCellValue(student.getGender() != null ? 
                    (student.getGender() == Student.Gender.MALE ? "男" : "女") : "");
                
                if (student.getBirthDate() != null) {
                    CellStyle dateStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
                    Cell dateCell = row.createCell(3);
                    dateCell.setCellValue(student.getBirthDate());
                    dateCell.setCellStyle(dateStyle);
                } else {
                    row.createCell(3).setCellValue("");
                }
                
                row.createCell(4).setCellValue(student.getIdNumber() != null ? student.getIdNumber() : "");
                row.createCell(5).setCellValue(student.getPhone() != null ? student.getPhone() : "");
                row.createCell(6).setCellValue(student.getEmail() != null ? student.getEmail() : "");
                row.createCell(7).setCellValue(student.getAddress() != null ? student.getAddress() : "");
                row.createCell(8).setCellValue(student.getClassInfo() != null ? student.getClassInfo().getName() : "");
                row.createCell(9).setCellValue(student.getStatus() != null ? student.getStatus().getDisplayName() : "");
                
                if (student.getEnrollmentDate() != null) {
                    CellStyle dateStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
                    Cell dateCell = row.createCell(10);
                    dateCell.setCellValue(student.getEnrollmentDate());
                    dateCell.setCellStyle(dateStyle);
                } else {
                    row.createCell(10).setCellValue("");
                }
            }
            
            // 自動調整列寬
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 轉換為字節數組
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Excel導出失敗: " + e.getMessage(), e);
        }
    }
    
    // 輔助方法：獲取單元格字符串值
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    // 輔助方法：驗證學生信息
    private boolean validateStudent(Student student) {
        return student.getStudentNumber() != null && !student.getStudentNumber().trim().isEmpty() &&
               student.getName() != null && !student.getName().trim().isEmpty() &&
               validateStudentNumber(student.getStudentNumber()) &&
               (student.getIdNumber() == null || validateIdNumber(student.getIdNumber())) &&
               (student.getPhone() == null || validatePhone(student.getPhone())) &&
               (student.getEmail() == null || validateEmail(student.getEmail()));
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