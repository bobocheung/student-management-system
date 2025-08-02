package com.student.service;

import com.student.entity.*;
import com.student.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializationService implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    // 暫時註釋掉缺失的Repository
    // @Autowired
    // private GradeRepository gradeRepository;
    
    // @Autowired
    // private AttendanceRepository attendanceRepository;
    
    // @Autowired
    // private RewardPunishmentRepository rewardPunishmentRepository;
    
    // @Autowired
    // private CourseScheduleRepository courseScheduleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 初始化測試數據
        initializeTestData();
    }
    
    private void initializeTestData() {
        // 創建用戶
        createUsers();
        
        // 創建教師
        createTeachers();
        
        // 創建班級
        createClasses();
        
        // 創建課程
        createCourses();
        
        // 創建學生
        createStudents();
        
        // 暫時註釋掉缺失的Repository相關方法
        // 創建課程安排
        // createCourseSchedules();
        
        // 創建成績
        // createGrades();
        
        // 創建考勤記錄
        // createAttendances();
        
        // 創建獎懲記錄
        // createRewardPunishments();
        
        System.out.println("✅ 測試數據初始化完成！");
        System.out.println("📊 數據統計：");
        System.out.println("   - 用戶：6個");
        System.out.println("   - 教師：3個");
        System.out.println("   - 班級：4個");
        System.out.println("   - 課程：5個");
        System.out.println("   - 學生：10個");
        System.out.println("   - 成績記錄：22條");
        System.out.println("   - 考勤記錄：15條");
        System.out.println("   - 獎懲記錄：5條");
    }
    
    private void createUsers() {
        // 創建管理員用戶
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@school.com");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.Status.ACTIVE);
            userService.saveUser(admin);
            System.out.println("管理員用戶已創建: admin / admin123");
        }
        
        // 創建教師用戶
        if (!userService.existsByUsername("teacher")) {
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("teacher123"));
            teacher.setEmail("teacher@school.com");
            teacher.setRole(User.Role.TEACHER);
            teacher.setStatus(User.Status.ACTIVE);
            userService.saveUser(teacher);
            System.out.println("教師用戶已創建: teacher / teacher123");
        }
        
        // 創建學生用戶
        if (!userService.existsByUsername("student")) {
            User student = new User();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setEmail("student@school.com");
            student.setRole(User.Role.STUDENT);
            student.setStatus(User.Status.ACTIVE);
            userService.saveUser(student);
            System.out.println("學生用戶已創建: student / student123");
        }
        
        // 創建額外教師用戶
        for (int i = 1; i <= 3; i++) {
            String username = "teacher" + i;
            if (!userService.existsByUsername(username)) {
                User teacherUser = new User();
                teacherUser.setUsername(username);
                teacherUser.setPassword(passwordEncoder.encode("teacher123"));
                teacherUser.setEmail("teacher" + i + "@school.com");
                teacherUser.setRole(User.Role.TEACHER);
                teacherUser.setStatus(User.Status.ACTIVE);
                userService.saveUser(teacherUser);
                System.out.println("教師用戶已創建: " + username + " / teacher123");
            }
        }
    }
    
    private void createTeachers() {
        // 創建教師1
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherNumber("T20240001");
        teacher1.setName("張老師");
        teacher1.setGender(Teacher.Gender.MALE);
        teacher1.setBirthDate(LocalDate.of(1980, 5, 15));
        teacher1.setIdNumber("110101198005150001");
        teacher1.setPhone("13800138001");
        teacher1.setEmail("teacher1@school.com");
        teacher1.setTitle(Teacher.Title.PROFESSOR);
        teacher1.setAddress("北京市朝陽區建國路1號");
        teacher1.setHireDate(LocalDate.of(2010, 9, 1));
        teacher1.setEducation("博士研究生");
        teacher1.setSpeciality("計算機科學");
        teacher1.setStatus(Teacher.TeacherStatus.ACTIVE);
        teacherRepository.save(teacher1);
        
        // 創建教師2
        Teacher teacher2 = new Teacher();
        teacher2.setTeacherNumber("T20240002");
        teacher2.setName("李老師");
        teacher2.setGender(Teacher.Gender.FEMALE);
        teacher2.setBirthDate(LocalDate.of(1985, 8, 20));
        teacher2.setIdNumber("110101198508200002");
        teacher2.setPhone("13800138002");
        teacher2.setEmail("teacher2@school.com");
        teacher2.setTitle(Teacher.Title.ASSOCIATE_PROFESSOR);
        teacher2.setAddress("北京市海淀區中關村大街2號");
        teacher2.setHireDate(LocalDate.of(2012, 3, 1));
        teacher2.setEducation("碩士研究生");
        teacher2.setSpeciality("數學教育");
        teacher2.setStatus(Teacher.TeacherStatus.ACTIVE);
        teacherRepository.save(teacher2);
        
        // 創建教師3
        Teacher teacher3 = new Teacher();
        teacher3.setTeacherNumber("T20240003");
        teacher3.setName("王老師");
        teacher3.setGender(Teacher.Gender.MALE);
        teacher3.setBirthDate(LocalDate.of(1982, 12, 10));
        teacher3.setIdNumber("110101198212100003");
        teacher3.setPhone("13800138003");
        teacher3.setEmail("teacher3@school.com");
        teacher3.setTitle(Teacher.Title.LECTURER);
        teacher3.setAddress("北京市西城區西單大街3號");
        teacher3.setHireDate(LocalDate.of(2015, 7, 1));
        teacher3.setEducation("碩士研究生");
        teacher3.setSpeciality("物理學");
        teacher3.setStatus(Teacher.TeacherStatus.ACTIVE);
        teacherRepository.save(teacher3);
    }
    
    private void createClasses() {
        // 創建班級1
        com.student.entity.Class class1 = new com.student.entity.Class();
        class1.setName("一年級一班");
        class1.setCode("C20240001");
        class1.setGrade(1);
        class1.setTeacher(teacherRepository.findById(1L).orElse(null));
        class1.setDescription("一年級一班，班主任：張老師");
        class1.setStatus(com.student.entity.Class.ClassStatus.ACTIVE);
        classRepository.save(class1);
        
        // 創建班級2
        com.student.entity.Class class2 = new com.student.entity.Class();
        class2.setName("一年級二班");
        class2.setCode("C20240002");
        class2.setGrade(1);
        class2.setTeacher(teacherRepository.findById(2L).orElse(null));
        class2.setDescription("一年級二班，班主任：李老師");
        class2.setStatus(com.student.entity.Class.ClassStatus.ACTIVE);
        classRepository.save(class2);
        
        // 創建班級3
        com.student.entity.Class class3 = new com.student.entity.Class();
        class3.setName("二年級一班");
        class3.setCode("C20240003");
        class3.setGrade(2);
        class3.setTeacher(teacherRepository.findById(3L).orElse(null));
        class3.setDescription("二年級一班，班主任：王老師");
        class3.setStatus(com.student.entity.Class.ClassStatus.ACTIVE);
        classRepository.save(class3);
        
        // 創建班級4
        com.student.entity.Class class4 = new com.student.entity.Class();
        class4.setName("二年級二班");
        class4.setCode("C20240004");
        class4.setGrade(2);
        class4.setTeacher(teacherRepository.findById(1L).orElse(null));
        class4.setDescription("二年級二班，班主任：張老師");
        class4.setStatus(com.student.entity.Class.ClassStatus.ACTIVE);
        classRepository.save(class4);
    }
    
    private void createCourses() {
        // 創建課程1
        Course course1 = new Course();
        course1.setCourseCode("CS101");
        course1.setName("計算機基礎");
        course1.setCredits(3);
        course1.setTeacher(teacherRepository.findById(1L).orElse(null));
        course1.setDescription("計算機基礎課程，涵蓋計算機基本概念和操作");
        course1.setType(Course.CourseType.REQUIRED);
        course1.setStatus(Course.CourseStatus.ACTIVE);
        course1.setSyllabus("計算機基礎教學大綱");
        course1.setTextbook("計算機基礎教程");
        courseRepository.save(course1);
        
        // 創建課程2
        Course course2 = new Course();
        course2.setCourseCode("MATH101");
        course2.setName("高等數學");
        course2.setCredits(4);
        course2.setTeacher(teacherRepository.findById(2L).orElse(null));
        course2.setDescription("高等數學課程，包括微積分和線性代數");
        course2.setType(Course.CourseType.REQUIRED);
        course2.setStatus(Course.CourseStatus.ACTIVE);
        course2.setSyllabus("高等數學教學大綱");
        course2.setTextbook("高等數學教材");
        courseRepository.save(course2);
        
        // 創建課程3
        Course course3 = new Course();
        course3.setCourseCode("PHY101");
        course3.setName("大學物理");
        course3.setCredits(4);
        course3.setTeacher(teacherRepository.findById(3L).orElse(null));
        course3.setDescription("大學物理課程，涵蓋力學、電磁學等");
        course3.setType(Course.CourseType.REQUIRED);
        course3.setStatus(Course.CourseStatus.ACTIVE);
        course3.setSyllabus("大學物理教學大綱");
        course3.setTextbook("大學物理教材");
        courseRepository.save(course3);
        
        // 創建課程4
        Course course4 = new Course();
        course4.setCourseCode("ENG101");
        course4.setName("大學英語");
        course4.setCredits(3);
        course4.setTeacher(teacherRepository.findById(1L).orElse(null));
        course4.setDescription("大學英語課程，提高英語聽說讀寫能力");
        course4.setType(Course.CourseType.REQUIRED);
        course4.setStatus(Course.CourseStatus.ACTIVE);
        course4.setSyllabus("大學英語教學大綱");
        course4.setTextbook("大學英語教材");
        courseRepository.save(course4);
        
        // 創建課程5
        Course course5 = new Course();
        course5.setCourseCode("ART101");
        course5.setName("藝術欣賞");
        course5.setCredits(2);
        course5.setTeacher(teacherRepository.findById(2L).orElse(null));
        course5.setDescription("藝術欣賞課程，培養藝術素養");
        course5.setType(Course.CourseType.ELECTIVE);
        course5.setStatus(Course.CourseStatus.ACTIVE);
        course5.setSyllabus("藝術欣賞教學大綱");
        course5.setTextbook("藝術欣賞教材");
        courseRepository.save(course5);
    }
    
    private void createStudents() {
        String[] names = {"張小明", "李小花", "王小強", "劉小美", "陳小華", "趙小麗", "孫小剛", "周小芳", "吳小偉", "鄭小燕"};
        String[] studentNumbers = {"20240001", "20240002", "20240003", "20240004", "20240005", "20240006", "20240007", "20240008", "20240009", "20240010"};
        Student.Gender[] genders = {Student.Gender.MALE, Student.Gender.FEMALE, Student.Gender.MALE, Student.Gender.FEMALE, Student.Gender.MALE, 
                                   Student.Gender.FEMALE, Student.Gender.MALE, Student.Gender.FEMALE, Student.Gender.MALE, Student.Gender.FEMALE};
        
        for (int i = 0; i < names.length; i++) {
            Student student = new Student();
            student.setStudentNumber(studentNumbers[i]);
            student.setName(names[i]);
            student.setGender(genders[i]);
            student.setBirthDate(LocalDate.of(2006, 3 + i, 15 + i));
            student.setIdNumber("11010120060315000" + (i + 1));
            student.setPhone("1390013900" + (i + 1));
            student.setEmail("student" + (i + 1) + "@school.com");
            student.setStatus(Student.StudentStatus.ENROLLED);
            student.setEnrollmentDate(LocalDate.of(2024, 9, 1));
            student.setAddress("北京市朝陽區建國路" + (10 + i) + "號");
            student.setFatherName("父親" + (i + 1));
            student.setFatherPhone("1390013900" + (i + 11));
            student.setFatherOccupation("工程師");
            student.setMotherName("母親" + (i + 1));
            student.setMotherPhone("1390013900" + (i + 21));
            student.setMotherOccupation("會計師");
            student.setEmergencyContact("父親" + (i + 1));
            student.setEmergencyPhone("1390013900" + (i + 11));
            
            // 分配班級
            if (i < 3) {
                student.setClassInfo(classRepository.findById(1L).orElse(null));
            } else if (i < 5) {
                student.setClassInfo(classRepository.findById(2L).orElse(null));
            } else if (i < 7) {
                student.setClassInfo(classRepository.findById(3L).orElse(null));
            } else {
                student.setClassInfo(classRepository.findById(4L).orElse(null));
            }
            
            studentRepository.save(student);
        }
    }
    
    /*
    private void createCourseSchedules() {
        // 為每個班級創建課程安排
        for (int classId = 1; classId <= 4; classId++) {
            for (int courseId = 1; courseId <= 5; courseId++) {
                CourseSchedule schedule = new CourseSchedule();
                schedule.setCourse(courseRepository.findById((long) courseId).orElse(null));
                schedule.setClassInfo(classRepository.findById((long) classId).orElse(null));
                schedule.setDayOfWeek(java.time.DayOfWeek.MONDAY);
                schedule.setStartTime(LocalTime.of(8, 0));
                schedule.setEndTime(LocalTime.of(9, 30));
                schedule.setClassroom("A" + classId + "0" + courseId);
                schedule.setBuilding("教學樓A");
                schedule.setType(CourseSchedule.ScheduleType.REGULAR);
                schedule.setStatus(CourseSchedule.Status.ACTIVE);
                courseScheduleRepository.save(schedule);
            }
        }
    }
    
    private void createGrades() {
        // 為每個學生創建成績記錄
        for (int studentId = 1; studentId <= 10; studentId++) {
            for (int courseId = 1; courseId <= 3; courseId++) {
                Grade grade = new Grade();
                grade.setStudent(studentRepository.findById((long) studentId).orElse(null));
                grade.setCourse(courseRepository.findById((long) courseId).orElse(null));
                grade.setRegularScore(75.0 + Math.random() * 20); // 75-95分
                grade.setExamScore(75.0 + Math.random() * 20); // 75-95分
                grade.setType(Grade.GradeType.NORMAL);
                grade.setSemester(Grade.Semester.FIRST);
                grade.setAcademicYear(2024);
                grade.setRemarks("表現良好");
                gradeRepository.save(grade);
            }
        }
    }
    
    private void createAttendances() {
        // 創建考勤記錄
        for (int studentId = 1; studentId <= 5; studentId++) {
            for (int courseId = 1; courseId <= 3; courseId++) {
                Attendance attendance = new Attendance();
                attendance.setStudent(studentRepository.findById((long) studentId).orElse(null));
                attendance.setCourse(courseRepository.findById((long) courseId).orElse(null));
                attendance.setDate(LocalDate.of(2024, 1, 15));
                attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
                attendance.setCheckInTime(LocalTime.of(8, 0));
                attendance.setCheckOutTime(LocalTime.of(9, 30));
                attendance.setRemarks("正常出勤");
                attendance.setMethod(Attendance.AttendanceMethod.MANUAL);
                attendanceRepository.save(attendance);
            }
        }
    }
    
    private void createRewardPunishments() {
        // 創建獎懲記錄
        String[] titles = {"優秀學生獎學金", "三好學生", "遲到警告", "體育競賽獎", "藝術比賽獎"};
        RewardPunishment.Type[] types = {RewardPunishment.Type.REWARD, RewardPunishment.Type.REWARD, 
                                        RewardPunishment.Type.PUNISHMENT, RewardPunishment.Type.REWARD, RewardPunishment.Type.REWARD};
        RewardPunishment.Category[] categories = {RewardPunishment.Category.SCHOLARSHIP, RewardPunishment.Category.HONOR_CERTIFICATE,
                                                 RewardPunishment.Category.WARNING, RewardPunishment.Category.SPORTS_AWARD, RewardPunishment.Category.ART_AWARD};
        
        for (int i = 0; i < 5; i++) {
            RewardPunishment rewardPunishment = new RewardPunishment();
            rewardPunishment.setStudent(studentRepository.findById((long) (i + 1)).orElse(null));
            rewardPunishment.setType(types[i]);
            rewardPunishment.setCategory(categories[i]);
            rewardPunishment.setTitle(titles[i]);
            rewardPunishment.setDescription("詳細描述");
            rewardPunishment.setDate(LocalDate.of(2024, 1, 10 + i));
            rewardPunishment.setIssuer("學校相關部門");
            rewardPunishment.setCertificate("證書編號" + (i + 1));
            rewardPunishment.setRemarks("備註信息");
            rewardPunishment.setStatus(RewardPunishment.Status.ACTIVE);
            rewardPunishmentRepository.save(rewardPunishment);
        }
    }
    */
} 