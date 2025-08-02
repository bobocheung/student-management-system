package com.student.repository;

import com.student.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    List<User> findByRole(User.Role role);
    
    List<User> findByStatus(User.Status status);
    
    List<User> findByRoleAndStatus(User.Role role, User.Status status);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginTime >= :startTime AND u.lastLoginTime <= :endTime")
    List<User> findByLastLoginTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                        @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT u FROM User u WHERE u.role = :role")
    Page<User> findByRole(@Param("role") User.Role role, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.status = :status")
    Page<User> findByStatus(@Param("status") User.Status status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    Page<User> findByUsernameContaining(@Param("username") String username, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.email LIKE %:email%")
    Page<User> findByEmailContaining(@Param("email") String email, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startTime AND u.createdAt <= :endTime")
    Page<User> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, 
                                     @Param("endTime") LocalDateTime endTime, 
                                     Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE " +
           "(:username IS NULL OR u.username LIKE %:username%) AND " +
           "(:email IS NULL OR u.email LIKE %:email%) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:status IS NULL OR u.status = :status)")
    Page<User> findByAdvancedSearch(@Param("username") String username,
                                   @Param("email") String email,
                                   @Param("role") User.Role role,
                                   @Param("status") User.Status status,
                                   Pageable pageable);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
} 