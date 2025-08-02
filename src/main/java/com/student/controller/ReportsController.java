package com.student.controller;

import com.student.entity.Student;
import com.student.service.StudentService;
import com.student.service.ClassService;
import com.student.service.CourseService;
import com.student.service.TeacherService;
import com.student.service.GradeService;
import com.student.service.AttendanceService;
import com.student.service.RewardPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportsController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private RewardPunishmentService rewardPunishmentService;
    
    @GetMapping
    public String index(@RequestParam(required = false) String reportType, Model model) {
        try {
            // 基本統計數據
            long totalStudents = studentService.findAllStudents().size();
            long totalTeachers = teacherService.countTeachers();
            long totalClasses = classService.findAllClasses().size();
            long totalCourses = courseService.findAllCourses().size();
            long totalGrades = gradeService.findAllGrades().size();
            long totalAttendances = attendanceService.findAllAttendance().size();
            long totalRewards = rewardPunishmentService.findAllRewardPunishments().size();
            
            // 性別統計
            long maleStudents = studentService.countByGender(Student.Gender.MALE);
            long femaleStudents = studentService.countByGender(Student.Gender.FEMALE);
            
            model.addAttribute("totalStudents", totalStudents);
            model.addAttribute("totalTeachers", totalTeachers);
            model.addAttribute("totalClasses", totalClasses);
            model.addAttribute("totalCourses", totalCourses);
            model.addAttribute("totalGrades", totalGrades);
            model.addAttribute("totalAttendances", totalAttendances);
            model.addAttribute("totalRewards", totalRewards);
            model.addAttribute("maleStudents", maleStudents);
            model.addAttribute("femaleStudents", femaleStudents);
            
            // 學生狀態統計
            long enrolledStudents = studentService.countByStatus(Student.StudentStatus.ENROLLED);
            long graduatedStudents = studentService.countByStatus(Student.StudentStatus.GRADUATED);
            long suspendedStudents = studentService.countByStatus(Student.StudentStatus.SUSPENDED);
            long withdrawnStudents = studentService.countByStatus(Student.StudentStatus.WITHDRAWN);
            
            model.addAttribute("enrolledStudents", enrolledStudents);
            model.addAttribute("graduatedStudents", graduatedStudents);
            model.addAttribute("suspendedStudents", suspendedStudents);
            model.addAttribute("withdrawnStudents", withdrawnStudents);
        } catch (Exception e) {
            // 如果出現錯誤，設置默認值
            model.addAttribute("totalStudents", 0);
            model.addAttribute("totalTeachers", 0);
            model.addAttribute("totalClasses", 0);
            model.addAttribute("totalCourses", 0);
            model.addAttribute("totalGrades", 0);
            model.addAttribute("totalAttendances", 0);
            model.addAttribute("totalRewards", 0);
            model.addAttribute("maleStudents", 0);
            model.addAttribute("femaleStudents", 0);
            model.addAttribute("enrolledStudents", 0);
            model.addAttribute("graduatedStudents", 0);
            model.addAttribute("suspendedStudents", 0);
            model.addAttribute("withdrawnStudents", 0);
        }
        
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
    
    // API端點：獲取統計數據用於圖表
    @GetMapping("/api/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 基本統計數據
            statistics.put("totalStudents", studentService.findAllStudents().size());
            statistics.put("totalTeachers", teacherService.countTeachers());
            statistics.put("totalClasses", classService.findAllClasses().size());
            statistics.put("totalCourses", courseService.findAllCourses().size());
            statistics.put("totalGrades", gradeService.findAllGrades().size());
            statistics.put("totalAttendances", attendanceService.findAllAttendance().size());
            statistics.put("totalRewards", rewardPunishmentService.findAllRewardPunishments().size());
            
            // 性別統計
            statistics.put("maleStudents", studentService.countByGender(Student.Gender.MALE));
            statistics.put("femaleStudents", studentService.countByGender(Student.Gender.FEMALE));
            
            // 學生狀態統計
            statistics.put("enrolledStudents", studentService.countByStatus(Student.StudentStatus.ENROLLED));
            statistics.put("graduatedStudents", studentService.countByStatus(Student.StudentStatus.GRADUATED));
            statistics.put("suspendedStudents", studentService.countByStatus(Student.StudentStatus.SUSPENDED));
            statistics.put("withdrawnStudents", studentService.countByStatus(Student.StudentStatus.WITHDRAWN));
            
        } catch (Exception e) {
            statistics.put("error", "獲取統計數據時發生錯誤: " + e.getMessage());
        }
        
        return ResponseEntity.ok(statistics);
    }
    
    // API端點：獲取性別分佈數據
    @GetMapping("/api/gender-distribution")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGenderDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long maleCount = studentService.countByGender(Student.Gender.MALE);
            long femaleCount = studentService.countByGender(Student.Gender.FEMALE);
            
            result.put("male", maleCount);
            result.put("female", femaleCount);
            result.put("total", maleCount + femaleCount);
        } catch (Exception e) {
            result.put("error", "獲取性別分佈數據時發生錯誤: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    // API端點：獲取學生狀態分佈數據
    @GetMapping("/api/student-status-distribution")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentStatusDistribution() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("enrolled", studentService.countByStatus(Student.StudentStatus.ENROLLED));
            result.put("graduated", studentService.countByStatus(Student.StudentStatus.GRADUATED));
            result.put("suspended", studentService.countByStatus(Student.StudentStatus.SUSPENDED));
            result.put("withdrawn", studentService.countByStatus(Student.StudentStatus.WITHDRAWN));
        } catch (Exception e) {
            result.put("error", "獲取學生狀態分佈數據時發生錯誤: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
}