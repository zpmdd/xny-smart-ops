<template>
  <div class="fbsgf_dataStatistics container">
    <div class="topDataStatistics content-item">
      <div class="content">
        <div class="topDataStatistics-c">
          <div class="label" v-if="topDataStatistics.childs?.length">{{ topDataStatistics.childs[0].label || '' }}</div>
          <div class="value" v-if="topDataStatistics.childs?.length">
            {{ topDataStatistics.childs[0].value }}
            <span class="unit" v-if="topDataStatistics.childs[0].unit">{{ topDataStatistics.childs[0].unit ? `/${topDataStatistics.childs[0].unit}` : '' }}</span>
          </div>
        </div>
        <div class="topDataStatistics-b" v-if="topDataStatistics.childs?.length">
          <div class="topDataStatistics-item" v-for="(item, index) in topDataStatistics.childs.slice(1) || []" :key="index">
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
      <MTittle :text="chart3Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart3"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart5Data.label || ''">
        <el-select v-model="chartActive" placeholder="请选择">
          <el-option v-for="item in selectList" :key="item.value" :label="item.label" :value="item.value"> </el-option>
        </el-select>
      </MTittle>
      <div class="chart-item" ref="chart5"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart4Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart4"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart2Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart2"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart6Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart6"></div>
    </div>
  </div>
</template>
<script>
import { dataStatistics } from '@/api/fbsgf'
export default {
  name: 'dataStatistics',
  data() {
    return {
      topDataStatistics: {},
      chart1Dom: null,
      chart1Data: {},
      chart2Dom: null,
      chart2Data: {},
      chart3Dom: null,
      chart3Data: {},
      chart4Dom: null,
      chart4Data: {},
      chart5Dom: null,
      chart5Data: {},
      chart6Dom: null,
      chart6Data: {},
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
      dataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.chart5Data = data.chartList[4] || {}
        this.chart5Data.childs[0].style = '0'
        this.chart5Data.childs[0].color = '255, 154, 5'
        this.chart5Data.childs[1].style = '1'
        this.chart5Data.childs[1].color = '37, 198, 60'
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
      dataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.topDataStatistics = data.topDataStatistics || {}
        this.chart1Data = data.chartList[0] || {}
        this.chart2Data = data.chartList[1] || {}
        this.chart3Data = data.chartList[2] || {}
        this.chart4Data = data.chartList[3] || {}
        this.chart5Data = data.chartList[4] || {}
        this.chart6Data = data.chartList[5] || {}
        this.chart1Data.childs[0].style = '2'
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '238, 168, 77'
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '55, 123, 247'
        this.chart3Data.childs[1].style = '0'
        this.chart3Data.childs[1].color = '37, 198, 60'
        this.chart4Data.childs[0].style = '0'
        this.chart4Data.childs[0].color = '55, 123, 247'
        this.chart5Data.childs[0].style = '0'
        this.chart5Data.childs[0].color = '255, 154, 5'
        this.chart5Data.childs[1].style = '1'
        this.chart5Data.childs[1].color = '37, 198, 60'
        this.chart6Data.childs[0].style = '1'
        this.chart6Data.childs[0].color = '238, 168, 77'
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          this.chart5Dom = this.$echarts.init(this.$refs.chart5, null, { renderer: 'svg' })
          this.chart6Dom = this.$echarts.init(this.$refs.chart6, null, { renderer: 'svg' })

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
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
          // needShadow: false,
          needLegend: false,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needShadow: false,
          needLegend: true,
          smooth: false,
          colorStops: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          // needShadow: false,
          needLegend: false,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
        let chart5Options = {
          data: this.chart5Data,
          needStart: true,
          needShadow: false,
          needLegend: true,
          smooth: false,
          colorStops: false,
          vm: this,
        }
        this.chart5Dom.setOption(this.$initChart(chart5Options))
        let chart6Options = {
          data: this.chart6Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          colorStops: false,
          vm: this,
        }
        this.chart6Dom.setOption(this.$initChart(chart6Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fbsgf_dataStatistics {
  display: flex;
  flex-direction: column;
  .topDataStatistics {
    .content {
      .topDataStatistics-c {
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
      .topDataStatistics-b {
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
        .topDataStatistics-item {
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
