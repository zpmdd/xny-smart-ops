// 定义格式化尺寸函数
export const formatSize = (size) => {
  let width = document.documentElement.clientWidth || document.body.clientWidth
  // 计算并返回根据屏幕视图高度调整后的尺寸，其中'viewHeight'为本地存储的视图高度值，1920为基准尺寸
  return width / (1920 / size)
}
