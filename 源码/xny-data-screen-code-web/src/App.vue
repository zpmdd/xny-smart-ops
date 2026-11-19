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
</style>
