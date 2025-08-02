package com.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object exception = request.getAttribute("javax.servlet.error.exception");
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            switch (statusCode) {
                case 404:
                    model.addAttribute("errorCode", "404");
                    model.addAttribute("errorMessage", "頁面未找到");
                    model.addAttribute("errorDescription", "您請求的頁面不存在或已被移動。");
                    return "error/404";
                case 403:
                    model.addAttribute("errorCode", "403");
                    model.addAttribute("errorMessage", "訪問被拒絕");
                    model.addAttribute("errorDescription", "您沒有權限訪問此頁面。");
                    return "error/403";
                case 500:
                    model.addAttribute("errorCode", "500");
                    model.addAttribute("errorMessage", "服務器錯誤");
                    model.addAttribute("errorDescription", "服務器內部發生錯誤，請稍後再試。");
                    return "error/500";
                default:
                    model.addAttribute("errorCode", statusCode);
                    model.addAttribute("errorMessage", "發生錯誤");
                    model.addAttribute("errorDescription", "發生未知錯誤，請稍後再試。");
                    return "error/error";
            }
        }
        
        model.addAttribute("errorCode", "未知");
        model.addAttribute("errorMessage", "發生錯誤");
        model.addAttribute("errorDescription", "發生未知錯誤，請稍後再試。");
        return "error/error";
    }
} 