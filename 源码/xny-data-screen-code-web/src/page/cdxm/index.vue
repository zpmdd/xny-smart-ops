<template>
  <div class="cdxm-detail">
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
          label: '充电桩监测',
          path: '/cdxm/chargingStation',
        },
        // {
        //   label: '逆变器监测',
        //   path: '/cdxm/inverterMonitoring',
        // },
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
.cdxm-detail {
  height: 100%;
}
</style>
