# 貢獻指南

感謝您對學生信息管理系統的關注！我們歡迎各種形式的貢獻。

## 如何貢獻

### 報告問題

如果您發現了bug或有功能建議：

1. 檢查 [Issues](../../issues) 確認問題是否已被報告
2. 如果沒有，請創建新的 Issue
3. 詳細描述問題或建議，包括：
   - 問題的詳細描述
   - 重現步驟
   - 預期行為
   - 實際行為
   - 環境信息（操作系統、Java版本等）

### 提交代碼

1. **Fork 項目**
   ```bash
   git clone https://github.com/your-username/student-management-system.git
   cd student-management-system
   ```

2. **創建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **進行開發**
   - 遵循現有的代碼風格
   - 添加必要的測試
   - 確保所有測試通過
   - 更新相關文檔

4. **提交更改**
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   ```

5. **推送到分支**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **創建 Pull Request**
   - 提供清晰的PR標題和描述
   - 引用相關的Issues
   - 確保CI檢查通過

## 開發規範

### 代碼風格

- 使用Java 11特性
- 遵循Spring Boot最佳實踐
- 使用有意義的變量和方法名
- 添加適當的註釋

### 提交信息規範

使用以下格式：

```
<type>(<scope>): <subject>

<body>

<footer>
```

類型 (type)：
- `feat`: 新功能
- `fix`: 修復bug
- `docs`: 文檔更新
- `style`: 代碼格式調整
- `refactor`: 代碼重構
- `test`: 測試相關
- `chore`: 構建過程或輔助工具的變動

### 測試

在提交PR之前，請確保：

```bash
# 運行所有測試
mvn test

# 檢查代碼覆蓋率
mvn jacoco:report

# 運行集成測試
mvn verify
```

## 開發環境設置

1. **環境要求**
   - Java 11+
   - Maven 3.6+
   - IDE (推薦 IntelliJ IDEA 或 Eclipse)

2. **設置開發環境**
   ```bash
   git clone <your-fork-url>
   cd student-management-system
   mvn clean install
   ```

3. **運行開發服務器**
   ```bash
   mvn spring-boot:run
   ```

## 項目結構

了解項目結構有助於更好地貢獻：

```
src/main/java/com/student/
├── config/          # 配置類
├── controller/      # REST控制器
├── entity/          # JPA實體
├── repository/      # 數據訪問層
├── service/         # 業務邏輯層
└── StudentManagementApplication.java
```

## 聯繫方式

如有任何問題，請通過以下方式聯繫：

- 創建 Issue
- 發送郵件至開發團隊

感謝您的貢獻！