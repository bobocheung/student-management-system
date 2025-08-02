# 多階段構建 - 構建階段
FROM maven:3.8.6-openjdk-11-slim AS builder

WORKDIR /app

# 複製Maven配置文件並下載依賴
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 複製源代碼並構建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 運行階段
FROM openjdk:11-jre-slim

# 創建非root用戶
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 設置工作目錄
WORKDIR /app

# 創建必要的目錄
RUN mkdir -p uploads qr-codes logs && \
    chown -R appuser:appuser /app

# 複製構建好的jar文件
COPY --from=builder /app/target/student-management-system-1.0.0.jar app.jar

# 切換到非root用戶
USER appuser

# 暴露端口
EXPOSE 8080

# 健康檢查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 設置JVM參數
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# 運行應用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 