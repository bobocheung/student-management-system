package com.student.controller;

import com.student.entity.Student;
import com.student.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private GradeService gradeService;
    
    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 獲取當前用戶信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        try {
            // 統計數據
            long totalStudents = studentService.countByStatus(Student.StudentStatus.ENROLLED);
            long maleStudents = studentService.countByGender(Student.Gender.MALE);
            long femaleStudents = studentService.countByGender(Student.Gender.FEMALE);
            long totalTeachers = teacherService.countTeachers();
            long totalCourses = courseService.findAllCourses().size();
            long totalGrades = gradeService.findAllGrades().size();
            
            model.addAttribute("username", username);
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("maleStudents", maleStudents);
            model.addAttribute("femaleStudents", femaleStudents);
            model.addAttribute("totalTeachers", totalTeachers);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("totalGrades", totalGrades);
        } catch (Exception e) {
            // 如果出現錯誤，設置默認值
            model.addAttribute("username", username);
            model.addAttribute("totalStudents", 0);
            model.addAttribute("maleStudents", 0);
            model.addAttribute("femaleStudents", 0);
            model.addAttribute("totalTeachers", 0);
            model.addAttribute("totalCourses", 0);
            model.addAttribute("totalGrades", 0);
        }
        
        return "dashboard";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
} 