<template>
  <div class="daily-tasks">
    <el-card :header="`📋 今日复习任务 (${tasks.length}个)`" shadow="hover">
      <div v-if="tasks.length === 0" class="empty-state">
        <el-empty description="今天没有复习任务，休息一下或学习新内容吧！" />
      </div>

      <el-timeline v-else>
        <el-timeline-item
          v-for="task in tasks"
          :key="task.id"
          :timestamp="`第 ${task.reviewStage} 次复习`"
          :type="getTimelineType(task.status)"
          :icon="getTaskIcon(task.status)"
          size="large"
        >
          <el-card
            shadow="never"
            :class="['task-card', task.status === 'COMPLETED' ? 'completed' : '']"
          >
            <div class="task-content">
              <h4 class="task-title">{{ task.studyContent.title }}</h4>
              <p class="task-desc">{{ task.studyContent.content }}</p>

              <div class="task-meta">
                <el-tag
                  :type="getCategoryType(task.studyContent.category)"
                  size="small"
                  class="category-tag"
                >
                  {{ getCategoryName(task.studyContent.category) }}
                </el-tag>
                <el-tag size="small" type="info" class="difficulty-tag">
                  难度: {{ '⭐'.repeat(task.studyContent.difficultyLevel) }}
                </el-tag>
                <!-- <el-tag size="small" type="info" class="difficulty-tag">
                  难度: {{ task.studyContent.difficultyLevel }}级 ({{ '⭐'.repeat(task.studyContent.difficultyLevel) }})
                </el-tag> -->
                <span class="study-date">
                  学习日期: {{ formatDate(task.studyContent.studyDate) }}
                </span>
              </div>

              <div class="task-actions">
                <el-button
                  v-if="task.status === 'PENDING'"
                  type="success"
                  size="small"
                  @click="completeTask(task.id)"
                  :loading="completingTaskId === task.id"
                >
                  <el-icon style="vertical-align: middle; margin-right: 5px">
                    <Check />
                  </el-icon>
                  标记完成
                </el-button>
                <el-tag v-else type="success" class="completed-tag">
                  <el-icon style="vertical-align: middle; margin-right: 5px">
                    <SuccessFilled />
                  </el-icon>
                  已完成
                </el-tag>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, defineExpose } from 'vue'
import { ElMessage } from 'element-plus'
import { Check, SuccessFilled } from '@element-plus/icons-vue'
import { studyApi } from '@/services/api'

const tasks = ref([])
const completingTaskId = ref(null)

// 分类类型映射
const categoryMap = {
  programming: { name: '编程开发', type: 'primary' },
  language: { name: '语言学习', type: 'success' },
  math: { name: '数学逻辑', type: 'warning' },
  science: { name: '科学技术', type: 'info' },
  humanity: { name: '人文历史', type: 'danger' },
  other: { name: '其他', type: '' },
}

const getCategoryName = (category) => {
  return categoryMap[category]?.name || category
}

const getCategoryType = (category) => {
  return categoryMap[category]?.type || ''
}

const getTimelineType = (status) => {
  return status === 'COMPLETED' ? 'success' : 'primary'
}

const getTaskIcon = (status) => {
  return status === 'COMPLETED' ? 'SuccessFilled' : 'Clock'
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const loadTodayTasks = async () => {
  try {
    const response = await studyApi.getTodayTasks()
    tasks.value = response.data
  } catch (error) {
    console.error('加载任务失败:', error)
    ElMessage.error('加载任务失败')
  }
}

const completeTask = async (taskId) => {
  completingTaskId.value = taskId
  try {
    await studyApi.completeTask(taskId)
    ElMessage.success({
      message: '任务完成！继续加油！💪',
      duration: 2000,
    })
    await loadTodayTasks() // 重新加载任务列表
  } catch (error) {
    console.error('完成任务失败:', error)
    ElMessage.error('操作失败')
  } finally {
    completingTaskId.value = null
  }
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

.task-card {
  transition: all 0.3s ease;
}

.task-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1) !important;
}

.task-card.completed {
  opacity: 0.8;
  background-color: #f8fff9;
}

.task-title {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 16px;
}

.task-desc {
  margin: 0 0 12px 0;
  color: #606266;
  line-height: 1.5;
  font-size: 14px;
}

.task-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.category-tag,
.difficulty-tag {
  font-size: 12px;
}

.study-date {
  font-size: 12px;
  color: #909399;
}

.task-actions {
  display: flex;
  justify-content: flex-end;
}

.completed-tag {
  font-size: 12px;
}

.empty-state {
  padding: 40px 0;
}
</style>
