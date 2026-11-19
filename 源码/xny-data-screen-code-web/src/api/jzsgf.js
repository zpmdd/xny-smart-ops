import axios from '@/utils/request'

// 集中式光伏全局总览
export const burstingGlobalView = (data) => {
  return axios({
    url: '/burstingPv/burstingGlobalView',
    method: 'post',
    data,
  })
}

// 数据统计
export const burstingDataStatistics = (data) => {
  return axios({
    url: '/burstingPv/burstingDataStatistics',
    method: 'post',
    data,
  })
}

// 逆变器监测
export const bpvInverterMonitoring = (data) => {
  return axios({
    url: '/burstingPv/bpvInverterMonitoring',
    method: 'post',
    data,
  })
}

// 子阵列表
export const listSubmatrix = (data) => {
  return axios({
    url: '/burstingPv/listSubmatrix',
    method: 'post',
    data,
  })
}

