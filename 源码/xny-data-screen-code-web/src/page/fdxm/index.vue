<template>
  <div class="fdxm-detail">
    <Side :sideList="sideList" :projectName="projectName" :slideActive.sync="slideActive"></Side>
    <router-view></router-view>
  </div>
</template>
<script>
const Side = () => import('@/components/Side')
export default {
  data() {
    return {
      projectName: '',
      sideList: [
        {
          label: '全局总览',
          path: '/fdxm/windGlobalView',
        },
        {
          label: '风机监测',
          path: '/fdxm/blowerMonitor',
        },
      ],
      slideActive: 0,
    }
  },
  created() {
    this.projectName = this.$route.query.projectName
  },
  components: { Side },
}
</script>
<style lang="scss" scoped>
.fdxm-detail {
  height: 100%;
}
</style>
