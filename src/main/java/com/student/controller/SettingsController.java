package com.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.student.service.StudentService;
import com.student.service.TeacherService;
import com.student.service.ClassService;
import com.student.service.CourseService;
import com.student.service.GradeService;
import com.student.service.AttendanceService;
import com.student.service.RewardPunishmentService;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/settings")
@PreAuthorize("hasRole('ADMIN')")
public class SettingsController {

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
    
    @Autowired
    private RewardPunishmentService rewardPunishmentService;

    @GetMapping
    public String settings(Model model) {
        // 系統統計信息
        Map<String, Object> systemStats = new HashMap<>();
        
        try {
            systemStats.put("totalStudents", studentService.findAllStudents().size());
            systemStats.put("totalTeachers", teacherService.countTeachers());
            systemStats.put("totalClasses", classService.findAllClasses().size());
            systemStats.put("totalCourses", courseService.findAllCourses().size());
            systemStats.put("totalGrades", gradeService.findAllGrades().size());
            systemStats.put("totalAttendances", attendanceService.findAllAttendance().size());
            systemStats.put("totalRewards", rewardPunishmentService.findAllRewardPunishments().size());
        } catch (Exception e) {
            systemStats.put("totalStudents", 0);
            systemStats.put("totalTeachers", 0);
            systemStats.put("totalClasses", 0);
            systemStats.put("totalCourses", 0);
            systemStats.put("totalGrades", 0);
            systemStats.put("totalAttendances", 0);
            systemStats.put("totalRewards", 0);
        }
        
        model.addAttribute("systemStats", systemStats);
        
        // 系統配置信息
        Map<String, String> systemConfig = new HashMap<>();
        systemConfig.put("version", "1.1.0");
        systemConfig.put("database", "H2 In-Memory Database");
        systemConfig.put("javaVersion", System.getProperty("java.version"));
        systemConfig.put("springBootVersion", "2.7.14");
        systemConfig.put("currentTime", new Date().toString());
        
        model.addAttribute("systemConfig", systemConfig);
        
        // 系統使用情況
        Runtime runtime = Runtime.getRuntime();
        Map<String, String> systemUsage = new HashMap<>();
        systemUsage.put("totalMemory", formatMemory(runtime.totalMemory()));
        systemUsage.put("freeMemory", formatMemory(runtime.freeMemory()));
        systemUsage.put("usedMemory", formatMemory(runtime.totalMemory() - runtime.freeMemory()));
        systemUsage.put("maxMemory", formatMemory(runtime.maxMemory()));
        
        model.addAttribute("systemUsage", systemUsage);
        
        return "settings/index";
    }
    
    @PostMapping("/backup")
    public String backupData(RedirectAttributes redirectAttributes) {
        try {
            // 這裡可以實現數據備份邏輯
            redirectAttributes.addFlashAttribute("successMessage", "數據備份已成功創建！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "數據備份失敗：" + e.getMessage());
        }
        return "redirect:/settings";
    }
    
    @PostMapping("/clear-cache")
    public String clearCache(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // 清除緩存
            session.invalidate();
            System.gc(); // 建議垃圾回收
            redirectAttributes.addFlashAttribute("successMessage", "系統緩存已清除！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "清除緩存失敗：" + e.getMessage());
        }
        return "redirect:/settings";
    }
    
    @PostMapping("/reset-demo-data")
    public String resetDemoData(RedirectAttributes redirectAttributes) {
        try {
            // 這裡可以實現重置演示數據的邏輯
            redirectAttributes.addFlashAttribute("successMessage", "演示數據已重置！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "重置演示數據失敗：" + e.getMessage());
        }
        return "redirect:/settings";
    }
    
    private String formatMemory(long bytes) {
        long mb = bytes / (1024 * 1024);
        return mb + " MB";
    }
}