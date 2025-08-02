#!/bin/bash

# 學生信息管理系統部署腳本

set -e

# 顏色定義
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置
APP_NAME="student-management-system"
JAR_NAME="student-management-system-1.0.0.jar"
DEPLOY_DIR="/opt/${APP_NAME}"
SERVICE_NAME="${APP_NAME}.service"
BACKUP_DIR="/opt/${APP_NAME}/backups"

# 打印帶顏色的消息
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}  學生信息管理系統部署工具${NC}"
    echo -e "${BLUE}================================${NC}"
    echo
}

# 檢查是否為root用戶
check_root() {
    if [[ $EUID -ne 0 ]]; then
        print_error "此腳本需要root權限運行"
        print_message "請使用: sudo $0"
        exit 1
    fi
}

# 創建部署目錄
create_directories() {
    print_message "創建部署目錄..."
    mkdir -p ${DEPLOY_DIR}
    mkdir -p ${DEPLOY_DIR}/logs
    mkdir -p ${DEPLOY_DIR}/uploads
    mkdir -p ${DEPLOY_DIR}/qr-codes
    mkdir -p ${BACKUP_DIR}
}

# 創建應用用戶
create_user() {
    if ! id "appuser" &>/dev/null; then
        print_message "創建應用用戶..."
        useradd -r -s /bin/false appuser
    else
        print_message "應用用戶已存在"
    fi
}

# 備份舊版本
backup_old_version() {
    if [[ -f "${DEPLOY_DIR}/${JAR_NAME}" ]]; then
        print_message "備份舊版本..."
        timestamp=$(date +%Y%m%d_%H%M%S)
        cp "${DEPLOY_DIR}/${JAR_NAME}" "${BACKUP_DIR}/${JAR_NAME}.${timestamp}"
    fi
}

# 停止服務
stop_service() {
    if systemctl is-active --quiet ${SERVICE_NAME}; then
        print_message "停止服務..."
        systemctl stop ${SERVICE_NAME}
    fi
}

# 部署新版本
deploy_jar() {
    print_message "部署新版本..."
    if [[ ! -f "target/${JAR_NAME}" ]]; then
        print_error "找不到JAR文件: target/${JAR_NAME}"
        print_message "請先運行: mvn clean package"
        exit 1
    fi
    
    cp "target/${JAR_NAME}" "${DEPLOY_DIR}/"
    chown appuser:appuser "${DEPLOY_DIR}/${JAR_NAME}"
    chmod 755 "${DEPLOY_DIR}/${JAR_NAME}"
}

# 創建systemd服務文件
create_service() {
    print_message "創建systemd服務..."
    cat > /etc/systemd/system/${SERVICE_NAME} << EOF
[Unit]
Description=Student Management System
After=network.target

[Service]
Type=simple
User=appuser
Group=appuser
WorkingDirectory=${DEPLOY_DIR}
ExecStart=/usr/bin/java -Xmx1g -Xms512m -XX:+UseG1GC -jar ${DEPLOY_DIR}/${JAR_NAME}
ExecStop=/bin/kill -TERM \$MAINPID
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=${APP_NAME}

Environment=SPRING_PROFILES_ACTIVE=prod
Environment=LOGGING_FILE_NAME=${DEPLOY_DIR}/logs/application.log

[Install]
WantedBy=multi-user.target
EOF

    systemctl daemon-reload
}

# 設置文件權限
set_permissions() {
    print_message "設置文件權限..."
    chown -R appuser:appuser ${DEPLOY_DIR}
    chmod -R 755 ${DEPLOY_DIR}
}

# 啟動服務
start_service() {
    print_message "啟動服務..."
    systemctl enable ${SERVICE_NAME}
    systemctl start ${SERVICE_NAME}
    
    # 等待服務啟動
    sleep 10
    
    if systemctl is-active --quiet ${SERVICE_NAME}; then
        print_message "服務啟動成功"
        print_message "應用運行在: http://localhost:8080"
    else
        print_error "服務啟動失敗"
        print_message "查看日誌: journalctl -u ${SERVICE_NAME} -f"
        exit 1
    fi
}

# 健康檢查
health_check() {
    print_message "進行健康檢查..."
    for i in {1..30}; do
        if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
            print_message "健康檢查通過"
            return 0
        fi
        print_message "等待應用啟動... ($i/30)"
        sleep 2
    done
    
    print_warning "健康檢查超時，請手動檢查應用狀態"
}

# 顯示狀態
show_status() {
    echo
    print_message "部署狀態:"
    systemctl status ${SERVICE_NAME} --no-pager
    echo
    print_message "應用日誌:"
    journalctl -u ${SERVICE_NAME} --no-pager -n 10
}

# 回滾到前一版本
rollback() {
    print_message "回滾到前一版本..."
    
    # 找到最新的備份
    latest_backup=$(ls -t ${BACKUP_DIR}/${JAR_NAME}.* 2>/dev/null | head -n 1)
    
    if [[ -z "$latest_backup" ]]; then
        print_error "沒有找到備份文件"
        exit 1
    fi
    
    print_message "使用備份: $latest_backup"
    
    stop_service
    cp "$latest_backup" "${DEPLOY_DIR}/${JAR_NAME}"
    chown appuser:appuser "${DEPLOY_DIR}/${JAR_NAME}"
    start_service
    health_check
}

# 顯示幫助信息
show_help() {
    echo "用法: $0 [命令]"
    echo
    echo "可用命令:"
    echo "  deploy      部署應用 (默認)"
    echo "  rollback    回滾到前一版本"
    echo "  status      顯示服務狀態"
    echo "  start       啟動服務"
    echo "  stop        停止服務"
    echo "  restart     重啟服務"
    echo "  logs        查看日誌"
    echo "  help        顯示幫助信息"
    echo
    echo "示例:"
    echo "  sudo $0 deploy    # 部署應用"
    echo "  sudo $0 rollback  # 回滾版本"
    echo "  sudo $0 status    # 查看狀態"
}

# 主函數
main() {
    print_header
    
    case "${1:-deploy}" in
        "deploy")
            check_root
            create_directories
            create_user
            backup_old_version
            stop_service
            deploy_jar
            create_service
            set_permissions
            start_service
            health_check
            show_status
            ;;
        "rollback")
            check_root
            rollback
            ;;
        "status")
            systemctl status ${SERVICE_NAME}
            ;;
        "start")
            check_root
            systemctl start ${SERVICE_NAME}
            print_message "服務已啟動"
            ;;
        "stop")
            check_root
            systemctl stop ${SERVICE_NAME}
            print_message "服務已停止"
            ;;
        "restart")
            check_root
            systemctl restart ${SERVICE_NAME}
            print_message "服務已重啟"
            ;;
        "logs")
            journalctl -u ${SERVICE_NAME} -f
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            print_error "未知命令: $1"
            show_help
            exit 1
            ;;
    esac
}

# 運行主函數
main "$@"