// src/services/api.js
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 学习内容相关API
export const studyApi = {
  // 添加学习内容
  addStudyContent: (data) => api.post('/study', data),

  // 获取今日任务
  getTodayTasks: () => api.get('/study/today-tasks'),

  // 完成任务
  completeTask: (taskId) => api.put(`/study/task/${taskId}/complete`),

  // 获取所有学习内容
  // getAllStudyContents: () => api.get('/study'),

  // 删除学习内容
  deleteStudyContent: (contentId) => api.delete(`/study/${contentId}`),

  // 获取所有学习内容
  getAllStudyContents: () => api.get('/study/all'),

  // 根据ID获取学习内容
  getStudyContentById: (contentId) => api.get(`/study/${contentId}`),

  // 获取今日已完成任务
  getTodayCompletedTasks: () => api.get('/study/today-completed-tasks'),

  // 获取今日待完成任务
  getTodayPendingTasks: () => api.get('/study/today-pending-tasks'),

  // 获取所有已完成任务
  getAllCompletedTasks: () => api.get('/study/completed-tasks'),
}

export default api
