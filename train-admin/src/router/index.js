import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    component: () => import('../views/main-page.vue'),
    meta: {
      loginRequired: true
    },
    children: [
      {
        path: '/',
        component: () => import('../views/main/welcome-page.vue')
      },
      {
        path: '/train',
        children: [
          {
            path: '/station',
            component: () => import('../views/main/train/station.vue')
          },
          {
            path: '/train',
            component: () => import('../views/main/train/train.vue')
          },
          {
            path: '/train-station',
            component: () => import('../views/main/train/train-station.vue')
          },
          {
            path: '/train-carriage',
            component: () => import('../views/main/train/train-carriage.vue')
          },
          {
            path: '/train-seat',
            component: () => import('../views/main/train/train-seat.vue')
          },
        ]
      },
      {
        path: '/daily',
        children: [
          {
            path: '/daily-train',
            component: () => import('../views/main/daily/daily-train.vue')
          },
          {
            path: '/daily-train-carriage',
            component: () => import('../views/main/daily/daily-train-carriage.vue')
          },
          {
            path: '/daily-train-seat',
            component: () => import('../views/main/daily/daily-train-seat.vue')
          },
          {
            path: '/daily-train-station',
            component: () => import('../views/main/daily/daily-train-station.vue')
          },
          {
            path: '/sk-token',
            component: () => import('../views/main/daily/sk-token.vue')
          },
          {
            path: '/confirm-order',
            component: () => import('../views/main/daily/confirm-order.vue')
          },
        ]
      },

      {
        path: '/timer/job',
        component: () => import('../views/main/timer/job.vue')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router

