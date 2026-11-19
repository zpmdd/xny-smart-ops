<template>
  <div class="jzsgf-dataStatistics view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="topInfo-content">
        <div class="topInfo-item" v-for="(item, index) in topInfo.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.fbsgf.topDataStatistics[index === 1 ? 2 : index === 2 ? 1 : index]})` }">
          <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
          <div class="value">{{ item.value }}</div>
        </div>
      </div>
      <div class="chart-content">
        <div class="chart-item">
          <TextTittle :label="chart1Data?.label || ''"></TextTittle>
          <div class="chart1 charts" ref="chart1"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart2Data?.label || ''"></TextTittle>
          <div class="chart2 charts" ref="chart2"></div>
        </div>
        <div class="chart-item">
          <div class="select">
            <TextTittle :label="chart3Data?.label || ''"></TextTittle>
            <div class="query">
              <div class="item" v-for="(item, index) in selectList" :key="index" @click="handleCheck(item)">
                <div class="icon" :class="{ chartActive: item.value === chartActive }"></div>
                <span>{{ item.label }}</span>
              </div>
            </div>
          </div>
          <div class="chart3 charts" ref="chart3"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart4Data?.label || ''"></TextTittle>
          <div class="chart4 charts" ref="chart4"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { burstingDataStatistics } from '@/api/jzsgf.js'
export default {
  name: 'jzsgfDataStatistics',
  data() {
    return {
      projectIcons: icons,

      topInfo: {},

      chart1Dom: null,
      chart2Dom: null,
      chart3Dom: null,
      chart4Dom: null,
      chart1Data: {},
      chart2Data: {},
      chart3Data: {},
      chart4Data: {},

      chartActive: 1,
      selectList: [
        {
          label: '日预测',
          value: 1,
        },
        {
          label: '周预测',
          value: 2,
        },
        {
          label: '月预测',
          value: 3,
        },
      ],
    }
  },
  watch: {
    chartActive() {
      burstingDataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '126, 184, 255'
        this.chart3Data.childs[1].style = '0'
        this.chart3Data.childs[1].color = '35, 229, 211'
        this.$nextTick(() => {
          this.initChart()
        })
      })
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
    handleCheck(obj) {
      this.chartActive = obj.value
    },
    initData() {
      burstingDataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.topInfo = data.topInfo
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '1'
        this.chart1Data.childs[0].color = '126, 184, 255'
        this.chart1Data.childs[1].style = '0'
        this.chart1Data.childs[1].color = '0, 255, 255'
        this.chart2Data = data.chartList[1]
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '29, 210, 255'
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '126, 184, 255'
        this.chart3Data.childs[1].style = '0'
        this.chart3Data.childs[1].color = '35, 229, 211'
        this.chart4Data = data.chartList[3]
        this.chart4Data.childs[0].style = '1'
        this.chart4Data.childs[0].color = '206, 173, 103'
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
          needShadow: false,
          smooth: false,
          vm: this,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
        let chart2Options = {
          data: this.chart2Data,
          needLegend: false,
          needStart: true,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        this.chart3Dom.resize()
        let chart4Options = {
          data: this.chart4Data,
          needLegend: false,
          needStart: true,
          vm: this,
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
.jzsgf-dataStatistics {
  .view-container {
    .topInfo-content {
      display: flex;
      justify-content: space-between;
      .topInfo-item {
        width: 346px;
        height: 136px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding-left: 154px;
        padding-top: 35px;
        .label {
          color: #cde3f6;
          font-size: 16px;
        }
        .value {
          color: #f4fdff;
          font-size: 32px;
          font-family: douyv;
          word-break: break-all;
        }
      }
    }
    .chart-content {
      display: flex;
      flex-wrap: wrap;
      gap: 10px 15px;
      margin-top: 30px;
      .chart-item {
        height: 378px;
        width: 49%;
        .charts {
          height: 330px;
        }
        .select {
          position: relative;
          .query {
            position: absolute;
            right: 0;
            top: 6px;
            display: flex;
            align-items: center;
            // padding-right: 100px;
            .item {
              display: flex;
              align-items: center;
              cursor: pointer;
              font-size: 18px;
              color: #cde3f6;
              margin-left: 10px;
              .icon {
                height: 34px;
                width: 34px;
                background-size: cover;
                background-repeat: no-repeat;
                background-image: url('~@/assets/img/common/icon_select02.png');
              }
              .chartActive {
                background-image: url('~@/assets/img/common/icon_select01.png');
              }
            }
          }
        }
      }
    }
  }
}
</style>
