<template>
  <div class="fbsgf-detail">
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
          path: '/fbsgf/pvGlobalOverview',
        },
        {
          label: '系统架构图',
          path: '/fbsgf/systemArchitecture',
        },
        {
          label: '逆变器监测',
          path: '/fbsgf/inverterMonitoring',
        },
        // {
        //   label: '数据统计',
        //   path: '/fbsgf/dataStatistics',
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
.fbsgf-detail {
  height: 100%;
}
</style>
