<template>
  <div class="cnxm_batteryMonitor container">
    <div class="topInfo content-item">
      <div class="content">
        <div class="topInfo-t">
          <div class="topInfo-item" :class="matteryStatus(item, index)" v-for="(item, index) in topInfo.childs?.slice(0, 1) || []" :key="index">
            <div class="label">{{ item.label || '' }}</div>
            <div class="value">
              {{ item.value || '' }} <span class="unit" v-if="item.unit">{{ item.unit ? `${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
        <div class="topInfo-b">
          <div class="topInfo-item" v-for="(item, index) in topInfo.childs?.slice(1) || []" :key="index">
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
    <div class="abnormalAlarm content-item">
      <MTittle :text="abnormalAlarm.label || ''"></MTittle>
      <div class="content">
        <div class="abnormalAlarm-item" v-for="(item, index) in abnormalAlarm.childs || []" :key="index">
          <div class="label">{{ item.label || '' }}</div>
          <div class="value">
            {{ item.value || '' }} <span class="unit" v-if="item.unit">{{ item.unit ? `${item.unit}` : '' }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="chart1" ref="chart1"></div>
    </div>
  </div>
</template>
<script>
import { batteryMonitor } from '@/api/cnxm.js'
export default {
  name: 'batteryMonitor',
  data() {
    return {
      topInfo: {},
      runningMonitor: {},
      abnormalAlarm: {},

      chart1Data: {},
      chart1Dom: null,
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
  mounted() {},
  methods: {
    initData() {
      batteryMonitor().then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.runningMonitor = data.runningMonitor || {}
        this.abnormalAlarm = data.abnormalAlarm || {}

        this.chart1Data = data.chartList[0] || {}
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '77, 105, 237'
        this.chart1Data.childs[1].style = '0'
        this.chart1Data.childs[1].color = '219, 92, 131'

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
          needShadow: false,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.cnxm_batteryMonitor {
  display: flex;
  flex-direction: column;
  .topInfo {
    .topInfo-t {
      .topInfo-item {
        display: flex;
        justify-content: space-between;
        min-height: 80px;
        align-items: center;
        font-size: 28px;
        border-radius: 10px;
        padding: 0 20px;
        margin-top: 20px;
        &:first-child {
          margin-top: 0;
        }
        .label {
          color: #333333;
          width: 400px;
        }
        .value {
          position: relative;
          max-width: 200px;
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
      .charge {
        background-color: rgba(#f40909, 0.05);
        .value {
          color: #ff0000;
          &::before {
            background-color: #ff0000;
          }
        }
      }
      .discharge {
        background-color: rgba(#36d2ff, 0.05);
        .value {
          color: #36d2ff;
          &::before {
            background-color: #36d2ff;
          }
        }
      }
      .fault {
        background-color: rgba(#f40909, 0.05);
        .value {
          color: #ff0000;
          &::before {
            background-color: #ff0000;
          }
        }
      }
      .idle {
        background-color: rgba(#32da45, 0.05);
        .value {
          color: #32da45;
          &::before {
            background-color: #32da45;
          }
        }
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
  .abnormalAlarm {
    .abnormalAlarm-item {
      display: flex;
      justify-content: space-between;
      min-height: 80px;
      align-items: center;
      font-size: 28px;
      border-radius: 10px;
      padding: 0 20px;
      margin-top: 20px;
      background-color: rgba(#f40909, 0.05);
      &:first-child {
        margin-top: 0;
      }
      .label {
        color: #333333;
        width: 400px;
      }
      .value {
        color: #ff0000;
        max-width: 200px;
        text-align: right;
        font-weight: bold;
        .unit {
          color: #333333;
          font-weight: 400;
        }
      }
    }
  }
  .chart {
    .chart1 {
      height: 422px;
    }
  }
}
</style>
