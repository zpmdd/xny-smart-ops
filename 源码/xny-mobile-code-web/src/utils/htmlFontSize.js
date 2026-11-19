// 导出默认组件配置
export default {
  // 初始化数据对象，包含一个resizeTimer属性用于处理重置字体大小的定时器
  data () {
    return {
      resizeTimer: null
    }
  },
  // 组件创建时执行方法
  created () {
    // 首次初始化字体大小
    this.initFontSize()
    
    // 监听窗口尺寸变化事件
    window.addEventListener("resize", ()=>{
      // 清除当前定时器
      clearTimeout(this.resizeTimer)
      
      // 设置新的定时器，在50毫秒后重新初始化字体大小并触发全局窗口尺寸改变事件
      this.resizeTimer = setTimeout(() => {
        this.initFontSize()
        this.$bus.$emit('onresize')
      }, 50)
    })
  },
  // 定义组件方法
  methods: {
    // 初始化字体大小的方法
    initFontSize () {
      // 获取本地存储的视图高度值
      let width = localStorage.getItem('viewWidth')
      
      // 计算并设置html元素的基础字体大小（rem单位）
      const rem = Number(width / 100).toFixed(4)
      document.querySelector('html').style.fontSize = `${rem}px`
    }
  }
}