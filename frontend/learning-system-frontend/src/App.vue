<template>
  <div id="app">
    <el-container class="layout-container">
      <!-- 顶部导航 -->
      <el-header class="app-header">
        <div class="header-content">
          <h1 class="app-title">
            <el-icon style="vertical-align: middle; margin-right: 10px; color: #409EFF;">
              <Reading />
            </el-icon>
            渐进式学习管理系统
          </h1>
          <div class="header-actions">
            <el-tag type="info" size="large">
              {{ currentDate }}
            </el-tag>
          </div>
        </div>
      </el-header>

      <!-- 主内容区 -->
      <el-main class="app-main">
        <div class="main-content">
          <!-- 左侧输入区域 -->
          <div class="left-panel">
            <StudyInput @content-added="handleContentAdded" />
          </div>

          <!-- 右侧任务区域 -->
          <div class="right-panel">
            <DailyTasks ref="dailyTasksRef" />
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Reading } from '@element-plus/icons-vue'
import StudyInput from '@/components/StudyInput.vue'
import DailyTasks from '@/components/DailyTasks.vue'

const currentDate = ref('')
const dailyTasksRef = ref(null)

// 更新当前日期
const updateCurrentDate = () => {
  const now = new Date()
  currentDate.value = now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
}

// 处理学习内容添加事件
const handleContentAdded = () => {
  // 刷新任务列表
  if (dailyTasksRef.value && dailyTasksRef.value.refreshTasks) {
    dailyTasksRef.value.refreshTasks()
  }
}

onMounted(() => {
  updateCurrentDate()
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
  height: 100vh;
}

.layout-container {
  height: 100vh;
}

.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.app-title {
  color: white;
  font-size: 24px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.app-main {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
}

.left-panel, .right-panel {
  min-height: 600px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }

  .app-header {
    padding: 0 15px;
  }

  .app-title {
    font-size: 18px;
  }
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>