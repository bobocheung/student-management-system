package com.student.controller;

import com.student.entity.Teacher;
import com.student.service.TeacherService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/teachers")
@PreAuthorize("hasAnyRole('ADMIN')")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    // 教師列表頁面
    @GetMapping
    public String listTeachers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String teacherNumber,
                              @RequestParam(required = false) String department,
                              Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Teacher> teachers;
        if (name != null || teacherNumber != null || department != null) {
            teachers = teacherService.findByAdvancedSearch(name, teacherNumber, department, pageable);
        } else {
            teachers = teacherService.findAllTeachers(pageable);
        }
        
        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.getTotalPages());
        model.addAttribute("totalItems", teachers.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchName", name);
        model.addAttribute("searchTeacherNumber", teacherNumber);
        model.addAttribute("searchDepartment", department);
        
        return "teacher/list";
    }
    
    // 教師詳情頁面
    @GetMapping("/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        Optional<Teacher> teacherOpt = teacherService.findById(id);
        if (teacherOpt.isPresent()) {
            model.addAttribute("teacher", teacherOpt.get());
            return "teacher/view";
        }
        return "redirect:/teachers";
    }
    
    // 新增教師頁面
    @GetMapping("/new")
    public String newTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher/form";
    }
    
    // 編輯教師頁面
    @GetMapping("/{id}/edit")
    public String editTeacherForm(@PathVariable Long id, Model model) {
        Optional<Teacher> teacherOpt = teacherService.findById(id);
        if (teacherOpt.isPresent()) {
            model.addAttribute("teacher", teacherOpt.get());
            return "teacher/form";
        }
        return "redirect:/teachers";
    }
    
    // 保存教師
    @PostMapping
    public String saveTeacher(@Valid @ModelAttribute Teacher teacher,
                             BindingResult result,
                             @RequestParam(required = false) MultipartFile photo,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "teacher/form";
        }
        
        try {
            // 保存教師信息
            Teacher savedTeacher = teacherService.saveTeacher(teacher);
            
            // 上傳照片
            if (photo != null && !photo.isEmpty()) {
                teacherService.uploadPhoto(savedTeacher.getId(), photo);
            }
            
            redirectAttributes.addFlashAttribute("success", "教師信息保存成功");
            return "redirect:/teachers/" + savedTeacher.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "teacher/form";
        }
    }
    
    // 刪除教師
    @PostMapping("/{id}/delete")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deleteTeacher(id);
            redirectAttributes.addFlashAttribute("success", "教師刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/teachers";
    }
    
    // 上傳照片
    @PostMapping("/{id}/photo")
    public String uploadPhoto(@PathVariable Long id,
                             @RequestParam("photo") MultipartFile photo,
                             RedirectAttributes redirectAttributes) {
        try {
            teacherService.uploadPhoto(id, photo);
            redirectAttributes.addFlashAttribute("success", "照片上傳成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "照片上傳失敗：" + e.getMessage());
        }
        return "redirect:/teachers/" + id;
    }
    
    // 刪除照片
    @PostMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deletePhoto(id);
            redirectAttributes.addFlashAttribute("success", "照片刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "照片刪除失敗：" + e.getMessage());
        }
        return "redirect:/teachers/" + id;
    }
} 