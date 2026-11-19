<template>
  <div class="cnxm-batteryMonitor view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="topInfo-content">
        <div class="topInfo-item" :class="matteryStatus(item, index)" v-for="(item, index) in topInfo.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.cnxm.topInfo[index]})` }">
          <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}<i class="status-icon"></i></div>
          <div class="value">{{ item.value }}</div>
        </div>
      </div>
      <TextTittle :label="runningMonitor.label || ''"></TextTittle>
      <div class="runningMonitor-content">
        <div class="runningMonitor-item" v-for="(item, index) in runningMonitor.childs || []" :key="index">
          <div class="value">
            {{ item.value | formatNum }} <span class="unit" v-if="item.unit">{{ item.unit || '' }}</span>
          </div>
          <div class="label">
            <div class="l-icon"></div>
            <div class="name">{{ item.label }}</div>
            <div class="r-icon"></div>
          </div>
        </div>
      </div>
      <div class="content-bottom">
        <div class="l">
          <TextTittle :label="abnormalAlarm.label || ''"></TextTittle>
          <div class="abnormalAlarm-content">
            <div class="abnormalAlarm-item" v-for="(item, index) in abnormalAlarm.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="r">
          <TextTittle :label="chart1Data.label || ''"></TextTittle>
          <div class="chart-content" ref="chart1"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { batteryMonitor } from '@/api/cnxm'
export default {
  data() {
    return {
      projectIcons: icons,

      //顶部信息
      topInfo: {},
      // 运行监测
      runningMonitor: {},
      // 异常状态分析
      abnormalAlarm: {},
      chart1Dom: null,
      chart1Data: {},
    }
  },
  computed: {
    matteryStatus() {
      return (obj, index) => {
        if (index !== 0) {
          return ''
        }
        if (obj.value == '充电中') {
          return 'charge'
        }
        if (obj.value == '放电中') {
          return 'discharge'
        }
        if (obj.value == '故障') {
          return 'fault'
        }
        if (obj.value == '待机') {
          return 'idle'
        }
        return ''
      }
    },
  },
  created() {
    this.initData()
  },
  mounted() {
    this.$bus.$on('onresize', () => {
      this.initChart()
    })
  },
  methods: {
    initData() {
      batteryMonitor().then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.runningMonitor = data.runningMonitor || {}
        this.abnormalAlarm = data.abnormalAlarm || {}
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '14, 156, 255'
        this.chart1Data.childs[1].style = '0'
        this.chart1Data.childs[1].color = '33, 213, 164'
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needStart: true,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
  },
}
</script>
<style lang="scss" scoped>
.cnxm-batteryMonitor {
  .view-container {
    .topInfo-content {
      display: flex;
      justify-content: space-between;
      margin-bottom: 40px;
      .topInfo-item {
        width: 346px;
        height: 136px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding-left: 150px;
        padding-top: 36px;
        .label {
          color: #cde3f6;
          font-size: 16px;
          position: relative;
          .status-icon {
            margin-left: 10px;
            display: inline-block;
            vertical-align: middle;
            height: 17px;
            width: 17px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
        }
        .value {
          color: #cde3f6;
          font-size: 24px;
          font-family: douyv;
          margin-top: 10px;
          word-break: break-all;
        }
      }
      .charge {
        background-image: url('~@/assets/img/cnxm/bg_esbm01.png') !important;
        .status-icon {
          background-image: url('~@/assets/img/cnxm/bg_battery_0111.png') !important;
        }
      }
      .discharge {
        background-image: url('~@/assets/img/cnxm/se_bg_battery_012.png') !important;
        .status-icon {
          background-image: url('~@/assets/img/cnxm/bg_battery_0122.png') !important;
        }
      }
      .fault {
        background-image: url('~@/assets/img/cnxm/bg_esbm01.png') !important;
        .status-icon {
          background-image: url('~@/assets/img/cnxm/bg_battery_0111.png') !important;
        }
      }
      .idle {
        background-image: url('~@/assets/img/cnxm/se_bg_battery_01.png') !important;
        .status-icon {
          background-image: url('~@/assets/img/cnxm/bg_battery_0133.png') !important;
        }
      }
    }
    .runningMonitor-content {
      margin-top: 10px;
      margin-bottom: 25px;
      display: flex;
      flex-wrap: wrap;
      gap: 25px 32px;
      .runningMonitor-item {
        width: 346px;
        .value {
          min-height: 78px;
          text-align: center;
          padding-top: 12px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/cnxm/bg_esbm04.png');
          font-size: 34px;
          color: #fff;
          .unit {
            font-size: 18px;
          }
        }
        .label {
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 19px;
          margin-top: 10px;
          color: #fff;
          .name {
            text-align: center;
            padding: 0 10px;
          }
          .l-icon,
          .r-icon {
            height: 24px;
            width: 21px;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            background-position: center center;
          }
          .l-icon {
            background-image: url('~@/assets/img/common/bg_energy_equipment08.png');
          }
          .r-icon {
            background-image: url('~@/assets/img/common/bg_energy_equipment0509.png');
          }
        }
      }
    }
    .content-bottom {
      display: flex;
      justify-content: space-between;
      .l {
        width: 492px;
        .abnormalAlarm-content {
          margin-top: 20px;
          height: 370px;
          overflow-y: auto;
          .abnormalAlarm-item {
            width: 492px;
            height: 61px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cnxm/bg_esbm05.png');
            margin-top: 8px;
            &:first-child {
              margin-top: 0;
            }
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-left: 67px;
            .label {
              font-size: 16px;
              font-weight: bold;
              color: #eef3ff;
              width: 160px;
            }
            .value {
              width: 238px;
              text-align: center;
              span {
                font-size: 28px;
                font-weight: bold;
                background: linear-gradient(0deg, #ffbfbf 0%, rgba(255, 255, 255, 0.87) 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
              }
            }
          }
        }
      }
      .r {
        width: 1300px;
        .chart-content {
          height: 370px;
        }
      }
    }
  }
}
</style>
