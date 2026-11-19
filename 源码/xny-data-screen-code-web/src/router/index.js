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
    redirect: '/country',
    children: [
      {
        path: '/country',
        component: () => import('@/page/country/index'),
        name: 'country',
      },
      {
        path: '/province',
        component: () => import('@/page/province/index'),
        name: 'province',
      },
      {
        path: '/fbsgf',
        component: () => import('@/page/fbsgf/index'),
        name: 'fbsgf',
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
          {
            path: 'clear',
            component: () => import('@/page/fbsgf/view/clear'),
            name: 'clear',
          },
        ],
      },
      {
        path: '/jzsgf',
        component: () => import('@/page/jzsgf/index'),
        name: 'jzsgf',
        redirect: '/jzsgf/pvGlobalOverview',
        children: [
          {
            path: 'pvGlobalOverview',
            component: () => import('@/page/jzsgf/view/pvGlobalOverview'),
            name: 'jzsgfPvGlobalOverview',
          },
          {
            path: 'inverterMonitoring',
            component: () => import('@/page/jzsgf/view/inverterMonitoring'),
            name: 'jzsgfInverterMonitoring',
          },
          {
            path: 'dataStatistics',
            component: () => import('@/page/jzsgf/view/dataStatistics'),
            name: 'jzsgfDataStatistics',
          },
        ],
      },
      {
        path: 'fdxm',
        component: () => import('@/page/fdxm/index'),
        name: 'fdxm',
        redirect: '/fdxm/windGlobalView',
        children: [
          {
            path: 'windGlobalView',
            component: () => import('@/page/fdxm/view/windGlobalView'),
            name: 'windGlobalView',
          },
          {
            path: 'blowerMonitor',
            component: () => import('@/page/fdxm/view/blowerMonitor'),
            name: 'blowerMonitor',
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
        path: '/sysuser',
        name: 'sysuser',
        component: () => import('@/page/sysUser/index'),
        meta: {
          roles: ['user'],
        },
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import(/* webpackChunkName: "login" */ '@/page/login/login'),
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "404" */ '@/page/404'),
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
