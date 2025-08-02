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
                             @RequestParam(required = false) MultipartFile photo,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("genders", Student.Gender.values());
            model.addAttribute("statuses", Student.StudentStatus.values());
            return "student/form";
        }
        
        try {
            // 保存學生信息
            Student savedStudent = studentService.saveStudent(student);
            
            // 上傳照片
            if (photo != null && !photo.isEmpty()) {
                studentService.uploadPhoto(savedStudent.getId(), photo);
            }
            
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
    
    // 學生入學
    @PostMapping("/{id}/enroll")
    public String enrollStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Student> studentOpt = studentService.findById(id);
            if (studentOpt.isPresent()) {
                studentService.enrollStudent(studentOpt.get());
                redirectAttributes.addFlashAttribute("success", "學生入學成功");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "入學失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 學生休學
    @PostMapping("/{id}/suspend")
    public String suspendStudent(@PathVariable Long id, 
                                @RequestParam String reason,
                                RedirectAttributes redirectAttributes) {
        try {
            studentService.suspendStudent(id, reason);
            redirectAttributes.addFlashAttribute("success", "學生休學成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "休學失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 學生復學
    @PostMapping("/{id}/resume")
    public String resumeStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.resumeStudent(id);
            redirectAttributes.addFlashAttribute("success", "學生復學成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "復學失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 學生畢業
    @PostMapping("/{id}/graduate")
    public String graduateStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.graduateStudent(id);
            redirectAttributes.addFlashAttribute("success", "學生畢業成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "畢業失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 學生退學
    @PostMapping("/{id}/withdraw")
    public String withdrawStudent(@PathVariable Long id, 
                                 @RequestParam String reason,
                                 RedirectAttributes redirectAttributes) {
        try {
            studentService.withdrawStudent(id, reason);
            redirectAttributes.addFlashAttribute("success", "學生退學成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "退學失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 上傳照片
    @PostMapping("/{id}/photo")
    public String uploadPhoto(@PathVariable Long id,
                             @RequestParam("photo") MultipartFile photo,
                             RedirectAttributes redirectAttributes) {
        try {
            studentService.uploadPhoto(id, photo);
            redirectAttributes.addFlashAttribute("success", "照片上傳成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "照片上傳失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // 刪除照片
    @PostMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("success", "照片刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "照片刪除失敗：" + e.getMessage());
        }
        return "redirect:/students/" + id;
    }
    
    // API接口 - 獲取學生列表
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Student>> getStudentsApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Student.StudentStatus status,
            @RequestParam(required = false) Student.Gender gender) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Student> students;
        if (name != null || studentNumber != null || classId != null || status != null || gender != null) {
            students = studentService.findByAdvancedSearch(name, studentNumber, classId, status, gender, pageable);
        } else {
            students = studentService.findAllStudents(pageable);
        }
        
        return ResponseEntity.ok(students);
    }
    
    // API接口 - 獲取學生詳情
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Student> getStudentApi(@PathVariable Long id) {
        Optional<Student> studentOpt = studentService.findById(id);
        return studentOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // API接口 - 創建學生
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Student> createStudentApi(@Valid @RequestBody Student student) {
        try {
            Student savedStudent = studentService.saveStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // API接口 - 更新學生
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Student> updateStudentApi(@PathVariable Long id, @Valid @RequestBody Student student) {
        try {
            student.setId(id);
            Student updatedStudent = studentService.saveStudent(student);
            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // API接口 - 刪除學生
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteStudentApi(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 導出學生數據
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStudents(@RequestParam(required = false) String name,
                                                @RequestParam(required = false) String studentNumber,
                                                @RequestParam(required = false) Long classId,
                                                @RequestParam(required = false) Student.StudentStatus status,
                                                @RequestParam(required = false) Student.Gender gender) {
        try {
            List<Student> students;
            if (name != null || studentNumber != null || classId != null || status != null || gender != null) {
                Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
                Page<Student> studentPage = studentService.findByAdvancedSearch(name, studentNumber, classId, status, gender, pageable);
                students = studentPage.getContent();
            } else {
                students = studentService.findAllStudents();
            }
            
            byte[] excelData = studentService.exportStudentsToExcel(students);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "students.xlsx");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 導入學生數據
    @PostMapping("/import")
    public String importStudents(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            List<Student> importedStudents = studentService.importStudentsFromExcel(file);
            redirectAttributes.addFlashAttribute("success", "成功導入 " + importedStudents.size() + " 名學生");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "導入失敗：" + e.getMessage());
        }
        return "redirect:/students";
    }
} 