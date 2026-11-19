import axios from '@/utils/request'

// 逆变器监测
export const pilesMonitor = (data) => {
  return axios({
    url: '/recharge/pilesMonitor',
    method: 'post',
    data,
  })
}

// 充电桩监测
export const chargeStationMonitor = (data) => {
  return axios({
    url: '/recharge/chargeStationMonitor',
    method: 'post',
    data,
  })
}
