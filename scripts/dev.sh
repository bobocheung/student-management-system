#!/bin/bash

# 學生信息管理系統開發腳本

set -e

# 顏色定義
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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
    echo -e "${BLUE}  學生信息管理系統開發工具${NC}"
    echo -e "${BLUE}================================${NC}"
    echo
}

# 檢查Java版本
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java未安裝，請先安裝Java 11或更高版本"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
    if [[ "$java_version" < "11" ]]; then
        print_error "需要Java 11或更高版本，當前版本: $java_version"
        exit 1
    fi
    
    print_message "Java版本檢查通過: $java_version"
}

# 檢查Maven
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven未安裝，請先安裝Maven"
        exit 1
    fi
    
    maven_version=$(mvn -version | head -n 1 | cut -d' ' -f3)
    print_message "Maven版本: $maven_version"
}

# 清理項目
clean() {
    print_message "正在清理項目..."
    mvn clean -q
    print_message "清理完成"
}

# 編譯項目
compile() {
    print_message "正在編譯項目..."
    mvn compile -q
    print_message "編譯完成"
}

# 運行測試
test() {
    print_message "正在運行測試..."
    mvn test
    print_message "測試完成"
}

# 打包項目
package() {
    print_message "正在打包項目..."
    mvn package -DskipTests -q
    print_message "打包完成"
}

# 運行應用
run() {
    print_message "正在啟動應用..."
    print_message "應用將在 http://localhost:8080 運行"
    print_message "測試帳戶："
    echo "  - 管理員: admin / admin123"
    echo "  - 教師: teacher / teacher123"
    echo "  - 學生: student / student123"
    echo
    print_message "按 Ctrl+C 停止應用"
    echo
    mvn spring-boot:run
}

# 使用Docker運行
docker_run() {
    print_message "正在構建Docker鏡像..."
    docker build -t student-management-system .
    
    print_message "正在啟動Docker容器..."
    docker run -p 8080:8080 --name student-management-system student-management-system
}

# 使用docker-compose運行
docker_compose_run() {
    print_message "正在使用docker-compose啟動服務..."
    docker-compose up --build
}

# 顯示幫助信息
show_help() {
    echo "用法: $0 [命令]"
    echo
    echo "可用命令:"
    echo "  clean           清理項目"
    echo "  compile         編譯項目"
    echo "  test            運行測試"
    echo "  package         打包項目"
    echo "  run             運行應用 (默認)"
    echo "  docker          使用Docker運行"
    echo "  docker-compose  使用docker-compose運行"
    echo "  help            顯示幫助信息"
    echo
    echo "示例:"
    echo "  $0              # 運行應用"
    echo "  $0 test         # 運行測試"
    echo "  $0 docker       # 使用Docker運行"
}

# 主函數
main() {
    print_header
    
    case "${1:-run}" in
        "clean")
            check_maven
            clean
            ;;
        "compile")
            check_java
            check_maven
            compile
            ;;
        "test")
            check_java
            check_maven
            test
            ;;
        "package")
            check_java
            check_maven
            package
            ;;
        "run")
            check_java
            check_maven
            clean
            compile
            run
            ;;
        "docker")
            docker_run
            ;;
        "docker-compose")
            docker_compose_run
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