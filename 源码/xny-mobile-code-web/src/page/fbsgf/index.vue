<template>
  <div class="fzsgf page">
    <MHeader text="项目详情" backBtn></MHeader>
    <TabBar :active.sync="active" :tabList="tabList"></TabBar>
    <div class="child-tabbar container">
      <TabBar :active.sync="childActive" :tabList="tabChildList" child></TabBar>
    </div>
    <div class="page-content">
      <router-view></router-view>
    </div>
  </div>
</template>
<script>
import TabBar from '../components/Tabbar'
export default {
  data() {
    return {
      active: 0,
      childActive:0,
      tabList: [
        {
          name: '分布式光伏',
          path: '/fbsgf/pvGlobalOverview',
        },
        {
          name: '储能',
          path: '/cnxm/deviceMonitor',
        },
        {
          name: '充电',
          path: '/cdxm/chargingStation',
        },
      ],
      tabChildList: [
        {
          name: '全局总览',
          path: '/fbsgf/pvGlobalOverview',
        },
        {
          name: '系统架构图',
          path: '/fbsgf/systemArchitecture',
        },
        {
          name: '逆变器监测',
          path: '/fbsgf/inverterMonitoring',
        },
        // {
        //   name: '数据统计',
        //   path: '/fbsgf/dataStatistics',
        // },
      ],
    }
  },
  components: {
    TabBar,
  },
}
</script>
<style lang="scss" scoped>
.fzsgf {
}
</style>
