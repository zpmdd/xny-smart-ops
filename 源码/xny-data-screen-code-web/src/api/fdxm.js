import axios from '@/utils/request'
// 全局总览
export const windGlobalView = (data) => {
  return axios({
    url: '/windPower/windGlobalView',
    method: 'post',
    data,
  })
}

// 风机监测
export const blowerMonitor = (data) => {
  return axios({
    url: '/windPower/blowerMonitor',
    method: 'post',
    data,
  })
}
