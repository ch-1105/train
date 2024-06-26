import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";

const routes = [
  {
    path: '/',
    component: () => import('../views/main-page.vue'),
    meta: {
      loginRequired: true
    },
    children: [
      {
        path: '/welcome',
        component: () => import('../views/main/welcome-page.vue')
      },
      {
        path: '/station',
        component: () => import('../views/main/station.vue')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

