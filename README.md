# 學生信息管理系統

一個基於Spring Boot的現代化學生信息管理系統，提供完整的學校管理功能。

## 功能特色

### 1. 學生信息管理
- **基本資料管理**: 學號、姓名、性別、出生日期、身份證號、照片、聯繫方式
- **家庭背景**: 父母/監護人信息、緊急聯絡人、地址
- **學籍管理**: 入學登記、轉學、休學、復學、退學、畢業等流程管理
- **學籍狀態追蹤**: 在讀、畢業、輟學等狀態管理

### 2. 班級與課程管理
- **班級管理**: 班級創建、學生分班/調班管理
- **課程管理**: 課程設置、排課系統、時間衝突檢測

### 3. 成績管理
- **成績錄入**: 平時成績、考試成績、批量導入
- **成績分析**: 成績單生成、GPA計算、排名統計
- **成績趨勢分析**: 圖表可視化
- **補考/重修管理**: 自動觸發不合格學生的補考流程

### 4. 考勤管理
- **日常考勤**: 課堂點名、二維碼簽到、人臉識別
- **記錄管理**: 遲到、早退、曠課、請假記錄
- **報表統計**: 出勤率報告、自動預警

### 5. 教師管理
- **教師檔案**: 基本信息、職稱、所授課程、聯繫方式
- **教學任務分配**: 班級分配、課程分配、課時量統計

### 6. 獎懲與綜合素質評價
- **獎懲記錄**: 獎學金、榮譽證書、處分記錄
- **綜合評測**: 德育、體育、課外活動等多元評價

### 7. 系統管理
- **權限控制**: 角色分配、細化功能訪問權限
- **數據安全**: 數據備份、隱私保護、操作日誌審計
- **系統設置**: 學年/學期切換、校曆設定

## 技術棧

- **後端**: Spring Boot 2.7.14, Spring Security, Spring Data JPA
- **數據庫**: H2 Database (開發環境), MySQL 8.0 (生產環境)
- **前端**: Thymeleaf, Bootstrap 5, Font Awesome
- **構建工具**: Maven
- **Java版本**: 11

## 快速開始

### 環境要求
- Java 11 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本 (可選，默認使用H2內存數據庫)

### 安裝步驟

1. **克隆項目**
   ```bash
   git clone <repository-url>
   cd student-management-system
   ```

2. **運行應用** (使用默認H2數據庫)
   ```bash
   mvn spring-boot:run
   ```

3. **訪問系統**
   - 打開瀏覽器訪問: http://localhost:8080
   - 使用測試帳戶登入:
     - 管理員: admin / admin123
     - 教師: teacher / teacher123
     - 學生: student / student123

### 可選：配置MySQL數據庫

如果你想使用MySQL而不是H2數據庫：

1. **創建MySQL數據庫**
   ```sql
   CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **修改配置文件**
   修改 `src/main/resources/application.yml`：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
       username: your_username
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       hibernate:
         ddl-auto: update
       database-platform: org.hibernate.dialect.MySQL8Dialect
   ```

## 項目結構

```
src/
├── main/
│   ├── java/com/student/
│   │   ├── config/          # 配置類
│   │   ├── controller/      # 控制器
│   │   ├── entity/          # 實體類
│   │   ├── repository/      # 數據訪問層
│   │   ├── service/         # 業務邏輯層
│   │   └── StudentManagementApplication.java
│   └── resources/
│       ├── templates/       # Thymeleaf模板
│       ├── static/          # 靜態資源
│       └── application.yml  # 配置文件
└── test/                    # 測試代碼
```

## 主要功能模塊

### 實體設計
- **Student**: 學生實體，包含基本信息和家庭背景
- **Teacher**: 教師實體，包含基本信息和職稱信息
- **Class**: 班級實體，包含班級信息和班主任
- **Course**: 課程實體，包含課程信息和授課教師
- **Grade**: 成績實體，包含平時成績和考試成績
- **Attendance**: 考勤實體，包含出勤記錄和簽到方式
- **User**: 用戶實體，用於系統認證和權限管理

### 安全認證
- 基於Spring Security的認證授權
- 角色權限控制（管理員、教師、學生、家長）
- 密碼加密存儲
- 會話管理

### 數據管理
- JPA/Hibernate ORM映射
- 分頁查詢支持
- 高級搜索功能
- 數據驗證和約束

## API接口

系統提供RESTful API接口，支持：

- 學生管理: `/students/api/**`
- 教師管理: `/teachers/api/**`
- 班級管理: `/classes/api/**`
- 課程管理: `/courses/api/**`
- 成績管理: `/grades/api/**`
- 考勤管理: `/attendance/api/**`

## 部署說明

### 開發環境

使用開發腳本：
```bash
# 使用開發腳本（推薦）
./scripts/dev.sh

# 或者直接使用Maven
mvn spring-boot:run
```

### 生產環境

使用部署腳本：
```bash
# 構建項目
mvn clean package

# 部署到生產環境（需要root權限）
sudo ./scripts/deploy.sh

# 查看服務狀態
sudo ./scripts/deploy.sh status
```

手動部署：
```bash
mvn clean package
java -jar target/student-management-system-1.0.0.jar
```

### Docker部署

單容器部署：
```bash
docker build -t student-management-system .
docker run -p 8080:8080 student-management-system
```

使用docker-compose（包含MySQL）：
```bash
# 啟動所有服務
docker-compose up -d

# 查看日誌
docker-compose logs -f

# 停止服務
docker-compose down
```

服務訪問：
- 應用: http://localhost:8080
- phpMyAdmin: http://localhost:8081

## 配置說明

### 數據庫配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/student_management
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 文件上傳配置
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
```

### 郵件配置
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

## 開發指南

### 添加新功能
1. 創建實體類
2. 創建Repository接口
3. 創建Service接口和實現類
4. 創建Controller類
5. 創建前端模板

### 代碼規範
- 使用Java 11特性
- 遵循Spring Boot最佳實踐
- 使用Lombok減少樣板代碼
- 添加適當的註釋和文檔

## 測試

### 單元測試
```bash
mvn test
```

### 集成測試
```bash
mvn verify
```

## 貢獻指南

1. Fork 項目
2. 創建功能分支
3. 提交更改
4. 推送到分支
5. 創建 Pull Request

## 許可證

本項目採用 MIT 許可證 - 查看 [LICENSE](LICENSE) 文件了解詳情。

## 聯繫方式

如有問題或建議，請聯繫開發團隊。

## 更新日誌

### v1.1.0 (2025-08-02)
- 🐛 修復Hibernate查詢驗證錯誤
- ✨ 新增報表統計功能
- ✨ 新增自定義錯誤頁面 (404, 403, 500)
- ✨ 新增學生照片URL支持
- ✨ 完善錯誤處理機制
- 🔧 優化項目配置
- 📝 更新文檔和README

### v1.0.0 (2024-01-01)
- 🎉 初始版本發布
- ✨ 基本CRUD功能
- 🔐 用戶認證授權
- 👨‍🎓 學生信息管理
- 👨‍🏫 教師信息管理
- 🏫 班級課程管理
- 📊 成績考勤管理 