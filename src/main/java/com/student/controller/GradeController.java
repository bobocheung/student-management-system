package com.student.controller;

import com.student.entity.Grade;
import com.student.entity.Student;
import com.student.entity.Course;
import com.student.service.GradeService;
import com.student.service.StudentService;
import com.student.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/grades")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    // 成績列表頁面
    @GetMapping
    public String listGrades(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String studentName,
                            @RequestParam(required = false) String courseName,
                            @RequestParam(required = false) String semester,
                            Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Grade> grades;
        if (studentName != null || courseName != null || semester != null) {
            grades = gradeService.findByAdvancedSearch(studentName, courseName, semester, pageable);
        } else {
            grades = gradeService.findAllGrades(pageable);
        }
        
        model.addAttribute("grades", grades);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", grades.getTotalPages());
        model.addAttribute("totalItems", grades.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchStudentName", studentName);
        model.addAttribute("searchCourseName", courseName);
        model.addAttribute("searchSemester", semester);
        
        return "grade/list";
    }
    
    // 成績詳情頁面
    @GetMapping("/{id}")
    public String viewGrade(@PathVariable Long id, Model model) {
        Optional<Grade> gradeOpt = gradeService.findById(id);
        if (gradeOpt.isPresent()) {
            model.addAttribute("grade", gradeOpt.get());
            return "grade/view";
        }
        return "redirect:/grades";
    }
    
    // 新增成績頁面
    @GetMapping("/new")
    public String newGradeForm(Model model) {
        model.addAttribute("grade", new Grade());
        List<Student> students = studentService.findAllStudents();
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return "grade/form";
    }
    
    // 編輯成績頁面
    @GetMapping("/{id}/edit")
    public String editGradeForm(@PathVariable Long id, Model model) {
        Optional<Grade> gradeOpt = gradeService.findById(id);
        if (gradeOpt.isPresent()) {
            model.addAttribute("grade", gradeOpt.get());
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "grade/form";
        }
        return "redirect:/grades";
    }
    
    // 保存成績
    @PostMapping
    public String saveGrade(@Valid @ModelAttribute Grade grade,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "grade/form";
        }
        
        try {
            Grade savedGrade = gradeService.saveGrade(grade);
            redirectAttributes.addFlashAttribute("success", "成績保存成功");
            return "redirect:/grades/" + savedGrade.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "grade/form";
        }
    }
    
    // 刪除成績
    @PostMapping("/{id}/delete")
    public String deleteGrade(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            gradeService.deleteGrade(id);
            redirectAttributes.addFlashAttribute("success", "成績刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/grades";
    }
    
    // API 端點
    @GetMapping("/api")
    @ResponseBody
    public Page<Grade> getGradesApi(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String studentName,
                                   @RequestParam(required = false) String courseName,
                                   @RequestParam(required = false) String semester) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        if (studentName != null || courseName != null || semester != null) {
            return gradeService.findByAdvancedSearch(studentName, courseName, semester, pageable);
        } else {
            return gradeService.findAllGrades(pageable);
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public Grade getGradeApi(@PathVariable Long id) {
        return gradeService.findById(id).orElse(null);
    }
} 