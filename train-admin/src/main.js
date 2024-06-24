import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue'
import axios from "axios";
import "./assets/js/enum"

// createApp(App).use(antd).use(store).use(router).mount('#app')

const app = createApp(App)
app.use(antd).use(store).use(router).mount('#app')

// 全局引用icon
const icons = Icons
for (const i in icons) {
    app.component(i,icons[i]);
}

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    const response = error.response;
    const status = response.code;
    return Promise.reject(error);
});
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
console.log('axios.defaults.baseURL：', axios.defaults.baseURL);
console.log('环境：', process.env.NODE_ENV);
console.log('服务端地址：', process.env.VUE_APP_SERVER);