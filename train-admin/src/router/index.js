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

