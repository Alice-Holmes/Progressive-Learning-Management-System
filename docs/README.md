# 渐进式学习管理系统 (Progressive Learning Management System)

## 🎯 项目简介

一个基于遗忘曲线理论的智能学习管理系统，帮助用户科学安排复习计划，减少学习挫败感，提升知识保留率。

### ✨ 核心特性
- 📝 **智能学习记录** - 记录每日学习内容，自动分类和标记难度
- 🔄 **渐进式复习** - 基于艾宾浩斯遗忘曲线自动生成复习任务
- 📊 **可视化进度** - 清晰展示学习成果和复习进度
- 🎮 **成就系统** - 通过游戏化设计保持学习动力
- 🤖 **AI助手集成** - 提供个性化学习建议和答疑

### 🛠 技术栈
**前端**: Vue 3 + Element Plus + Pinia + Vue Router  
**后端**: Spring Boot 3 + Spring Data JPA + MySQL 8.0  
**AI平台**: Dify (智能体集成)  
**开发工具**: IntelliJ IDEA + VS Code + Maven

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 快速启动
```bash
# 1. 克隆项目
git clone <项目地址>

# 2. 后端启动
cd backend
mvn spring-boot:run

# 3. 前端启动  
cd ../frontend/learning-system-frontend
npm install
npm run dev

# 4. 访问应用
打开 http://localhost:5173

# 5. 相关文档
- [开发指南](DEVELOPMENT.md) - 详细开发环境和配置说明
- [API文档](API_DOCUMENTATION.md) - 后端接口详细说明
- [数据库设计](DATABASE.md) - 数据表结构和关系
- [部署指南](DEPLOYMENT.md) - 生产环境部署说明
- [贡献指南](CONTRIBUTING.md) - 参与项目开发指南