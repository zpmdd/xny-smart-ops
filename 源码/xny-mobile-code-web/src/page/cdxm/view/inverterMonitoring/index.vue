<template>
  <div class="cdxm_inverterMonitoring container">
    <div class="topInfo content-item">
      <div class="content">
        <div class="topInfo-b">
          <div class="topInfo-item" v-for="(item, index) in topInfo.childs || []" :key="index">
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
    <div class="runningMonitor content-item">
      <MTittle :text="runningMonitor.label || ''"></MTittle>
      <div class="content">
        <div class="runningMonitor-item" v-for="(item, index) in runningMonitor.childs || []" :key="index">
          <div class="value">
            {{ item.value || '' }}
          </div>
          <div class="label">
            {{ item.label || '' }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="chart first content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="content" v-if="chart1Data.childs?.length">
        <div class="chart-item" v-for="(item, index) in chart1Data.childs[0]?.childs || []" :key="index">
          <div class="item-title">
            <div class="label">{{ item.label || '' }}</div>
          </div>
          <div class="item-value">
            <div class="progress">
              <div class="progress-bar" :style="{ width: item.percentage + '%' }"></div>
            </div>
            <div class="value">
              {{ item.value }}<span class="unit">{{ chart1Data.unit ? `/${chart1Data.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="chart second content-item">
      <MTittle :text="chart2Data.label || ''"></MTittle>
      <div class="content" v-if="chart2Data.childs?.length">
        <div class="chart-item" v-for="(item, index) in chart2Data.childs[0]?.childs || []" :key="index">
          <div class="item-title">
            <div class="label">{{ item.label || '' }}</div>
          </div>
          <div class="item-value">
            <div class="progress">
              <div class="progress-bar" :style="{ width: item.percentage + '%' }"></div>
            </div>
            <div class="value">
              {{ item.value }}<span class="unit">{{ chart2Data.unit ? `/${chart2Data.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart3Data.label || ''"></MTittle>
      <div class="chart3" ref="chart3"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart4Data.label || ''"></MTittle>
      <div class="chart4" ref="chart4"></div>
    </div>
  </div>
</template>
<script>
import { pilesMonitor } from '@/api/cdxm'
export default {
  name: 'inverterMonitoring',
  data() {
    return {
      topInfo: {},
      runningMonitor: {},

      chart1Data: {},
      chart2Data: {},

      chart3Data: {},
      chart3Dom: null,
      chart4Data: {},
      chart4Dom: null,
    }
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      pilesMonitor().then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.runningMonitor = data.runningMonitor || {}
        this.chart1Data = data.chartList[0] || {}
        this.chart1Data.maxValue = this.checkMax(this.chart1Data.childs[0].childs)
        this.chart1Data.childs[0].childs.forEach((item) => {
          item.percentage = ((item.value / this.chart1Data.maxValue) * 100).toFixed(0)
        })
        this.chart2Data = data.chartList[2] || {}
        this.chart2Data.maxValue = this.checkMax(this.chart2Data.childs[0].childs)
        this.chart2Data.childs[0].childs.forEach((item) => {
          item.percentage = ((item.value / this.chart2Data.maxValue) * 100).toFixed(0)
        })
        this.chart3Data = data.chartList[1]
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '0, 79, 223'
        this.chart4Data = data.chartList[3]
        this.chart4Data.childs[0].style = '0'
        this.chart4Data.childs[0].color = '241, 138, 5'
        this.$nextTick(() => {
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    checkMax(list) {
      let max = list.sort((a, b) => {
        return Number(b.value) - Number(a.value)
      })[0].value
      return max
    },
    initChart() {
      this.$nextTick(() => {
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needLegend: false,
          needShadow: false,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          needLegend: false,
          needShadow: false,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.cdxm_inverterMonitoring {
  display: flex;
  flex-direction: column;
  .topInfo {
    .topInfo-b {
      background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
      border-radius: 14px;
      display: flex;
      flex-wrap: wrap;
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
  .runningMonitor {
    .content {
      display: flex;
      flex-wrap: wrap;
      gap: 20px 20px;
      .runningMonitor-item {
        width: 206px;
        min-height: 150px;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .value {
          color: #292929;
          font-size: 36px;
        }
        .label {
          color: #666666;
          font-size: 23px;
          margin-top: 10px;
        }
      }
    }
  }
  .chart {
    .chart3,
    .chart4 {
      height: 422px;
    }
    .content {
      .chart-item {
        margin-top: 10px;
        .item-title {
          color: #666666;
          font-size: 26px;
        }
        .item-value {
          display: flex;
          align-items: center;
          justify-content: space-between;
          .progress {
            background: #f3f6fa;
            border-radius: 8px;
            height: 16px;
            width: 450px;
            .progress-bar {
              border-radius: 8px;
              height: 16px;
            }
          }
          .value {
            color: #333333;
            font-size: 28px;
            width: calc(100% - 470px);
            text-align: right;
            .unit {
              color: #666666;
              font-size: 26px;
            }
          }
        }
      }
    }
  }
  .first {
    .progress-bar {
      border-radius: 8px;
      height: 16px;
      background: linear-gradient(-90deg, #367bf8 0%, #6bb1ff 100%);
    }
  }
  .second {
    .progress-bar {
      border-radius: 8px;
      height: 16px;
      background: linear-gradient(-90deg, #32b554 0%, #83d498 100%);
    }
  }
}
</style>
