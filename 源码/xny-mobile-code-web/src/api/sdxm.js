import axios from '@/utils/request'
// 全局总览
export const fdxm_waterGlobalView = (data) => {
  return axios({
    url: '/waterPower/waterGlobalView',
    method: 'post',
    data,
  })
}

