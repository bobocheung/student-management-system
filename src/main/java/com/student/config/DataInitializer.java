package com.student.config;

import com.student.entity.User;
import com.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void run(String... args) throws Exception {
        // 初始化測試用戶
        initializeTestUsers();
    }
    
    private void initializeTestUsers() {
        // 檢查是否已存在管理員用戶
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@school.com");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ACTIVE);
            userService.createUser(admin, "admin123");
            System.out.println("管理員用戶已創建: admin / admin123");
        }
        
        // 檢查是否已存在教師用戶
        if (!userService.existsByUsername("teacher")) {
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setEmail("teacher@school.com");
            teacher.setRole(User.Role.TEACHER);
            teacher.setStatus(User.Status.ACTIVE);
            userService.createUser(teacher, "teacher123");
            System.out.println("教師用戶已創建: teacher / teacher123");
        }
        
        // 檢查是否已存在學生用戶
        if (!userService.existsByUsername("student")) {
            User student = new User();
            student.setUsername("student");
            student.setEmail("student@school.com");
            student.setRole(User.Role.STUDENT);
            student.setStatus(User.Status.ACTIVE);
            userService.createUser(student, "student123");
            System.out.println("學生用戶已創建: student / student123");
        }
    }
} 