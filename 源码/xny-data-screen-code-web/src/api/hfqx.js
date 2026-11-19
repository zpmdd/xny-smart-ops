import axios from '@/utils/request'

// 全局总览
export const hfqxGlobalView = (data) => {
  return axios({
    url: '/hfqx/hfqxGlobalView',
    method: 'post',
    data,
  })
}
// 设备详情
export const detailEquipment = (data) => {
  return axios({
    url: '/hfqx/detailEquipment',
    method: 'post',
    data,
  })
}
