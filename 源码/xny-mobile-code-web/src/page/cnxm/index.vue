<template>
  <div class="cnxm page">
    <MHeader text="项目详情" backBtn></MHeader>
    <TabBar :active.sync="active" :tabList="tabList"></TabBar>
    <div class="child-tabbar container">
      <!-- <TabBar :active.sync="childActive" :tabList="tabChildList" child></TabBar> -->
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
      active: 1,
      childActive: 0,
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
          name: '储能设备监测',
          path: '/cnxm/deviceMonitor',
        },
        // {
        //   name: '储能电池监测',
        //   path: '/cnxm/batteryMonitor',
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
.cnxm {
}
</style>
