import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 50000, // request timeout
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
  },
})

// request interceptor
service.interceptors.request.use(
  (config) => {

    config.headers['ctype'] = 'pc'
    if (store.getters.token) {
      config.headers['xny-token'] = store.getters.token
    }
    config.data = config.data === undefined ? {} : config.data
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 1) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000,
      })
      return Promise.reject(new Error(res.message || 'Error'))
    } else if (res.code == 2) {
      store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    } else {
      return res
    }
  },
  (error) => {
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000,
    })
    return Promise.reject(error)
  }
)

export default service
