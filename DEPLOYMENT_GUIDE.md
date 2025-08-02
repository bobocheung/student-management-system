# 部署指南

本文檔提供了學生信息管理系統的詳細部署指南。

## 🚀 快速開始

### 本地開發

1. **克隆項目**
   ```bash
   git clone <your-repo-url>
   cd student-management-system
   ```

2. **運行應用**
   ```bash
   # 使用開發腳本（推薦）
   ./scripts/dev.sh

   # 或者使用Maven
   mvn spring-boot:run
   ```

3. **訪問應用**
   - 打開瀏覽器訪問: http://localhost:8080
   - 使用測試帳戶登入:
     - 管理員: `admin` / `admin123`
     - 教師: `teacher` / `teacher123`
     - 學生: `student` / `student123`

## 🐳 Docker 部署

### 方式一：單容器部署（使用H2數據庫）

```bash
# 構建鏡像
docker build -t student-management-system .

# 運行容器
docker run -d \
  --name student-management-system \
  -p 8080:8080 \
  student-management-system
```

### 方式二：使用docker-compose（包含MySQL）

```bash
# 啟動所有服務
docker-compose up -d

# 查看日誌
docker-compose logs -f app

# 停止服務
docker-compose down -v
```

訪問地址：
- 應用: http://localhost:8080
- phpMyAdmin: http://localhost:8081 (root/password)

## 🏭 生產環境部署

### 系統要求

- Ubuntu 18.04+ / CentOS 7+
- Java 11+
- 至少 2GB RAM
- 至少 10GB 磁盤空間

### 自動部署（推薦）

1. **準備環境**
   ```bash
   # 安裝Java 11
   sudo apt update
   sudo apt install openjdk-11-jdk

   # 驗證安裝
   java -version
   ```

2. **構建應用**
   ```bash
   mvn clean package
   ```

3. **部署應用**
   ```bash
   # 使用部署腳本
   sudo ./scripts/deploy.sh
   ```

4. **管理服務**
   ```bash
   # 查看狀態
   sudo ./scripts/deploy.sh status

   # 查看日誌
   sudo ./scripts/deploy.sh logs

   # 重啟服務
   sudo ./scripts/deploy.sh restart

   # 回滾版本
   sudo ./scripts/deploy.sh rollback
   ```

### 手動部署

1. **創建用戶和目錄**
   ```bash
   sudo useradd -r -s /bin/false appuser
   sudo mkdir -p /opt/student-management-system
   sudo chown appuser:appuser /opt/student-management-system
   ```

2. **複製JAR文件**
   ```bash
   sudo cp target/student-management-system-1.0.0.jar /opt/student-management-system/
   sudo chown appuser:appuser /opt/student-management-system/student-management-system-1.0.0.jar
   ```

3. **創建systemd服務**
   ```bash
   sudo tee /etc/systemd/system/student-management-system.service > /dev/null <<EOF
   [Unit]
   Description=Student Management System
   After=network.target

   [Service]
   Type=simple
   User=appuser
   Group=appuser
   WorkingDirectory=/opt/student-management-system
   ExecStart=/usr/bin/java -Xmx1g -Xms512m -jar /opt/student-management-system/student-management-system-1.0.0.jar
   Restart=on-failure
   RestartSec=10

   Environment=SPRING_PROFILES_ACTIVE=prod

   [Install]
   WantedBy=multi-user.target
   EOF
   ```

4. **啟動服務**
   ```bash
   sudo systemctl daemon-reload
   sudo systemctl enable student-management-system
   sudo systemctl start student-management-system
   ```

## 🗄️ 數據庫配置

### 使用MySQL

1. **安裝MySQL**
   ```bash
   sudo apt install mysql-server
   sudo mysql_secure_installation
   ```

2. **創建數據庫**
   ```sql
   CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON student_management.* TO 'appuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **更新配置文件**
   創建 `application-prod.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
       username: appuser
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       hibernate:
         ddl-auto: update
       database-platform: org.hibernate.dialect.MySQL8Dialect
   ```

## 🔒 安全配置

### 防火牆設置

```bash
# 只允許必要的端口
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 8080/tcp  # 應用端口
sudo ufw enable
```

### SSL/TLS 配置

使用nginx反向代理：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /path/to/your/certificate.crt;
    ssl_certificate_key /path/to/your/private.key;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## 📊 監控和日誌

### 應用日誌

```bash
# 查看實時日誌
sudo journalctl -u student-management-system -f

# 查看錯誤日誌
sudo journalctl -u student-management-system -p err

# 查看最近的日誌
sudo journalctl -u student-management-system --since "1 hour ago"
```

### 系統監控

建議安裝監控工具：
- Prometheus + Grafana
- ELK Stack (Elasticsearch, Logstash, Kibana)
- 或使用雲監控服務

## 🔄 備份和恢復

### 數據庫備份

```bash
# 備份數據庫
mysqldump -u appuser -p student_management > backup_$(date +%Y%m%d_%H%M%S).sql

# 恢復數據庫
mysql -u appuser -p student_management < backup_20240102_120000.sql
```

### 應用備份

部署腳本會自動備份舊版本的JAR文件到 `/opt/student-management-system/backups/`

## 🚨 故障排除

### 常見問題

1. **端口被占用**
   ```bash
   sudo netstat -tlnp | grep :8080
   sudo kill -9 <PID>
   ```

2. **內存不足**
   ```bash
   # 調整JVM參數
   Environment=JAVA_OPTS="-Xmx512m -Xms256m"
   ```

3. **數據庫連接失敗**
   ```bash
   # 檢查MySQL服務
   sudo systemctl status mysql
   
   # 檢查網絡連接
   telnet localhost 3306
   ```

### 日誌分析

```bash
# 查找錯誤
sudo journalctl -u student-management-system | grep ERROR

# 查找異常
sudo journalctl -u student-management-system | grep Exception

# 查看啟動日誌
sudo journalctl -u student-management-system --since "today" | head -50
```

## 📞 支持

如遇到問題，請：

1. 查看日誌文件
2. 檢查系統資源使用情況
3. 參考故障排除章節
4. 在GitHub上創建Issue

---

**注意**: 在生產環境中，請確保：
- 更改默認密碼
- 配置SSL證書
- 設置防火牆規則
- 定期備份數據
- 監控系統性能