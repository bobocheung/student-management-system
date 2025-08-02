package com.student.controller;

import com.student.entity.Attendance;
import com.student.entity.Student;
import com.student.entity.Course;
import com.student.service.AttendanceService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/attendance")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    // 考勤列表頁面
    @GetMapping
    public String listAttendance(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) String studentName,
                               @RequestParam(required = false) String courseName,
                               @RequestParam(required = false) LocalDate date,
                               Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        
        Page<Attendance> attendances;
        if (studentName != null || courseName != null || date != null) {
            attendances = attendanceService.findByAdvancedSearch(studentName, courseName, date, pageable);
        } else {
            attendances = attendanceService.findAllAttendance(pageable);
        }
        
        model.addAttribute("attendances", attendances);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", attendances.getTotalPages());
        model.addAttribute("totalItems", attendances.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchStudentName", studentName);
        model.addAttribute("searchCourseName", courseName);
        model.addAttribute("searchDate", date);
        
        return "attendance/list";
    }
    
    // 考勤詳情頁面
    @GetMapping("/{id}")
    public String viewAttendance(@PathVariable Long id, Model model) {
        Optional<Attendance> attendanceOpt = attendanceService.findById(id);
        if (attendanceOpt.isPresent()) {
            model.addAttribute("attendance", attendanceOpt.get());
            return "attendance/view";
        }
        return "redirect:/attendance";
    }
    
    // 新增考勤頁面
    @GetMapping("/new")
    public String newAttendanceForm(Model model) {
        model.addAttribute("attendance", new Attendance());
        List<Student> students = studentService.findAllStudents();
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return "attendance/form";
    }
    
    // 編輯考勤頁面
    @GetMapping("/{id}/edit")
    public String editAttendanceForm(@PathVariable Long id, Model model) {
        Optional<Attendance> attendanceOpt = attendanceService.findById(id);
        if (attendanceOpt.isPresent()) {
            model.addAttribute("attendance", attendanceOpt.get());
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "attendance/form";
        }
        return "redirect:/attendance";
    }
    
    // 保存考勤
    @PostMapping
    public String saveAttendance(@Valid @ModelAttribute Attendance attendance,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "attendance/form";
        }
        
        try {
            Attendance savedAttendance = attendanceService.saveAttendance(attendance);
            redirectAttributes.addFlashAttribute("success", "考勤記錄保存成功");
            return "redirect:/attendance/" + savedAttendance.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            return "attendance/form";
        }
    }
    
    // 刪除考勤
    @PostMapping("/{id}/delete")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            attendanceService.deleteAttendance(id);
            redirectAttributes.addFlashAttribute("success", "考勤記錄刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/attendance";
    }
    
    // API 端點
    @GetMapping("/api")
    @ResponseBody
    public Page<Attendance> getAttendanceApi(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(required = false) String studentName,
                                            @RequestParam(required = false) String courseName,
                                            @RequestParam(required = false) LocalDate date) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        
        if (studentName != null || courseName != null || date != null) {
            return attendanceService.findByAdvancedSearch(studentName, courseName, date, pageable);
        } else {
            return attendanceService.findAllAttendance(pageable);
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public Attendance getAttendanceApi(@PathVariable Long id) {
        return attendanceService.findById(id).orElse(null);
    }
} 