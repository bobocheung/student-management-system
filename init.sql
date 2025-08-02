-- 初始化數據庫腳本
CREATE DATABASE IF NOT EXISTS student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE student_management;

-- 創建用戶表的初始數據將由應用程序的DataInitializer處理
-- 這個文件可以用來添加其他初始化數據

-- 示例：插入一些基礎數據
-- INSERT INTO classes (name, code, grade, status, created_at, updated_at) 
-- VALUES ('一年級A班', 'G1A', 1, 'ACTIVE', NOW(), NOW());

-- INSERT INTO courses (name, code, credits, hours, status, created_at, updated_at)
-- VALUES ('數學', 'MATH101', 3, 40, 'ACTIVE', NOW(), NOW());