import Vue from 'vue'
import Router from 'vue-router'
import * as echarts from 'echarts'
import App from './App.vue'
import { overviewRoutes } from './router/overview'
import { initChart } from './utils/echarts'
import vueBus from './utils/vueBus'
import Header from './components/Header'
import TextTittle from './components/TextTittle'
import './assets/font/font.scss'

Vue.use(Router)
Vue.use(vueBus)
Vue.component('MyHeader', Header)
Vue.component('TextTittle', TextTittle)
Vue.prototype.$echarts = echarts
Vue.prototype.$initChart = initChart
Vue.config.productionTip = false
const router = new Router({
  routes: [
    ...overviewRoutes.map(route => ({ ...route, meta: { standalone: true } })),
    { path: '*', redirect: '/country' },
  ],
})
new Vue({ router, render: h => h(App) }).$mount('#app')
