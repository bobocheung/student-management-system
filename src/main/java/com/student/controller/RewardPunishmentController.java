package com.student.controller;

import com.student.entity.RewardPunishment;
import com.student.service.RewardPunishmentService;
import com.student.service.StudentService;
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

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/rewards")
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class RewardPunishmentController {
    
    @Autowired
    private RewardPunishmentService rewardPunishmentService;
    
    @Autowired
    private StudentService studentService;
    
    // 獎懲列表頁面
    @GetMapping
    public String listRewardPunishments(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String studentName,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false) RewardPunishment.Type type,
                                      @RequestParam(required = false) RewardPunishment.Category category,
                                      @RequestParam(required = false) RewardPunishment.Status status,
                                      Model model) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        
        Page<RewardPunishment> rewardPunishments;
        if (studentName != null || title != null || type != null || category != null || status != null) {
            rewardPunishments = rewardPunishmentService.findByAdvancedSearch(studentName, title, type, category, status, pageable);
        } else {
            rewardPunishments = rewardPunishmentService.findAllRewardPunishments(pageable);
        }
        
        model.addAttribute("rewardPunishments", rewardPunishments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rewardPunishments.getTotalPages());
        model.addAttribute("totalItems", rewardPunishments.getTotalElements());
        
        // 添加搜索條件到模型
        model.addAttribute("searchStudentName", studentName);
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchType", type);
        model.addAttribute("searchCategory", category);
        model.addAttribute("searchStatus", status);
        
        // 添加枚舉值到模型
        model.addAttribute("types", RewardPunishment.Type.values());
        model.addAttribute("categories", RewardPunishment.Category.values());
        model.addAttribute("statuses", RewardPunishment.Status.values());
        
        return "rewards/list";
    }
    
    // 獎懲詳情頁面
    @GetMapping("/{id}")
    public String viewRewardPunishment(@PathVariable Long id, Model model) {
        Optional<RewardPunishment> rewardPunishmentOpt = rewardPunishmentService.findById(id);
        if (rewardPunishmentOpt.isPresent()) {
            model.addAttribute("rewardPunishment", rewardPunishmentOpt.get());
            return "rewards/view";
        }
        return "redirect:/rewards";
    }
    
    // 新增獎懲頁面
    @GetMapping("/new")
    public String newRewardPunishmentForm(Model model) {
        model.addAttribute("rewardPunishment", new RewardPunishment());
        model.addAttribute("students", studentService.findAllStudents());
        model.addAttribute("types", RewardPunishment.Type.values());
        model.addAttribute("categories", RewardPunishment.Category.values());
        model.addAttribute("statuses", RewardPunishment.Status.values());
        return "rewards/form";
    }
    
    // 編輯獎懲頁面
    @GetMapping("/{id}/edit")
    public String editRewardPunishmentForm(@PathVariable Long id, Model model) {
        Optional<RewardPunishment> rewardPunishmentOpt = rewardPunishmentService.findById(id);
        if (rewardPunishmentOpt.isPresent()) {
            model.addAttribute("rewardPunishment", rewardPunishmentOpt.get());
            model.addAttribute("students", studentService.findAllStudents());
            model.addAttribute("types", RewardPunishment.Type.values());
            model.addAttribute("categories", RewardPunishment.Category.values());
            model.addAttribute("statuses", RewardPunishment.Status.values());
            return "rewards/form";
        }
        return "redirect:/rewards";
    }
    
    // 保存獎懲
    @PostMapping
    public String saveRewardPunishment(@Valid @ModelAttribute RewardPunishment rewardPunishment,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.findAllStudents());
            model.addAttribute("types", RewardPunishment.Type.values());
            model.addAttribute("categories", RewardPunishment.Category.values());
            model.addAttribute("statuses", RewardPunishment.Status.values());
            return "rewards/form";
        }
        
        try {
            RewardPunishment savedRewardPunishment = rewardPunishmentService.saveRewardPunishment(rewardPunishment);
            redirectAttributes.addFlashAttribute("success", "獎懲記錄保存成功");
            return "redirect:/rewards/" + savedRewardPunishment.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("students", studentService.findAllStudents());
            model.addAttribute("types", RewardPunishment.Type.values());
            model.addAttribute("categories", RewardPunishment.Category.values());
            model.addAttribute("statuses", RewardPunishment.Status.values());
            return "rewards/form";
        }
    }
    
    // 刪除獎懲
    @PostMapping("/{id}/delete")
    public String deleteRewardPunishment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rewardPunishmentService.deleteRewardPunishment(id);
            redirectAttributes.addFlashAttribute("success", "獎懲記錄刪除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/rewards";
    }
    
    // 激活獎懲
    @PostMapping("/{id}/activate")
    public String activateRewardPunishment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rewardPunishmentService.activateRewardPunishment(id);
            redirectAttributes.addFlashAttribute("success", "獎懲記錄已激活");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "激活失敗：" + e.getMessage());
        }
        return "redirect:/rewards/" + id;
    }
    
    // 過期獎懲
    @PostMapping("/{id}/expire")
    public String expireRewardPunishment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rewardPunishmentService.expireRewardPunishment(id);
            redirectAttributes.addFlashAttribute("success", "獎懲記錄已設為過期");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失敗：" + e.getMessage());
        }
        return "redirect:/rewards/" + id;
    }
    
    // 取消獎懲
    @PostMapping("/{id}/cancel")
    public String cancelRewardPunishment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rewardPunishmentService.cancelRewardPunishment(id);
            redirectAttributes.addFlashAttribute("success", "獎懲記錄已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失敗：" + e.getMessage());
        }
        return "redirect:/rewards/" + id;
    }
    
    // API接口 - 獲取獎懲列表
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<Page<RewardPunishment>> getRewardPunishmentsApi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) RewardPunishment.Type type,
            @RequestParam(required = false) RewardPunishment.Category category,
            @RequestParam(required = false) RewardPunishment.Status status) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        
        Page<RewardPunishment> rewardPunishments;
        if (studentName != null || title != null || type != null || category != null || status != null) {
            rewardPunishments = rewardPunishmentService.findByAdvancedSearch(studentName, title, type, category, status, pageable);
        } else {
            rewardPunishments = rewardPunishmentService.findAllRewardPunishments(pageable);
        }
        
        return ResponseEntity.ok(rewardPunishments);
    }
    
    // API接口 - 獲取獎懲詳情
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<RewardPunishment> getRewardPunishmentApi(@PathVariable Long id) {
        Optional<RewardPunishment> rewardPunishmentOpt = rewardPunishmentService.findById(id);
        return rewardPunishmentOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}