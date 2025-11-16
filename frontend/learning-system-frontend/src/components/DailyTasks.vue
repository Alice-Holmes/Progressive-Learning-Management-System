<template>
  <div class="daily-tasks">
    <el-card shadow="hover">
      <template #header>
        <div class="task-header">
          <span class="header-title">📋 今日复习任务</span>
          <div class="header-stats">
            <el-tag type="success" size="small"> 已完成: {{ completedTasksCount }} </el-tag>
            <el-tag type="warning" size="small"> 待完成: {{ pendingTasksCount }} </el-tag>
            <el-button
              v-if="completedTasksCount > 0"
              size="small"
              link
              @click="showCompleted = !showCompleted"
            >
              {{ showCompleted ? '隐藏已完成' : '查看已完成' }}
            </el-button>
          </div>
        </div>
      </template>

      <!-- 空状态 -->
      <div v-if="pendingTasks.length === 0 && completedTasks.length === 0" class="empty-state">
        <el-empty description="今天没有复习任务，休息一下或学习新内容吧！" />
      </div>

      <!-- 全部完成状态 -->
      <div v-else-if="pendingTasks.length === 0 && completedTasks.length > 0" class="all-completed">
        <el-result icon="success" title="恭喜！" sub-title="今日复习任务已全部完成！">
          <template #extra>
            <el-button type="primary" @click="showCompleted = true"> 查看完成记录 </el-button>
          </template>
        </el-result>
      </div>

      <!-- 待完成任务 -->
      <div v-if="pendingTasks.length > 0">
        <div class="section-title">待完成 ({{ pendingTasks.length }})</div>
        <el-timeline>
          <el-timeline-item
            v-for="task in pendingTasks"
            :key="task.id"
            :timestamp="`第 ${task.reviewStage} 次复习`"
            type="primary"
            icon="Clock"
            size="large"
          >
            <task-card :task="task" @task-completed="handleTaskCompleted" />
          </el-timeline-item>
        </el-timeline>
      </div>

      <!-- 已完成任务（可折叠） -->
      <div v-if="completedTasks.length > 0 && showCompleted">
        <div class="section-title">已完成 ({{ completedTasks.length }})</div>
        <el-timeline>
          <el-timeline-item
            v-for="task in completedTasks"
            :key="task.id"
            :timestamp="`第 ${task.reviewStage} 次复习 - ${formatTime(task.completedTime)}`"
            type="success"
            icon="SuccessFilled"
            size="large"
          >
            <task-card :task="task" :readonly="true" />
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import TaskCard from './TaskCard.vue'
import { studyApi } from '@/services/api'

const pendingTasks = ref([])
const completedTasks = ref([])
const showCompleted = ref(false)

// 计算属性
const pendingTasksCount = computed(() => pendingTasks.value.length)
const completedTasksCount = computed(() => completedTasks.value.length)

const formatTime = (dateTimeString) => {
  if (!dateTimeString) return ''
  return new Date(dateTimeString).toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
  })
}

const loadTodayTasks = async () => {
  try {
    // 并行加载待完成和已完成任务
    const [pendingResponse, completedResponse] = await Promise.all([
      studyApi.getTodayPendingTasks(),
      studyApi.getTodayCompletedTasks(),
    ])

    pendingTasks.value = pendingResponse.data
    completedTasks.value = completedResponse.data
  } catch (error) {
    console.error('加载任务失败:', error)
    ElMessage.error('加载任务失败')
  }
}

const handleTaskCompleted = () => {
  ElMessage.success({
    message: '任务完成！继续加油！💪',
    duration: 2000,
  })
  loadTodayTasks() // 重新加载任务
}

onMounted(() => {
  loadTodayTasks()
})

// 暴露刷新方法给父组件
defineExpose({
  refreshTasks: loadTodayTasks,
})
</script>

<style scoped>
.daily-tasks {
  margin-bottom: 20px;
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.header-stats {
  display: flex;
  align-items: center;
  gap: 10px;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #606266;
  margin: 20px 0 15px 0;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.empty-state,
.all-completed {
  padding: 40px 0;
}

:deep(.el-timeline) {
  padding-left: 10px;
}

:deep(.el-timeline-item__timestamp) {
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .task-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-stats {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
