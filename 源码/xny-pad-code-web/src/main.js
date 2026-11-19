import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vueBus from '@/utils/vueBus'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import { vailtAccount, vailtPassword } from '@/utils/vaild.js'
Vue.prototype.vailtAccount = vailtAccount
Vue.prototype.vailtPassword = vailtPassword

import { formatSize } from '@/utils/echartsFontSize.js'
Vue.prototype.$formatSize = formatSize

import './permission.js'
import '@/assets/font/font.scss'
import './utils/polyfill'
import * as filters from './filters'
Object.keys(filters).forEach((key) => {
  Vue.filter(key, filters[key])
})

import * as echarts from 'echarts'
import 'echarts-gl'
Vue.prototype.$echarts = echarts

import { initChart } from '@/utils/echarts'
Vue.prototype.$initChart = initChart

Vue.config.productionTip = false
Vue.use(vueBus)
Vue.use(ElementUI)
new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app')
