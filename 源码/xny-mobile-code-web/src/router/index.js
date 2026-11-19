import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/',
    component: () => import('@/page/index/index'),
  },
  {
    path: '/project',
    component: () => import(/* webpackChunkName: "project" */ '@/page/project/index'),
  },
  {
    path: '/fbsgf',
    component: () => import('@/page/fbsgf/index'),
    redirect: '/fbsgf/pvGlobalOverview',
    children: [
      {
        path: 'pvGlobalOverview',
        component: () => import('@/page/fbsgf/view/pvGlobalOverview'),
        name: 'pvGlobalOverview',
      },
      {
        path: 'systemArchitecture',
        component: () => import('@/page/fbsgf/view/systemArchitecture'),
        name: 'systemArchitecture',
      },
      {
        path: 'inverterMonitoring',
        component: () => import('@/page/fbsgf/view/inverterMonitoring'),
        name: 'inverterMonitoring',
      },
      {
        path: 'dataStatistics',
        component: () => import('@/page/fbsgf/view/dataStatistics'),
        name: 'dataStatistics',
      },
    ],
  },
  {
    path: '/jzsgf',
    component: () => import(/* webpackChunkName: "jzsgf" */ '@/page/jzsgf/index'),
    redirect: '/jzsgf/pvGlobalOverview',
    children: [
      {
        path: 'pvGlobalOverview',
        name: 'jzsgf_pvGlobalOverview',
        component: () => import(/* webpackChunkName: "jzsgf" */ '@/page/jzsgf/view/pvGlobalOverview'),
      },
      {
        path: 'inverterMonitoring',
        name: 'jzsgf_inverterMonitoring',
        component: () => import(/* webpackChunkName: "jzsgf" */ '@/page/jzsgf/view/inverterMonitoring'),
      },
      {
        path: 'dataStatistics',
        name: 'jzsgf_dataStatistics',
        component: () => import(/* webpackChunkName: "jzsgf" */ '@/page/jzsgf/view/dataStatistics'),
      },
    ],
  },
  {
    path: '/cnxm',
    component: () => import('@/page/cnxm/index'),
    name: 'cnxm',
    redirect: '/cnxm/deviceMonitor',
    children: [
      {
        path: 'deviceMonitor',
        component: () => import('@/page/cnxm/view/deviceMonitor'),
        name: 'deviceMonitor',
      },
      {
        path: 'batteryMonitor',
        component: () => import('@/page/cnxm/view/batteryMonitor'),
        name: 'batteryMonitor',
      },
    ],
  },
  {
    path: '/cdxm',
    component: () => import('@/page/cdxm/index'),
    name: 'cdxm',
    redirect: '/cdxm/chargingStation',
    children: [
      {
        path: 'chargingStation',
        component: () => import('@/page/cdxm/view/chargingStation'),
        name: 'chargingStation',
      },
      {
        path: 'inverterMonitoring',
        component: () => import('@/page/cdxm/view/inverterMonitoring'),
        name: 'cdxmInverterMonitoring',
      },
    ],
  },
  {
    path: '/user',
    component: () => import(/* webpackChunkName: "user" */ '@/page/user/index'),
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
