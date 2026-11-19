import axios from '@/utils/request'

// 首页数据
export const moBileHomePage = (data) => {
  return axios({
    url: '/moBileHomePage/moBileHomePage',
    method: 'post',
    data,
  })
}
