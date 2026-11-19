<template>
  <div class="jzsgf_dataStatistics container">
    <div class="topInfo content-item">
      <div class="content">
        <div class="topInfo-c">
          <div class="label" v-if="topInfo.childs?.length">{{ topInfo.childs[0].label || '' }}</div>
          <div class="value" v-if="topInfo.childs?.length">
            {{ topInfo.childs[0].value }}
            <span class="unit" v-if="topInfo.childs[0].unit">{{ topInfo.childs[0].unit ? `/${topInfo.childs[0].unit}` : '' }}</span>
          </div>
        </div>
        <div class="topInfo-b" v-if="topInfo.childs?.length">
          <div class="topInfo-item" v-for="(item, index) in topInfo.childs.slice(1) || []" :key="index">
            <div class="value">
              {{ item.value || '' }}
            </div>
            <div class="label">
              {{ item.label || '' }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart1"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart3Data.label || ''">
        <el-select v-model="chartActive" placeholder="请选择"> <el-option v-for="item in selectList" :key="item.value" :label="item.label" :value="item.value"> </el-option> </el-select
      ></MTittle>
      <div class="chart-item" ref="chart3"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart2Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart2"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart4Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart4"></div>
    </div>
  </div>
</template>
<script>
import { burstingDataStatistics } from '@/api/jzsgf'
export default {
  name: 'jzsgf_dataStatistics',
  data() {
    return {
      topInfo: {},
      chart1Dom: null,
      chart1Data: {},
      chart2Dom: null,
      chart2Data: {},
      chart3Dom: null,
      chart3Data: {},
      chart4Dom: null,
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
        this.chart3Data = data.chartList[2] || {}
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '255, 154, 5'
        this.chart3Data.childs[1].style = '1'
        this.chart3Data.childs[1].color = '37, 198, 60'
        this.$nextTick(() => {
          this.initChart()
        })
      })
    },
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      burstingDataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.chart1Data = data.chartList[0] || {}
        this.chart2Data = data.chartList[1] || {}
        this.chart3Data = data.chartList[2] || {}
        this.chart4Data = data.chartList[3] || {}
        this.chart1Data.childs[0].style = '1'
        this.chart1Data.childs[0].color = '55, 123, 247'
        this.chart1Data.childs[1].style = '0'
        this.chart1Data.childs[1].color = '37, 198, 60'
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '0, 79, 223'
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '255, 154, 5'
        this.chart3Data.childs[1].style = '1'
        this.chart3Data.childs[1].color = '37, 198, 60'
        this.chart4Data.childs[0].style = '1'
        this.chart4Data.childs[0].color = '238, 168, 77'
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
          needLegend: true,
          smooth: false,
          colorStops: false,
          vm: this,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
          needLegend: false,
          colorStops: false,
          vm: this,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needLegend: true,
          colorStops: false,
          needShadow: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          colorStops: false,
          vm: this,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.jzsgf_dataStatistics {
  display: flex;
  flex-direction: column;
  .topInfo {
    .content {
      .topInfo-c {
        min-height: 80px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 10px;
        padding: 0 20px;
        .label {
          color: #333333;
          font-size: 28px;
          width: 150px;
        }
        .value {
          font-weight: bold;
          font-size: 28px;
          color: #333333;
          width: calc(100% - 150px);
          text-align: right;
        }
      }
      .topInfo-b {
        background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
        border-radius: 14px;
        display: flex;
        flex-wrap: wrap;
        margin-top: 20px;
        position: relative;
        &::before {
          content: '';
          position: absolute;
          height: 74px;
          width: 0px;
          border-left: 1px solid #e0e5ec;
          left: 50%;
          top: 50%;
          margin-top: -37px;
        }
        .topInfo-item {
          min-height: 134px;
          width: 50%;
          text-align: center;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .label {
            margin-top: 10px;
            color: #666666;
            font-size: 24px;
          }
          .value {
            font-weight: bold;
            font-size: 36px;
            color: #292929;
          }
        }
      }
    }
  }
  .chart {
    .chart-item {
      height: 422px;
    }
  }
}
</style>
