package com.student.repository;

import com.student.entity.RewardPunishment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardPunishmentRepository extends JpaRepository<RewardPunishment, Long> {
    
    List<RewardPunishment> findByStudentId(Long studentId);
    
    List<RewardPunishment> findByType(RewardPunishment.Type type);
    
    List<RewardPunishment> findByCategory(RewardPunishment.Category category);
    
    List<RewardPunishment> findByStatus(RewardPunishment.Status status);
    
    List<RewardPunishment> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.student.name LIKE %:studentName%")
    Page<RewardPunishment> findByStudentNameContaining(@Param("studentName") String studentName, Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.title LIKE %:title%")
    Page<RewardPunishment> findByTitleContaining(@Param("title") String title, Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.type = :type")
    Page<RewardPunishment> findByType(@Param("type") RewardPunishment.Type type, Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.category = :category")
    Page<RewardPunishment> findByCategory(@Param("category") RewardPunishment.Category category, Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.status = :status")
    Page<RewardPunishment> findByStatus(@Param("status") RewardPunishment.Status status, Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE rp.date >= :startDate AND rp.date <= :endDate")
    Page<RewardPunishment> findByDateBetween(@Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate, 
                                           Pageable pageable);
    
    @Query("SELECT rp FROM RewardPunishment rp WHERE " +
           "(:studentName IS NULL OR rp.student.name LIKE %:studentName%) AND " +
           "(:title IS NULL OR rp.title LIKE %:title%) AND " +
           "(:type IS NULL OR rp.type = :type) AND " +
           "(:category IS NULL OR rp.category = :category) AND " +
           "(:status IS NULL OR rp.status = :status)")
    Page<RewardPunishment> findByAdvancedSearch(@Param("studentName") String studentName,
                                              @Param("title") String title,
                                              @Param("type") RewardPunishment.Type type,
                                              @Param("category") RewardPunishment.Category category,
                                              @Param("status") RewardPunishment.Status status,
                                              Pageable pageable);
    
    long countByType(RewardPunishment.Type type);
    
    long countByStatus(RewardPunishment.Status status);
    
    @Query("SELECT COUNT(rp) FROM RewardPunishment rp WHERE rp.student.id = :studentId AND rp.type = :type")
    long countByStudentIdAndType(@Param("studentId") Long studentId, @Param("type") RewardPunishment.Type type);
}