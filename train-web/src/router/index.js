import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";

const routes = [
  {
    path: '/',
    component: () => import('../views/login-page.vue')
  },
  {
    path: '/welcome',
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
        path: '/passenger',
        component: () => import('../views/main/passenger-page.vue')
      },
      {
        path: '/ticket',
        component: () => import('../views/main/ticket.vue')
      },
      {
        path: '/order',
        component: () => import('../views/main/order-view.vue')
      },
      {
        path: '/my-ticket',
        component: () => import('../views/main/my-ticket.vue')
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router


//路由登录拦截
router.beforeEach((to, from, next) => {
  //根据loginRequired属性判断是否需要拦截请求
  if (to.matched.some(function (item) {
    console.log(item,"是否需要拦截判断",item.meta.loginRequired || false);
    return item.meta.loginRequired
  })) {
    const user = store.state.member
    console.log("校验页面登录请求中：",user);
    if (!user.token) {
      console.log("用户未登录或token失效",user)
      next({
        path: '/'
      })
    } else {
      next()
    }
  }else{
    next()
  }
})

