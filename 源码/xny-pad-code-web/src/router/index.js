import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/page/redirect'),
      },
    ],
  },
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/page/index/index'),
        name: 'index',
      },
    ],
  },
  {
    path: '/country',
    name: 'country',
    component: () => import('@/page/country/index'),
  },
  {
    path: '/province',
    name: 'province',
    component: () => import('@/page/province/index'),
  },
  {
    path: '/detail',
    name: 'detail',
    component: () => import('@/page/detail/index'),
    children: [
      {
        path: 'cnxm',
        name: 'cnxm',
        component: () => import('@/page/detail/cnxm/index'),
      },
      {
        path: 'fbsgf',
        name: 'fbsgf',
        component: () => import('@/page/detail/fbsgf/index'),
      },
      {
        path: 'jzsgf',
        name: 'jzsgf',
        component: () => import('@/page/detail/jzsgf/index'),
      },
      {
        path: 'cdxm',
        name: 'cdxmdetail',
        component: () => import('@/page/detail/cdxm/index'),
      },
      {
        path: 'cnxm',
        name: 'cdxmdetail',
        component: () => import('@/page/detail/cnxm/index'),
      },
      {
        path: 'fdxm',
        name: 'fdxmdetail',
        component: () => import('@/page/detail/fdxm/index'),
      },
      {
        path: 'sdxm',
        name: 'sdxmdetail',
        component: () => import('@/page/detail/sdxm/index'),
      },
    ],
  },

  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '@/page/login/login'),
  },

  {
    path: '*',
    component: () => import(/* webpackChunkName: "404" */ '@/page/404'),
  },
]
export const asyncRoutes = []
const createRouter = () =>
  new Router({
    mode: 'hash', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes,
  })

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
