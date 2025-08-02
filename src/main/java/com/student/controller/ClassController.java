package com.student.controller;

import com.student.entity.Class;

import com.student.service.ClassService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/classes")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class ClassController {
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private TeacherService teacherService;
    
    // 班級列表頁面
    @GetMapping
    public String listClasses(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String code,
                             @RequestParam(required = false) Integer grade,
                             @RequestParam(required = false) Class.ClassStatus status,
                             Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Class> classes;
        if (name != null || code != null || grade != null || status != null) {
            classes = classService.findByAdvancedSearch(name, code, grade, status, pageable);
        } else {
            classes = classService.findAllClasses(pageable);
        }
        
        model.addAttribute("classes", classes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", classes.getTotalPages());
        model.addAttribute("totalItems", classes.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchName", name);
        model.addAttribute("searchCode", code);
        model.addAttribute("searchGrade", grade);
        model.addAttribute("searchStatus", status);
        
        return "class/list";
    }
    
    // 班級詳情頁面
    @GetMapping("/{id}")
    public String viewClass(@PathVariable Long id, Model model) {
        Optional<Class> classOpt = classService.findById(id);
        if (classOpt.isPresent()) {
            model.addAttribute("class", classOpt.get());
            return "class/view";
        }
        return "redirect:/classes";
    }
    
    // 新增班級頁面
    @GetMapping("/new")
    public String newClassForm(Model model) {
        model.addAttribute("class", new Class());
        model.addAttribute("teachers", teacherService.findAllActiveTeachers());
        model.addAttribute("statuses", Class.ClassStatus.values());
        return "class/form";
    }
    
    // 編輯班級頁面
    @GetMapping("/{id}/edit")
    public String editClassForm(@PathVariable Long id, Model model) {
        Optional<Class> classOpt = classService.findById(id);
        if (classOpt.isPresent()) {
            model.addAttribute("class", classOpt.get());
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Class.ClassStatus.values());
            return "class/form";
        }
        return "redirect:/classes";
    }
    
    // 保存班級
    @PostMapping
    public String saveClass(@Valid @ModelAttribute("class") Class classEntity,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Class.ClassStatus.values());
            return "class/form";
        }
        
        try {
            Class savedClass = classService.saveClass(classEntity);
            redirectAttributes.addFlashAttribute("success", "班級信息保存成功");
            return "redirect:/classes/" + savedClass.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("teachers", teacherService.findAllActiveTeachers());
            model.addAttribute("statuses", Class.ClassStatus.values());
            return "class/form";
        }
    }
    
    // 刪除班級
    @PostMapping("/{id}/delete")
    public String deleteClass(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            classService.deleteClass(id);
            redirectAttributes.addFlashAttribute("success", "班級刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/classes";
    }
    
    // API接口 - 獲取班級列表
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<Class>> getClassesApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer grade,
            @RequestParam(required = false) Class.ClassStatus status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Class> classes;
        if (name != null || code != null || grade != null || status != null) {
            classes = classService.findByAdvancedSearch(name, code, grade, status, pageable);
        } else {
            classes = classService.findAllClasses(pageable);
        }
        
        return ResponseEntity.ok(classes);
    }
    
    // API接口 - 獲取班級詳情
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Class> getClassApi(@PathVariable Long id) {
        Optional<Class> classOpt = classService.findById(id);
        return classOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 