<template>
    <div class="jzsgf-detail">
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
            path: '/jzsgf/pvGlobalOverview',
          },
          {
            label: '逆变器监测',
            path: '/jzsgf/inverterMonitoring',
          },
          // {
          //   label: '数据统计',
          //   path: '/jzsgf/dataStatistics',
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
  .jzsgf-detail {
    height: 100%;
  }
  </style>
  