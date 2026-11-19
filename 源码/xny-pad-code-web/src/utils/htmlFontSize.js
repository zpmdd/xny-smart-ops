
export default {
  data () {
    return {
      resizeTimer: null
    }
  },
  created () {
    this.initFontSize()
    window.addEventListener("resize", ()=>{
      clearTimeout(this.resizeTimer)
      this.resizeTimer = setTimeout(() => {
        this.initFontSize()
        this.$bus.$emit('onresize')
      },50)
    })
  },
  methods: {
    initFontSize () {
      let width = localStorage.getItem('viewWidth')
      const rem =  Number(width / 100).toFixed(4)
      document.querySelector('html').style.fontSize = `${rem}px`
    }
  }
}