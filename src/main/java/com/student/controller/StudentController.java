package com.student.controller;

import com.student.entity.Student;
import com.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    // 學生列表頁面
    @GetMapping
    public String listStudents(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String studentNumber,
                              @RequestParam(required = false) Long classId,
                              @RequestParam(required = false) Student.StudentStatus status,
                              @RequestParam(required = false) Student.Gender gender,
                              Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Student> students;
        if (name != null || studentNumber != null || classId != null || status != null || gender != null) {
            students = studentService.findByAdvancedSearch(name, studentNumber, classId, status, gender, pageable);
        } else {
            students = studentService.findAllStudents(pageable);
        }
        
        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.getTotalPages());
        model.addAttribute("totalItems", students.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchName", name);
        model.addAttribute("searchStudentNumber", studentNumber);
        model.addAttribute("searchClassId", classId);
        model.addAttribute("searchStatus", status);
        model.addAttribute("searchGender", gender);
        
        return "student/list";
    }
    
    // 學生詳情頁面
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Optional<Student> studentOpt = studentService.findById(id);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            return "student/view";
        }
        return "redirect:/students";
    }
    
    // 新增學生頁面
    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("genders", Student.Gender.values());
        model.addAttribute("statuses", Student.StudentStatus.values());
        return "student/form";
    }
    
    // 編輯學生頁面
    @GetMapping("/{id}/edit")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Optional<Student> studentOpt = studentService.findById(id);
        if (studentOpt.isPresent()) {
            model.addAttribute("student", studentOpt.get());
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("statuses", Student.StudentStatus.values());
            return "student/form";
        }
        return "redirect:/students";
    }
    
    // 保存學生
    @PostMapping
    public String saveStudent(@Valid @ModelAttribute Student student,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("statuses", Student.StudentStatus.values());
            return "student/form";
        }
        
        try {
            Student savedStudent = studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("success", "學生信息保存成功");
            return "redirect:/students/" + savedStudent.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("statuses", Student.StudentStatus.values());
            return "student/form";
        }
    }
    
    // 刪除學生
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "學生刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/students";
    }
    
    // Excel導入頁面
    @GetMapping("/import")
    public String importPage() {
        return "student/import";
    }
    
    // Excel導入處理
    @PostMapping("/import")
    public String importStudents(@RequestParam("file") MultipartFile file, 
                               RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "請選擇要導入的Excel文件");
            return "redirect:/students/import";
        }
        
        try {
            List<Student> importedStudents = studentService.importStudentsFromExcel(file);
            redirectAttributes.addFlashAttribute("success", 
                "成功導入 " + importedStudents.size() + " 條學生記錄");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "導入失敗：" + e.getMessage());
        }
        
        return "redirect:/students";
    }
    
    // Excel導出
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Student.StudentStatus status,
            @RequestParam(required = false) Student.Gender gender) {
        
        try {
            List<Student> students;
            if (name != null || studentNumber != null || classId != null || status != null || gender != null) {
                // 如果有搜索條件，導出搜索結果
                Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
                Page<Student> studentPage = studentService.findByAdvancedSearch(name, studentNumber, classId, status, gender, pageable);
                students = studentPage.getContent();
            } else {
                // 導出所有學生
                students = studentService.findAllStudents();
            }
            
            byte[] excelData = studentService.exportStudentsToExcel(students);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "students_" + LocalDate.now() + ".xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // 下載Excel模板
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            // 創建一個空的Excel模板
            List<Student> emptyList = List.of();
            byte[] templateData = studentService.exportStudentsToExcel(emptyList);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "student_template.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(templateData);
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}