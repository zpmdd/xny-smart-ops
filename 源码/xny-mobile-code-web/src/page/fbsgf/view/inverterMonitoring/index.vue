<template>
  <div class="fbsgf_inverterMonitoring container">
    <div class="inverterDataStatistics content-item">
      <div class="content">
        <div class="inverterDataStatistics-t">
          <div class="inverterDataStatistics-t-item" v-for="(item, index) in inverterDataStatistics.childs?.slice(0, 3) || []" :key="index">
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label || '' }}{{ item.unit ? `/${item.unit}` : '' }}</div>
          </div>
        </div>
        <div class="inverterDataStatistics-b">
          <div class="inverterDataStatistics-b-item" v-for="(item, index) in inverterDataStatistics.childs?.slice(3) || []" :key="index">
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
            <div class="label">{{ item.label || '' }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart1"></div>
    </div>
    <div class="realTimeData content-item">
      <MTittle :text="'逆变器实时数据'"></MTittle>
      <div class="content">
        <div class="realTimeData-item" v-for="(item, index) in realTimeData.slice(1)" :key="index">
          <div class="realTimeData-item-title" :class="{ err: item[1].value != '正常', normal: item[1].value == '正常' }" v-for="(item1, index1) in item.slice(0, 1) || []" :key="index1">
            <div class="label">{{ item1.value || '' }}</div>
            <div class="value">{{ item[1].value == '正常' ? '运行正常' : '运行异常' || '' }}</div>
          </div>
          <div class="realTimeData-item-values">
            <div class="realTimeData-item-value" v-for="(item1, index1) in item.slice(2)" :key="index1">
              <div class="label">{{ item1.label || '' }}{{ item.unit ? `/${item.unit}` : '' }}</div>
              <div class="value">{{ item1.value || '' }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { inverterMonitoring } from '@/api/fbsgf'
export default {
  name: 'inverterMonitoring',
  data() {
    return {
      realTimeData: [],
      inverterDataStatistics: {},
      chart1Dom: null,
      chart1Data: {},
    }
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      inverterMonitoring().then((res) => {
        let data = res.data
        this.inverterDataStatistics = data.inverterDataStatistics || []
        if (this.inverterDataStatistics.childs) {
          this.inverterDataStatistics.childs = this.inverterDataStatistics.childs.concat(data.otherStatistics.childs ? data.otherStatistics.childs : [])
        }
        if (this.inverterDataStatistics.childs?.length >= 5) {
          ;[this.inverterDataStatistics.childs[2], this.inverterDataStatistics.childs[3]] = [this.inverterDataStatistics.childs[3], this.inverterDataStatistics.childs[2]]
        }
        let realTimeData = []
        if (data.realTimeData.length) {
          for (let index = 0; index < data.realTimeData[0].length; index++) {
            let item = []
            for (let i = 0; i < data.realTimeData.length; i++) {
              item.push({ label: data.realTimeData[i][0].value, ...data.realTimeData[i][index] })
            }
            realTimeData.push(item)
          }
        }
        this.realTimeData = realTimeData || []
        this.chart1Data = data.chartList[0] || {}
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '0, 79, 223'
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
          needLegend: false,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fbsgf_inverterMonitoring {
  display: flex;
  flex-direction: column;
  .inverterDataStatistics {
    .inverterDataStatistics-t {
      display: flex;
      justify-content: space-between;
      .inverterDataStatistics-t-item {
        flex: 1;
        text-align: center;
        min-height: 150px;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        margin-left: 20px;
        display: flex;
        justify-content: center;
        flex-direction: column;
        &:first-child {
          margin-left: 0;
        }
        .value {
          font-weight: bold;
          font-size: 36px;
          color: #292929;
        }
        .label {
          font-size: 24px;
          color: #666666;
          margin-top: 10px;
        }
      }
    }
    .inverterDataStatistics-b {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
      background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
      border-radius: 14px;
      min-height: 150px;
      align-items: center;
      .inverterDataStatistics-b-item {
        flex: 1;
        text-align: center;
        display: flex;
        justify-content: center;
        flex-direction: column;
        border-left: 1px solid #e0e5ec;
        &:first-child {
          border: none;
        }
        .value {
          font-weight: bold;
          font-size: 36px;
          color: #292929;
          .unit {
            font-size: 24px;
          }
        }
        .label {
          font-size: 24px;
          color: #666666;
          margin-top: 10px;
        }
      }
    }
  }
  .chart {
    .chart-item {
      height: 422px;
    }
  }
  .realTimeData {
    .realTimeData-item {
      margin-top: 20px;
      &:first-child {
        margin-top: 0;
      }
      .realTimeData-item-title {
        display: flex;
        justify-content: space-between;
        min-height: 80px;
        align-items: center;
        font-size: 28px;
        border-radius: 10px;
        padding: 0 20px;
        background-color: rgba(#b5b5b5, 0.05);
        .label {
          color: #333333;
          width: 400px;
          font-weight: bold;
        }
        .value {
          position: relative;
          max-width: 200px;
          text-align: right;
          &::before {
            content: '';
            position: absolute;
            width: 11px;
            height: 11px;
            border-radius: 50%;
            left: -22px;
            top: 16px;
          }
        }
      }
      .err {
        background-color: rgba(#f40909, 0.05);
        .value {
          color: #ff0000;
          &::before {
            background-color: #ff0000;
          }
        }
      }
      .normal {
        background-color: rgba(#00b827, 0.05);
        .value {
          color: #00b827;
          &::before {
            background-color: #00b827;
          }
        }
      }
      .realTimeData-item-values {
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 10px;
        margin-top: 20px;
        padding: 20px 0;
        display: flex;
        flex-wrap: wrap;
        gap: 20px 10px;
        position: relative;
        &::before {
          content: '';
          position: absolute;
          width: 1px;
          height: calc(100% - 40px);
          left: 50%;
          background-color: #e0e5ec;
        }
        .realTimeData-item-value {
          display: flex;
          justify-content: space-between;
          width: calc(50% - 5px);
          padding: 0 20px;
          .label {
            font-size: 26px;
            color: #666666;
            width: 160px;
          }
          .value {
            font-weight: bold;
            font-size: 28px;
            color: #333333;
            max-width: calc(100% - 170px);
          }
        }
      }
    }
  }
}
</style>
