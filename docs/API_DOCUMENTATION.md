# API 文档

## 基础信息
- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

## 学习内容管理

### 1. 添加学习内容
**POST** `/study`

**请求体**:
```json
{
  "title": "Spring Boot基础",
  "content": "学习了Spring Boot的自动配置原理...",
  "category": "programming",
  "difficultyLevel": 3,
  "studyDate": "2024-01-15"
}

**响应**:
{
  "id": 1,
  "title": "Spring Boot基础",
  "content": "学习了Spring Boot的自动配置原理...",
  "category": "programming",
  "difficultyLevel": 3,
  "studyDate": "2024-01-15",
  "createdTime": "2024-01-15T10:30:00",
  "updatedTime": "2024-01-15T10:30:00"
}