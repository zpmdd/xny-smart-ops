<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import htmlFontSize from '@/utils/htmlFontSize.js'
export default {
  name: 'App',
  mixins: [htmlFontSize],
  watch: {},
  data() {
    return {
      resizeTimer: null,
      refreshTime: null,
    }
  },
  created() {
    clearInterval(this.refreshTime)
    this.refreshTime = setInterval(() => {
      this.$bus.$emit('refreshData')
    }, 5000)
  },
  mounted() {},
  methods: {},
}
</script>

<style lang="scss">
@import './assets/style/base.scss';
@import './assets/style/swiper.min.css';
#app {
  height: 100%;
}

/* Keep the three map and station screens in the 1920 × 1080 design area. */
html:has(.overview-screen) {
  font-size: min(1vw, 1.7777778vh) !important;
  background: #06101b;
  min-height: 100vh;
  display: grid;
  place-items: center;
  body { width: 100rem; margin: 0; }
  .overview-screen { position: relative; }
  .overview-back {
    position: absolute;
    z-index: 2;
    left: 38px;
    top: 86px;
    color: #b1e9ff;
    font-size: 16px;
  }
  a:focus-visible, button:focus-visible { outline: 2px solid #8ceaff; outline-offset: 2px; }
}
</style>
