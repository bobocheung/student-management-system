package com.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @NotNull(message = "平時成績不能為空")
    @Min(value = 0, message = "平時成績不能低於0")
    @Max(value = 100, message = "平時成績不能超過100")
    @Column(nullable = false)
    private Double regularScore;
    
    @NotNull(message = "考試成績不能為空")
    @Min(value = 0, message = "考試成績不能低於0")
    @Max(value = 100, message = "考試成績不能超過100")
    @Column(nullable = false)
    private Double examScore;
    
    @Min(value = 0, message = "總成績不能低於0")
    @Max(value = 100, message = "總成績不能超過100")
    private Double totalScore;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GradeType type = GradeType.NORMAL;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;
    
    @NotNull(message = "學年不能為空")
    @Column(nullable = false)
    private Integer academicYear;
    
    @Column(length = 1000)
    private String remarks;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotalScore();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotalScore();
    }
    
    private void calculateTotalScore() {
        if (regularScore != null && examScore != null) {
            // 平時成績佔30%，考試成績佔70%
            this.totalScore = regularScore * 0.3 + examScore * 0.7;
        }
    }
    
    // 枚舉定義
    public enum GradeType {
        NORMAL("正常"), MAKEUP("補考"), RETAKE("重修");
        
        private final String displayName;
        
        GradeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum Semester {
        FIRST("第一學期"), SECOND("第二學期"), SUMMER("暑期");
        
        private final String displayName;
        
        Semester(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Double getRegularScore() {
        return regularScore;
    }
    
    public void setRegularScore(Double regularScore) {
        this.regularScore = regularScore;
    }
    
    public Double getExamScore() {
        return examScore;
    }
    
    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }
    
    public Double getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }
    
    public GradeType getType() {
        return type;
    }
    
    public void setType(GradeType type) {
        this.type = type;
    }
    
    public Semester getSemester() {
        return semester;
    }
    
    public void setSemester(Semester semester) {
        this.semester = semester;
    }
    
    public Integer getAcademicYear() {
        return academicYear;
    }
    
    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 