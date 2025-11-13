import { createApp } from 'vue'
import { createPinia } from 'pinia' // 保留原有 Pinia 导入

// 1. 新增：引入 Element Plus 核心和样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 2. 新增：引入 Element Plus 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'

const app = createApp(App)

// 3. 新增：注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 保留原有配置（Pinia 和 Router）
app.use(createPinia())
app.use(router)
// 4. 新增：使用 Element Plus
app.use(ElementPlus)

app.mount('#app')