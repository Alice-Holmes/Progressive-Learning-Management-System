<template>
  <div class="study-input">
    <el-card header="📚 今天学了什么？" shadow="hover">
      <el-form :model="studyForm" label-width="100px">
        <el-form-item label="学习标题">
          <el-input
            v-model="studyForm.title"
            placeholder="简要概括学习内容，如：Spring Boot基础"
            clearable
          />
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="studyForm.category" placeholder="选择分类" style="width: 100%">
            <el-option label="编程开发" value="programming" />
            <el-option label="语言学习" value="language" />
            <el-option label="数学逻辑" value="math" />
            <el-option label="科学技术" value="science" />
            <el-option label="人文历史" value="humanity" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>

        <el-form-item label="难度等级">
          <el-rate
            v-model="studyForm.difficultyLevel"
            :max="5"
            show-text
            :texts="['很简单', '简单', '一般', '困难', '很困难']"
          />
        </el-form-item>

        <el-form-item label="详细内容">
          <el-input
            v-model="studyForm.content"
            type="textarea"
            :rows="5"
            placeholder="详细记录学习内容、重点、心得..."
            resize="none"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="saveStudyContent"
            :loading="loading"
            size="large"
            style="width: 100%"
          >
            <el-icon style="vertical-align: middle; margin-right: 5px;">
              <Check />
            </el-icon>
            保存学习记录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import { studyApi } from '@/services/api'

const studyForm = reactive({
  title: '',
  category: '',
  difficultyLevel: 3,
  content: ''
})

const loading = ref(false)

const saveStudyContent = async () => {
  if (!studyForm.title.trim()) {
    ElMessage.error('请输入学习标题')
    return
  }

  if (!studyForm.content.trim()) {
    ElMessage.error('请输入学习内容')
    return
  }

  loading.value = true
  try {
    await studyApi.addStudyContent({
      ...studyForm,
      studyDate: new Date().toISOString().split('T')[0]
    })

    ElMessage.success({
      message: '学习记录保存成功！系统已自动生成复习计划 🎉',
      duration: 3000
    })

    // 清空表单
    Object.assign(studyForm, {
      title: '',
      category: '',
      difficultyLevel: 3,
      content: ''
    })

    // 触发事件通知父组件刷新任务列表
    emit('content-added')

  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

const emit = defineEmits(['content-added'])
</script>

<style scoped>
.study-input {
  margin-bottom: 20px;
}
</style>