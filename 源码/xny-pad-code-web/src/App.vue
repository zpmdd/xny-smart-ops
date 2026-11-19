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
  computed: {},
  beforeCreate() {
    this.$store.dispatch('user/logout').then(() => {
      this.$store.commit('app/video', true)
      this.$router.push('/login')
    })
  },
  created() {
    localStorage.setItem('viewWidth', document.documentElement.clientWidth || document.body.clientWidth)
  },
}
</script>

<style lang="scss">
@import './assets/style/base.scss';
@import './assets/style/swiper.min.css';
#app {
  min-height: 1600px;
}
</style>
