package com.student.service.impl;

import com.student.entity.User;
import com.student.repository.UserRepository;
import com.student.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 更新最後登錄時間
            updateLastLoginTime(user.getId());
            return user;
        }
        throw new UsernameNotFoundException("用戶不存在: " + username);
    }
    
    @Override
    public User saveUser(User user) {
        // 驗證用戶名唯一性
        if (user.getId() == null && existsByUsername(user.getUsername())) {
            throw new RuntimeException("用戶名已存在");
        }
        
        // 驗證郵箱唯一性
        if (user.getId() == null && user.getEmail() != null && existsByEmail(user.getEmail())) {
            throw new RuntimeException("郵箱已存在");
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByStatus(User.Status status) {
        return userRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByRoleAndStatus(User.Role role, User.Status status) {
        return userRepository.findByRoleAndStatus(role, status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByKeyword(String keyword) {
        return userRepository.findByKeyword(keyword);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByRole(User.Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByStatus(User.Status status, Pageable pageable) {
        return userRepository.findByStatus(status, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByUsernameContaining(String username, Pageable pageable) {
        return userRepository.findByUsernameContaining(username, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByEmailContaining(String email, Pageable pageable) {
        return userRepository.findByEmailContaining(email, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<User> findByAdvancedSearch(String username, String email, User.Role role, User.Status status, Pageable pageable) {
        return userRepository.findByAdvancedSearch(username, email, role, status, pageable);
    }
    
    @Override
    public User createUser(User user, String rawPassword) {
        // 加密密碼
        user.setPassword(passwordEncoder.encode(rawPassword));
        return saveUser(user);
    }
    
    @Override
    public User updatePassword(Long userId, String newPassword) {
        Optional<User> userOpt = findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            return saveUser(user);
        }
        throw new RuntimeException("用戶不存在");
    }
    
    @Override
    public User updateStatus(Long userId, User.Status status) {
        Optional<User> userOpt = findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            return saveUser(user);
        }
        throw new RuntimeException("用戶不存在");
    }
    
    @Override
    public User updateLastLoginTime(Long userId) {
        Optional<User> userOpt = findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLoginTime(LocalDateTime.now());
            return saveUser(user);
        }
        throw new RuntimeException("用戶不存在");
    }
    
    @Override
    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        // 用戶名格式驗證：3-20位字母數字下劃線
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    @Override
    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // 郵箱格式驗證
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    @Override
    public boolean validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        // 密碼格式驗證：至少8位，包含字母和數字
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }
} 