<template>
  <div class="jzsgf page">
    <MHeader text="项目详情" backBtn></MHeader>
    <TabBar :active.sync="active" :tabList="tabList"></TabBar>
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
      tabList: [
        {
          name: '全局总览',
          path: '/jzsgf/pvGlobalOverview',
        },
        {
          name: '逆变器监测',
          path: '/jzsgf/inverterMonitoring',
        },
        // {
        //   name: '数据统计',
        //   path: '/jzsgf/dataStatistics',
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
.jzsgf {
}
</style>
