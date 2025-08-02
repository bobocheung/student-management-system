package com.student.config;

import com.student.entity.*;
import com.student.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private GradeService gradeService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private RewardPunishmentService rewardPunishmentService;
    
    private Random random = new Random();
    
    @Override
    public void run(String... args) throws Exception {
        // 初始化測試用戶
        initializeTestUsers();
        
        // 初始化演示數據（僅在沒有數據時執行）
        if (shouldInitializeDemoData()) {
            initializeDemoData();
        }
    }
    
    private boolean shouldInitializeDemoData() {
        try {
            return gradeService.findAllGrades().isEmpty() && 
                   attendanceService.findAllAttendance().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    private void initializeDemoData() {
        try {
            System.out.println("🚀 開始初始化演示數據...");
            
            List<Student> students = studentService.findAllStudents();
            List<Course> courses = courseService.findAllCourses();
            
            if (!students.isEmpty() && !courses.isEmpty()) {
                // 創建成績數據
                createGradeData(students, courses);
                
                // 創建考勤數據
                createAttendanceData(students, courses);
                
                // 創建獎懲數據
                createRewardPunishmentData(students);
                
                System.out.println("✅ 演示數據初始化完成！");
                printDataStatistics();
            }
        } catch (Exception e) {
            System.err.println("❌ 演示數據初始化失敗: " + e.getMessage());
        }
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
    
    private void createGradeData(List<Student> students, List<Course> courses) {
        System.out.println("📊 創建成績數據...");
        
        for (Student student : students) {
            for (Course course : courses) {
                if (random.nextBoolean()) { // 隨機為學生創建某些課程的成績
                    Grade grade = new Grade();
                    grade.setStudent(student);
                    grade.setCourse(course);
                    grade.setSemester(Grade.Semester.FIRST);
                    grade.setAcademicYear(2024);
                    
                    // 隨機生成成績
                    double dailyScore = 70 + random.nextInt(25); // 70-95
                    double examScore = 65 + random.nextInt(30);  // 65-95
                    double totalScore = dailyScore * 0.4 + examScore * 0.6;
                    
                    grade.setRegularScore(dailyScore);
                    grade.setExamScore(examScore);
                    grade.setTotalScore(totalScore);
                    
                    grade.setType(Grade.GradeType.NORMAL);
                    grade.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                    grade.setUpdatedAt(LocalDateTime.now());
                    
                    gradeService.saveGrade(grade);
                }
            }
        }
    }
    
    private void createAttendanceData(List<Student> students, List<Course> courses) {
        System.out.println("📅 創建考勤數據...");
        
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        
        for (Student student : students) {
            for (Course course : courses) {
                // 為每個學生在每門課程中創建一些考勤記錄
                for (int i = 0; i < 5 + random.nextInt(10); i++) {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(student);
                    attendance.setCourse(course);
                    
                    // 隨機日期
                    long randomDay = random.nextInt((int)(endDate.toEpochDay() - startDate.toEpochDay()));
                    attendance.setDate(startDate.plusDays(randomDay));
                    
                    // 隨機考勤狀態
                    int statusRandom = random.nextInt(100);
                    if (statusRandom < 80) {
                        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
                    } else if (statusRandom < 90) {
                        attendance.setStatus(Attendance.AttendanceStatus.LATE);
                    } else if (statusRandom < 95) {
                        attendance.setStatus(Attendance.AttendanceStatus.EARLY_LEAVE);
                    } else {
                        attendance.setStatus(Attendance.AttendanceStatus.ABSENT);
                    }
                    
                    // 設置簽到簽退時間
                    if (attendance.getStatus() != Attendance.AttendanceStatus.ABSENT) {
                        attendance.setCheckInTime(LocalTime.of(8 + random.nextInt(2), random.nextInt(60)));
                        attendance.setCheckOutTime(LocalTime.of(16 + random.nextInt(2), random.nextInt(60)));
                    }
                    
                    attendance.setMethod(Attendance.AttendanceMethod.MANUAL);
                    attendance.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                    attendance.setUpdatedAt(LocalDateTime.now());
                    
                    attendanceService.saveAttendance(attendance);
                }
            }
        }
    }
    
    private void createRewardPunishmentData(List<Student> students) {
        System.out.println("🏆 創建獎懲數據...");
        
        String[] rewardTypes = {"獎學金", "三好學生", "優秀學生幹部", "學習進步獎", "文體活動獎"};
        String[] punishmentTypes = {"警告", "嚴重警告", "記過", "留校察看"};
        
        for (Student student : students) {
            // 隨機為部分學生創建獎懲記錄
            if (random.nextInt(100) < 60) { // 60%的學生有記錄
                RewardPunishment record = new RewardPunishment();
                record.setStudent(student);
                
                if (random.nextBoolean()) {
                    // 獎勵
                    record.setType(RewardPunishment.Type.REWARD);
                    record.setTitle(rewardTypes[random.nextInt(rewardTypes.length)]);
                    record.setDescription("表現優秀，特予表揚");
                    // 隨機選擇獎勵類別
                    RewardPunishment.Category[] rewardCategories = {
                        RewardPunishment.Category.SCHOLARSHIP,
                        RewardPunishment.Category.HONOR_CERTIFICATE,
                        RewardPunishment.Category.EXCELLENT_STUDENT,
                        RewardPunishment.Category.SPORTS_AWARD
                    };
                    record.setCategory(rewardCategories[random.nextInt(rewardCategories.length)]);
                } else {
                    // 懲罰
                    record.setType(RewardPunishment.Type.PUNISHMENT);
                    record.setTitle(punishmentTypes[random.nextInt(punishmentTypes.length)]);
                    record.setDescription("違反校規，特予處分");
                    // 隨機選擇懲罰類別
                    RewardPunishment.Category[] punishmentCategories = {
                        RewardPunishment.Category.WARNING,
                        RewardPunishment.Category.SERIOUS_WARNING,
                        RewardPunishment.Category.DEMERIT
                    };
                    record.setCategory(punishmentCategories[random.nextInt(punishmentCategories.length)]);
                }
                
                record.setDate(LocalDate.now().minusDays(random.nextInt(90)));
                record.setIssuer("學務處");
                record.setStatus(RewardPunishment.Status.ACTIVE);
                record.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                record.setUpdatedAt(LocalDateTime.now());
                
                rewardPunishmentService.saveRewardPunishment(record);
            }
        }
    }
    
    private void printDataStatistics() {
        try {
            long totalStudents = studentService.findAllStudents().size();
            long totalTeachers = teacherService.countTeachers();
            long totalClasses = classService.findAllClasses().size();
            long totalCourses = courseService.findAllCourses().size();
            long totalGrades = gradeService.findAllGrades().size();
            long totalAttendances = attendanceService.findAllAttendance().size();
            long totalRewards = rewardPunishmentService.findAllRewardPunishments().size();
            
            System.out.println("📊 數據統計：");
            System.out.println("   - 用戶：6個");
            System.out.println("   - 教師：" + totalTeachers + "個");
            System.out.println("   - 班級：" + totalClasses + "個");
            System.out.println("   - 課程：" + totalCourses + "個");
            System.out.println("   - 學生：" + totalStudents + "個");
            System.out.println("   - 成績記錄：" + totalGrades + "條");
            System.out.println("   - 考勤記錄：" + totalAttendances + "條");
            System.out.println("   - 獎懲記錄：" + totalRewards + "條");
        } catch (Exception e) {
            System.err.println("統計數據時發生錯誤: " + e.getMessage());
        }
    }
} 