import axios from '@/utils/request'

// 地图数据
export const mapData = (data) => {
  return axios({
    url: '/mapData/count',
    method: 'post',
    data,
  })
}

// 项目列表
export const listProject = (data) => {
  return axios({
    url: '/mapData/listProject',
    method: 'post',
    data,
  })
}
