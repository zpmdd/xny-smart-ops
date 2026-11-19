// 定义格式化尺寸函数
export const formatSize = (size) => {
  // 计算并返回根据屏幕视图高度调整后的尺寸，其中'viewWidth'为本地存储的视图高度值，750为基准尺寸
  return localStorage.getItem('viewWidth') / (750 / size)
}