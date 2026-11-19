export const formatDate = () => {
  const date = new Date()
  const y = date.getFullYear()
  const m = date.getMonth() + 1
  const d = date.getDate()
  const h = date.getHours()
  const mi = date.getMinutes()
  const s = date.getSeconds()
  const week = date.getDay()
  const weekArr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  return {
    formatWeek: weekArr[week],
    formatTime: `${h < 10 ? '0' + h : h}:${mi < 10 ? '0' + mi : mi}:${s < 10 ? '0' + s : s}`,
    formatDate: `${y} ${m < 10 ? '0' + m : m}-${d < 10 ? '0' + d : d}`,
  }
}
