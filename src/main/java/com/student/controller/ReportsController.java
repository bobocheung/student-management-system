package com.student.controller;

import com.student.entity.Student;
import com.student.service.StudentService;
import com.student.service.TeacherService;
import com.student.service.ClassService;
import com.student.service.CourseService;
import com.student.service.GradeService;
import com.student.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class ReportsController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @GetMapping
    public String reports(@RequestParam(required = false) String reportType, Model model) {
        
        // 基本統計數據
        long totalStudents = studentService.countByStatus(Student.StudentStatus.ENROLLED);
        long maleStudents = studentService.countByGender(Student.Gender.MALE);
        long femaleStudents = studentService.countByGender(Student.Gender.FEMALE);
        long totalTeachers = teacherService.countTeachers();
        long totalClasses = classService.findAllClasses().size();
        long totalCourses = courseService.findAllCourses().size();
        
        // 學生狀態統計
        long enrolledStudents = studentService.countByStatus(Student.StudentStatus.ENROLLED);
        long graduatedStudents = studentService.countByStatus(Student.StudentStatus.GRADUATED);
        long suspendedStudents = studentService.countByStatus(Student.StudentStatus.SUSPENDED);
        long withdrawnStudents = studentService.countByStatus(Student.StudentStatus.WITHDRAWN);
        
        // 添加到模型
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("maleStudents", maleStudents);
        model.addAttribute("femaleStudents", femaleStudents);
        model.addAttribute("totalTeachers", totalTeachers);
        model.addAttribute("totalClasses", totalClasses);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("graduatedStudents", graduatedStudents);
        model.addAttribute("suspendedStudents", suspendedStudents);
        model.addAttribute("withdrawnStudents", withdrawnStudents);
        
        // 根據報告類型添加特定數據
        if ("student" != null && "student".equals(reportType)) {
            // 學生相關報告
            model.addAttribute("reportType", "student");
        } else if ("grade" != null && "grade".equals(reportType)) {
            // 成績相關報告
            model.addAttribute("reportType", "grade");
        } else if ("attendance" != null && "attendance".equals(reportType)) {
            // 考勤相關報告
            model.addAttribute("reportType", "attendance");
        } else {
            // 默認總覽報告
            model.addAttribute("reportType", "overview");
        }
        
        return "reports/index";
    }
    
    @GetMapping("/student")
    public String studentReports(Model model) {
        // 學生詳細報告
        return "reports/student";
    }
    
    @GetMapping("/grade")
    public String gradeReports(Model model) {
        // 成績詳細報告
        return "reports/grade";
    }
    
    @GetMapping("/attendance")
    public String attendanceReports(Model model) {
        // 考勤詳細報告
        return "reports/attendance";
    }
} 