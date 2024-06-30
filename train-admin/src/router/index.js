import { createRouter, createWebHistory } from 'vue-router'

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
      {
        path: '/train',
        component: () => import('../views/main/train.vue')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

