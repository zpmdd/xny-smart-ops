<template>
  <div id="app" :style="{ height: viewHeight }">
    <router-view />
  </div>
</template>

<script>
import htmlFontSize from '@/utils/htmlFontSize.js'
import { mapState } from 'vuex'
export default {
  name: 'App',
  mixins: [htmlFontSize],
  data() {
    return {
      viewHeight: '',
    }
  },
  computed: {},
  watch: {},
  beforeCreate() {
    // this.$store.dispatch('user/logout').then(() => {
    //   this.$router.push('/login')
    // })
  },
  created() {
    localStorage.setItem('viewWidth', document.documentElement.clientWidth || document.body.clientWidth)
    localStorage.setItem('viewHeight', document.documentElement.clientHeight || document.body.clientHeight)
  },
  mounted() {
    this.initFontSize()
    this.viewHeight = localStorage.getItem('viewHeight') + 'px'
  },
}
</script>

<style lang="scss">
@import './assets/style/base.scss';
@import './assets/style/swiper.min.css';
#app {
  height: 100%;
}
</style>
