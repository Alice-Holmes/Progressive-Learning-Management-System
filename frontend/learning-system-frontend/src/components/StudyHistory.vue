<template>
  <div class="study-history">
    <el-card header="📖 学习历史" shadow="hover">
      <!-- 搜索和筛选 -->
      <div class="filter-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索学习内容..."
          clearable
          style="width: 300px; margin-right: 15px;"
          @clear="loadStudyContents"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-select v-model="filterCategory" placeholder="全部分类" clearable @change="loadStudyContents">
          <el-option label="全部分类" value="" />
          <el-option
            v-for="category in categories"
            :key="category.value"
            :label="category.label"
            :value="category.value"
          />
        </el-select>
      </div>

      <!-- 学习内容列表 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="studyContents.length === 0" class="empty-state">
        <el-empty description="还没有学习记录，开始你的第一份学习吧！" />
      </div>

      <el-timeline v-else>
        <el-timeline-item
          v-for="content in studyContents"
          :key="content.id"
          :timestamp="formatDate(content.studyDate)"
          placement="top"
        >
          <el-card shadow="hover" class="content-card">
            <div class="content-header">
              <h3 class="content-title">{{ content.title }}</h3>
              <div class="content-actions">
                <el-button
                  type="danger"
                  size="small"
                  link
                  @click="confirmDelete(content.id)"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </div>

            <div class="content-body">
              <p class="content-text">{{ content.content }}</p>

              <div class="content-meta">
                <el-tag
                  :type="getCategoryType(content.category)"
                  size="small"
                >
                  {{ getCategoryName(content.category) }}
                </el-tag>
                <el-tag size="small" type="info">
                  难度: {{ '⭐'.repeat(content.difficultyLevel) }}
                </el-tag>
                <span class="create-time">
                  记录于: {{ formatDateTime(content.createdTime) }}
                </span>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>

      <!-- 分页 -->
      <div v-if="studyContents.length > 0" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="totalCount"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Delete } from '@element-plus/icons-vue'
import { studyApi } from '@/services/api'

const studyContents = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const filterCategory = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(0)

// 分类选项
const categories = [
  { label: '编程开发', value: 'programming' },
  { label: '语言学习', value: 'language' },
  { label: '数学逻辑', value: 'math' },
  { label: '科学技术', value: 'science' },
  { label: '人文历史', value: 'humanity' },
  { label: '其他', value: 'other' }
]

// 分类类型映射
const categoryMap = {
  programming: { name: '编程开发', type: 'primary' },
  language: { name: '语言学习', type: 'success' },
  math: { name: '数学逻辑', type: 'warning' },
  science: { name: '科学技术', type: 'info' },
  humanity: { name: '人文历史', type: 'danger' },
  other: { name: '其他', type: '' }
}

const getCategoryName = (category) => {
  return categoryMap[category]?.name || category
}

const getCategoryType = (category) => {
  return categoryMap[category]?.type || ''
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
}

const formatDateTime = (dateTimeString) => {
  return new Date(dateTimeString).toLocaleString('zh-CN')
}

const loadStudyContents = async () => {
  loading.value = true
  try {
    const response = await studyApi.getAllStudyContents()
    let contents = response.data

    // 应用筛选
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase()
      contents = contents.filter(content =>
        content.title.toLowerCase().includes(keyword) ||
        content.content.toLowerCase().includes(keyword)
      )
    }

    if (filterCategory.value) {
      contents = contents.filter(content => content.category === filterCategory.value)
    }

    // 分页处理
    totalCount.value = contents.length
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    studyContents.value = contents.slice(start, end)

  } catch (error) {
    console.error('加载学习历史失败:', error)
    ElMessage.error('加载学习历史失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadStudyContents()
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
  loadStudyContents()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  loadStudyContents()
}

const confirmDelete = async (contentId) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条学习记录吗？相关的复习任务也会被删除，此操作不可恢复。',
      '确认删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )

    await studyApi.deleteStudyContent(contentId)
    ElMessage.success('删除成功')
    loadStudyContents() // 重新加载列表

    // 通知父组件更新
    emit('content-deleted')

  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const emit = defineEmits(['content-deleted'])

onMounted(() => {
  loadStudyContents()
})

// 暴露刷新方法
defineExpose({
  refresh: loadStudyContents
})
</script>

<style scoped>
.study-history {
  margin-bottom: 20px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.content-card {
  margin: 10px 0;
  transition: all 0.3s ease;
}

.content-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1) !important;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.content-title {
  margin: 0;
  color: #303133;
  font-size: 16px;
  line-height: 1.4;
  flex: 1;
  margin-right: 15px;
}

.content-actions {
  flex-shrink: 0;
}

.content-body {
  padding-top: 8px;
  border-top: 1px solid #f0f0f0;
}

.content-text {
  margin: 0 0 12px 0;
  color: #606266;
  line-height: 1.6;
  font-size: 14px;
  white-space: pre-wrap;
  word-break: break-word;
}

.content-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.create-time {
  font-size: 12px;
  color: #909399;
  margin-left: auto;
}

.loading-container {
  padding: 20px 0;
}

.empty-state {
  padding: 40px 0;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

@media (max-width: 768px) {
  .filter-bar {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-bar .el-input {
    width: 100% !important;
    margin-right: 0 !important;
    margin-bottom: 10px;
  }

  .content-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .content-title {
    margin-right: 0;
    margin-bottom: 8px;
  }

  .content-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }

  .create-time {
    margin-left: 0;
  }
}
</style>