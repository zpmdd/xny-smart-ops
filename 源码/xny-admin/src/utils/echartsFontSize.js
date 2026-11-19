// 定义格式化尺寸函数
export const formatSize = (size) => {
  return Math.ceil(localStorage.getItem("viewWidth") / (1920 / size));
};
