package com.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "課程代碼不能為空")
    @Column(unique = true, nullable = false)
    private String courseCode;
    
    @NotBlank(message = "課程名稱不能為空")
    @Column(nullable = false)
    private String name;
    
    @NotNull(message = "學分不能為空")
    @Min(value = 1, message = "學分至少為1")
    @Max(value = 20, message = "學分不能超過20")
    @Column(nullable = false)
    private Integer credits;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseType type = CourseType.REQUIRED;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.ACTIVE;
    
    @Column(length = 1000)
    private String syllabus;
    
    @Column(length = 1000)
    private String textbook;
    
    // 關聯關係
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CourseSchedule> schedules;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Grade> grades;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // 枚舉定義
    public enum CourseType {
        REQUIRED("必修"), ELECTIVE("選修"), GENERAL("通識");
        
        private final String displayName;
        
        CourseType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum CourseStatus {
        ACTIVE("開設"), INACTIVE("停開"), SUSPENDED("暫停");
        
        private final String displayName;
        
        CourseStatus(String displayName) {
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
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CourseType getType() {
        return type;
    }
    
    public void setType(CourseType type) {
        this.type = type;
    }
    
    public CourseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CourseStatus status) {
        this.status = status;
    }
    
    public String getSyllabus() {
        return syllabus;
    }
    
    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
    
    public String getTextbook() {
        return textbook;
    }
    
    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }
    
    public List<CourseSchedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(List<CourseSchedule> schedules) {
        this.schedules = schedules;
    }
    
    public List<Grade> getGrades() {
        return grades;
    }
    
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
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