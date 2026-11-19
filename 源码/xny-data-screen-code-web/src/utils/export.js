import axios from 'axios'
import store from '../store'
import * as FileSaver from 'file-saver'
import { MessageBox, Message } from 'element-ui'
export const exportData = (url, param) => {
  let config = {
    responseType: 'blob',
    headers: {
      'c-type': 'pc_front',
      'ies-token': store.getters.token
    }
  }
  axios.post(`${process.env.VUE_APP_BASE_API}${url}`, param, config).then(res => {
    const blob = new Blob([res.data]) // 创建一个类文件对象：Blob 对象表示一个不可变的、原始数据的类文件对象
    // URL.createObjectURL(object) 表示生成一个 File 对象或 Blob 对象
    const url = window.URL.createObjectURL(blob)
    const dom = document.createElement('a') // 设置一个隐藏的 a 标签，href 为输出流，设置 download
    dom.style.display = 'none'
    dom.href = url
    dom.setAttribute('download', '任务列表.xlsx') // 指示浏览器下载 url,而不是导航到它；因此将提示用户将其保存为本地文件
    document.body.appendChild(dom)
    dom.click()
    document.body.removeChild(dom) // 下载完成移除元素
    window.URL.revokeObjectURL(url) // 释放掉 blob 对象
  })
}

export const exportData1 = (url, param, method, name) => {
  let config = {
    responseType: 'blob',
    headers: {
      "c-type": "pc_front",
      "ies-token": store.getters.token,
    }
  }
  if (method == 'get') {
    axios({
      url: `${process.env.VUE_APP_BASE_API}${url}`,
      data: param,
      ...config
    }).then(response => {
      if (response.data.type == 'application/json') {
        const reader = new FileReader()
        reader.readAsText(response.data, 'utf-8')
        reader.onload = function() {
          const { message } = JSON.parse(reader.result)
          Message({
            message: message || 'Error',
            type: 'error'
          })
        }
        return
      }
      const contentDisposition = decodeURI(response.headers['content-disposition'])
      const filename = contentDisposition.split('=')[1]
      const blob = new Blob([response.data], { type: 'application/octet-stream' })
      FileSaver.saveAs(blob, name || filename) // 使用FileSaver.js保存文件
    })
    return
  }
  axios({
    url: `${process.env.VUE_APP_BASE_API}${url}`,
    method: 'POST',
    data: param,
    ...config
  }).then(response => {
    if (response.data.type == 'application/json') {
      const reader = new FileReader()
      reader.readAsText(response.data, 'utf-8')
      reader.onload = function() {
        const { message } = JSON.parse(reader.result)
        Message({
          message: message || 'Error',
          type: 'error'
        })
      }
      return
    }
    const contentDisposition = decodeURI(response.headers['content-disposition'])
    const filename = contentDisposition.split('=')[1]
    const blob = new Blob([response.data], { type: 'application/octet-stream' })
    FileSaver.saveAs(blob, filename) // 使用FileSaver.js保存文件
  })
}
