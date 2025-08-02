package com.student.service;

import com.student.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    
    // 基本CRUD操作
    User saveUser(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findAllUsers();
    
    Page<User> findAllUsers(Pageable pageable);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    // 查詢操作
    List<User> findByRole(User.Role role);
    
    List<User> findByStatus(User.Status status);
    
    List<User> findByRoleAndStatus(User.Role role, User.Status status);
    
    List<User> findByKeyword(String keyword);
    
    // 分頁查詢
    Page<User> findByRole(User.Role role, Pageable pageable);
    
    Page<User> findByStatus(User.Status status, Pageable pageable);
    
    Page<User> findByUsernameContaining(String username, Pageable pageable);
    
    Page<User> findByEmailContaining(String email, Pageable pageable);
    
    // 高級搜索
    Page<User> findByAdvancedSearch(String username, String email, User.Role role, User.Status status, Pageable pageable);
    
    // 業務操作
    User createUser(User user, String rawPassword);
    
    User updatePassword(Long userId, String newPassword);
    
    User updateStatus(Long userId, User.Status status);
    
    User updateLastLoginTime(Long userId);
    
    // 驗證操作
    boolean validateUsername(String username);
    
    boolean validateEmail(String email);
    
    boolean validatePassword(String password);
} 