package com.student.service;

import com.student.entity.RewardPunishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RewardPunishmentService {
    
    RewardPunishment saveRewardPunishment(RewardPunishment rewardPunishment);
    
    Optional<RewardPunishment> findById(Long id);
    
    List<RewardPunishment> findAllRewardPunishments();
    
    Page<RewardPunishment> findAllRewardPunishments(Pageable pageable);
    
    Page<RewardPunishment> findByAdvancedSearch(String studentName, String title, 
                                              RewardPunishment.Type type, 
                                              RewardPunishment.Category category,
                                              RewardPunishment.Status status, 
                                              Pageable pageable);
    
    List<RewardPunishment> findByStudentId(Long studentId);
    
    List<RewardPunishment> findByType(RewardPunishment.Type type);
    
    List<RewardPunishment> findByCategory(RewardPunishment.Category category);
    
    List<RewardPunishment> findByStatus(RewardPunishment.Status status);
    
    List<RewardPunishment> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    void deleteRewardPunishment(Long id);
    
    long countRewardPunishments();
    
    long countByType(RewardPunishment.Type type);
    
    long countByStatus(RewardPunishment.Status status);
    
    long countByStudentIdAndType(Long studentId, RewardPunishment.Type type);
    
    // 業務方法
    RewardPunishment activateRewardPunishment(Long id);
    
    RewardPunishment expireRewardPunishment(Long id);
    
    RewardPunishment cancelRewardPunishment(Long id);
    
    // 統計方法
    List<RewardPunishment> getRecentRewards(int limit);
    
    List<RewardPunishment> getRecentPunishments(int limit);
    
    double getRewardPunishmentRatio(Long studentId);
}