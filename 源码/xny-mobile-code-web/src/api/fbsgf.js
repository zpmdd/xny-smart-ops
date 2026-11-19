import axios from '@/utils/request'

// 分布式光伏全局总览
export const pvGlobalOverview = (data) => {
  return axios({
    url: '/dcsPv/pvGlobalOverview',
    method: 'post',
    data,
  })
}

// 系统架构
export const systemArchitecture = (data) => {
  return axios({
    url: '/dcsPv/systemArchitecture',
    method: 'post',
    data,
  })
}

// 逆变器监测
export const inverterMonitoring = (data) => {
  return axios({
    url: '/dcsPv/inverterMonitoring',
    method: 'post',
    data,
  })
}

// 数据统计
export const dataStatistics = (data) => {
  return axios({
    url: '/dcsPv/dataStatistics',
    method: 'post',
    data,
  })
}