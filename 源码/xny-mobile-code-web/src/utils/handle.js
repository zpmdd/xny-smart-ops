import store from '@/store'

// 处理提示信息
export const handleTip = (status) => {
  // 如果已有定时器存在，则清除定时器并更新状态
  if (window.tipTimes) {
    clearTimeout(window.tipTimes)
    store.commit('status/leftTip', status)
  }
  // 如果有提示信息，则设置定时器在2秒后更新状态为false
  if (status) {
    window.tipTimes = setTimeout(() => {
      store.commit('status/leftTip', false)
    }, 2000)
  } else {
    // 如果没有提示信息，则直接更新状态
    store.commit('status/leftTip', status)
  }
}