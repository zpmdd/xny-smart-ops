<template>
  <div class="cdxm-inverterMonitoring view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="topInfo-content">
        <div class="topInfo-item" v-for="(item, index) in topInfo.childs || []" :key="index" @click="handleTopInfo(item)">
          <div class="value">
            {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit || '' }}</span>
            <span class="router-icon" v-if="item.path"></span>
          </div>
          <div class="label">
            <div class="l-icon"></div>
            <div class="name">{{ item.label }}</div>
            <div class="r-icon"></div>
          </div>
        </div>
      </div>
      <div class="runningMonitor-content">
        <div class="runningMonitor-item" v-for="(item, index) in runningMonitor.childs || []" :key="index">
          <div class="icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.runningMonitor[index < 3 ? 0 : 1]})` }"></div>
          <div class="values">
            <div class="label" v-html="item.label"></div>
            <div class="value">
              {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="charts-content">
        <div class="l">
          <TextTittle :label="chart1Data.label || ''"></TextTittle>
          <div class="charts" ref="chart1"></div>
          <TextTittle :label="chart3Data.label || ''"></TextTittle>
          <div class="charts" ref="chart3"></div>
        </div>
        <div class="r">
          <TextTittle :label="chart2Data.label || ''"></TextTittle>
          <div class="charts" ref="chart2"></div>
          <TextTittle :label="chart4Data.label || ''"></TextTittle>
          <div class="charts" ref="chart4"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { pilesMonitor } from '@/api/cdxm'
export default {
  name: 'cdxmInverterMonitoring',
  data() {
    return {
      projectIcons: icons,

      topInfo: {},
      runningMonitor: {},
      chart1Data: {},
      chart1Dom: null,
      chart2Data: {},
      chart2Dom: null,
      chart3Data: {},
      chart3Dom: null,
      chart4Data: {},
      chart4Dom: null,
    }
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
    handleTopInfo(obj){
      if(obj.value==='充电管理平台'){
        window.open(process.env.VUE_APP_CHARGE_MANAGEMENT)
      }
    },
    initData() {
      pilesMonitor().then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.topInfo.childs.push({
          label: '点击前往',
          value: '充电管理平台',
          path: '/',
        })
        this.runningMonitor = data.runningMonitor || {}
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '1'
        this.chart1Data.childs[0].color = '6, 94, 225'
        this.chart2Data = data.chartList[1]
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '41, 245, 175'
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '30, 161, 153'
        this.chart4Data = data.chartList[3]
        this.chart4Data.childs[0].style = '0'
        this.chart4Data.childs[0].color = '29, 210, 255'
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needStart: true,
          needLegend: false,
          vm: this,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
          needLegend: false,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needLegend: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        this.chart3Dom.resize()
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          needLegend: false,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
        this.chart4Dom.resize()
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
  },
}
</script>
<style lang="scss" scoped>
.cdxm-inverterMonitoring {
  .topInfo-content {
    margin-bottom: 25px;
    display: flex;
    flex-wrap: wrap;
    gap: 25px 32px;
    .topInfo-item {
      width: 346px;
      .value {
        min-height: 78px;
        text-align: center;
        padding-top: 12px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/cnxm/bg_esbm04.png');
        font-size: 32px;
        color: #fff;
        .unit {
          font-size: 18px;
        }
        .router-icon {
          height: 20px;
          width: 20px;
          display: inline-block;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/cdxm/icon_cpi0.png');
          vertical-align: middle;
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
      &:last-child {
        cursor: pointer;
        .value {
          padding-top: 20px;
          font-size: 24px;
          background-image: url('~@/assets/img/cdxm/bg_cpi04.png');
        }
      }
    }
  }
  .runningMonitor-content {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 29px;
    justify-content: space-around;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/cdxm/icon_cpi01.png');
    padding: 18px 0;
    .runningMonitor-item {
      display: flex;
      .icon {
        width: 53px;
        height: 54px;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        margin-right: 7px;
      }
      .values {
        width: 85px;
        .label {
          color: #fff;
          font-size: 14px;
          background: linear-gradient(90deg, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0));
          border-left: 2px solid #80c5f0;
          padding: 0 6px;
        }
        .value {
          color: #86d9ff;
          font-size: 24px;
          font-weight: bold;
          .unit {
            font-size: 14px;
            font-weight: 400;
          }
        }
      }
    }
  }
  .charts-content {
    display: flex;
    .l {
      width: 530px;
    }
    .r {
      flex: 1;
    }
    .charts {
      height: 295px;
    }
  }
}
</style>
