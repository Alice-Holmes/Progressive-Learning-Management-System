<template>
  <el-card shadow="never" :class="['task-card', task.status === 'COMPLETED' ? 'completed' : '']">
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
        <span class="study-date"> 学习日期: {{ formatDate(task.studyContent.studyDate) }} </span>
      </div>

      <div class="task-actions" v-if="!readonly">
        <el-button type="success" size="small" @click="completeTask(task.id)" :loading="completing">
          <el-icon style="vertical-align: middle; margin-right: 5px">
            <Check />
          </el-icon>
          标记完成
        </el-button>
      </div>

      <div v-else class="task-completed-info">
        <el-tag type="success" class="completed-tag">
          <el-icon style="vertical-align: middle; margin-right: 5px">
            <SuccessFilled />
          </el-icon>
          已完成于 {{ formatDateTime(task.completedTime) }}
        </el-tag>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Check, SuccessFilled } from '@element-plus/icons-vue'
import { studyApi } from '@/services/api'

const props = defineProps({
  task: {
    type: Object,
    required: true,
  },
  readonly: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['task-completed'])
const completing = ref(false)

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

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''
  return new Date(dateTimeString).toLocaleString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

const completeTask = async (taskId) => {
  completing.value = true
  try {
    await studyApi.completeTask(taskId)
    emit('task-completed')
  } catch (error) {
    console.error('完成任务失败:', error)
    ElMessage.error('操作失败')
  } finally {
    completing.value = false
  }
}
</script>

<style scoped>
.task-card {
  transition: all 0.3s ease;
  margin: 5px 0;
}

.task-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1) !important;
}

.task-card.completed {
  opacity: 0.8;
  background-color: #f8fff9;
}

.task-title {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 15px;
  line-height: 1.4;
}

.task-desc {
  margin: 0 0 12px 0;
  color: #606266;
  line-height: 1.5;
  font-size: 14px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
  margin-left: auto;
}

.task-actions,
.task-completed-info {
  display: flex;
  justify-content: flex-end;
}

.completed-tag {
  font-size: 12px;
}

@media (max-width: 768px) {
  .task-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }

  .study-date {
    margin-left: 0;
  }
}
</style>
