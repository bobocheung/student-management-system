package com.student.service.impl;

import com.student.entity.RewardPunishment;
import com.student.repository.RewardPunishmentRepository;
import com.student.service.RewardPunishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RewardPunishmentServiceImpl implements RewardPunishmentService {
    
    @Autowired
    private RewardPunishmentRepository rewardPunishmentRepository;
    
    @Override
    public RewardPunishment saveRewardPunishment(RewardPunishment rewardPunishment) {
        if (rewardPunishment.getId() == null) {
            rewardPunishment.setCreatedAt(LocalDateTime.now());
        }
        rewardPunishment.setUpdatedAt(LocalDateTime.now());
        return rewardPunishmentRepository.save(rewardPunishment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RewardPunishment> findById(Long id) {
        return rewardPunishmentRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findAllRewardPunishments() {
        return rewardPunishmentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RewardPunishment> findAllRewardPunishments(Pageable pageable) {
        return rewardPunishmentRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RewardPunishment> findByAdvancedSearch(String studentName, String title, 
                                                     RewardPunishment.Type type, 
                                                     RewardPunishment.Category category,
                                                     RewardPunishment.Status status, 
                                                     Pageable pageable) {
        return rewardPunishmentRepository.findByAdvancedSearch(studentName, title, type, category, status, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findByStudentId(Long studentId) {
        return rewardPunishmentRepository.findByStudentId(studentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findByType(RewardPunishment.Type type) {
        return rewardPunishmentRepository.findByType(type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findByCategory(RewardPunishment.Category category) {
        return rewardPunishmentRepository.findByCategory(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findByStatus(RewardPunishment.Status status) {
        return rewardPunishmentRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return rewardPunishmentRepository.findByDateBetween(startDate, endDate);
    }
    
    @Override
    public void deleteRewardPunishment(Long id) {
        rewardPunishmentRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countRewardPunishments() {
        return rewardPunishmentRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByType(RewardPunishment.Type type) {
        return rewardPunishmentRepository.countByType(type);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(RewardPunishment.Status status) {
        return rewardPunishmentRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByStudentIdAndType(Long studentId, RewardPunishment.Type type) {
        return rewardPunishmentRepository.countByStudentIdAndType(studentId, type);
    }
    
    @Override
    public RewardPunishment activateRewardPunishment(Long id) {
        Optional<RewardPunishment> rewardPunishmentOpt = findById(id);
        if (rewardPunishmentOpt.isPresent()) {
            RewardPunishment rewardPunishment = rewardPunishmentOpt.get();
            rewardPunishment.setStatus(RewardPunishment.Status.ACTIVE);
            return saveRewardPunishment(rewardPunishment);
        }
        throw new RuntimeException("獎懲記錄不存在");
    }
    
    @Override
    public RewardPunishment expireRewardPunishment(Long id) {
        Optional<RewardPunishment> rewardPunishmentOpt = findById(id);
        if (rewardPunishmentOpt.isPresent()) {
            RewardPunishment rewardPunishment = rewardPunishmentOpt.get();
            rewardPunishment.setStatus(RewardPunishment.Status.EXPIRED);
            return saveRewardPunishment(rewardPunishment);
        }
        throw new RuntimeException("獎懲記錄不存在");
    }
    
    @Override
    public RewardPunishment cancelRewardPunishment(Long id) {
        Optional<RewardPunishment> rewardPunishmentOpt = findById(id);
        if (rewardPunishmentOpt.isPresent()) {
            RewardPunishment rewardPunishment = rewardPunishmentOpt.get();
            rewardPunishment.setStatus(RewardPunishment.Status.CANCELLED);
            return saveRewardPunishment(rewardPunishment);
        }
        throw new RuntimeException("獎懲記錄不存在");
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> getRecentRewards(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("date").descending());
        return rewardPunishmentRepository.findByType(RewardPunishment.Type.REWARD, pageable).getContent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RewardPunishment> getRecentPunishments(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("date").descending());
        return rewardPunishmentRepository.findByType(RewardPunishment.Type.PUNISHMENT, pageable).getContent();
    }
    
    @Override
    @Transactional(readOnly = true)
    public double getRewardPunishmentRatio(Long studentId) {
        long rewardCount = countByStudentIdAndType(studentId, RewardPunishment.Type.REWARD);
        long punishmentCount = countByStudentIdAndType(studentId, RewardPunishment.Type.PUNISHMENT);
        
        if (punishmentCount == 0) {
            return rewardCount > 0 ? Double.MAX_VALUE : 0.0;
        }
        
        return (double) rewardCount / punishmentCount;
    }
}