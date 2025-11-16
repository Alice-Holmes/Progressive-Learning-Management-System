# 开发指南

## 开发环境搭建

### 1. 后端开发环境

#### 环境要求
- **JDK**: 17或更高版本
- **IDE**: IntelliJ IDEA (推荐) 或 Eclipse
- **构建工具**: Maven 3.6+
- **数据库**: MySQL 8.0+

#### 配置步骤
1. **导入项目**
   - 在IDEA中选择 `File` → `Open`
   - 选择 `backend` 目录
   - 等待Maven依赖下载完成

2. **数据库配置**
   ```sql
   -- 创建数据库
   CREATE DATABASE plearningmsystem CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 创建用户（可选）
   CREATE USER 'learn_user'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON plearningmsystem.* TO 'learn_user'@'localhost';