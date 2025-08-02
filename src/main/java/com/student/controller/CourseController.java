package com.student.controller;

import com.student.entity.Course;

import com.student.service.CourseService;
import com.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/courses")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private TeacherService teacherService;
    
    // 課程列表頁面
    @GetMapping
    public String listCourses(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String code,
                             @RequestParam(required = false) Course.CourseStatus status,
                             Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Course> courses;
        if (name != null || code != null || status != null) {
            courses = courseService.findByAdvancedSearch(name, code, status, pageable);
        } else {
            courses = courseService.findAllCourses(pageable);
        }
        
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.getTotalPages());
        model.addAttribute("totalItems", courses.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchName", name);
        model.addAttribute("searchCode", code);
        model.addAttribute("searchStatus", status);
        
        return "course/list";
    }
    
    // 課程詳情頁面
    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Optional<Course> courseOpt = courseService.findById(id);
        if (courseOpt.isPresent()) {
            model.addAttribute("course", courseOpt.get());
            return "course/view";
        }
        return "redirect:/courses";
    }
    
    // 新增課程頁面
    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("teachers", teacherService.findAllActiveTeachers());
        model.addAttribute("statuses", Course.CourseStatus.values());
        return "course/form";
    }
    
    // 編輯課程頁面
    @GetMapping("/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Optional<Course> courseOpt = courseService.findById(id);
        if (courseOpt.isPresent()) {
            model.addAttribute("course", courseOpt.get());
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Course.CourseStatus.values());
            return "course/form";
        }
        return "redirect:/courses";
    }
    
    // 保存課程
    @PostMapping
    public String saveCourse(@Valid @ModelAttribute Course course,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Course.CourseStatus.values());
            return "course/form";
        }
        
        try {
            Course savedCourse = courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("success", "課程信息保存成功");
            return "redirect:/courses/" + savedCourse.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Course.CourseStatus.values());
            return "course/form";
        }
    }
    
    // 刪除課程
    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("success", "課程刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/courses";
    }
    
    // API接口 - 獲取課程列表
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Course>> getCoursesApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Course.CourseStatus status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Course> courses;
        if (name != null || code != null || status != null) {
            courses = courseService.findByAdvancedSearch(name, code, status, pageable);
        } else {
            courses = courseService.findAllCourses(pageable);
        }
        
        return ResponseEntity.ok(courses);
    }
    
    // API接口 - 獲取課程詳情
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Course> getCourseApi(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.findById(id);
        return courseOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 