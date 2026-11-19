<template>
  <div class="cnxm-detail">
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
          label: '储能设备监测',
          path: '/cnxm/deviceMonitor',
        },
        // {
        //   label: '储能电池监测',
        //   path: '/cnxm/batteryMonitor',
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
.cnxm-detail {
  height: 100%;
}
</style>
