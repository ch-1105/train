import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue'

// createApp(App).use(antd).use(store).use(router).mount('#app')

const app = createApp(App)
app.use(antd).use(store).use(router).mount('#app')

// 全局引用icon
const icons = Icons
for (const i in icons) {
    app.component(i,icons[i]);
}