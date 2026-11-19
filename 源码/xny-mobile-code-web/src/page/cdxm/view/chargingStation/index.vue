<template>
  <div class="cdxm_chargingStation container">
    <div class="devOpsData content-item">
      <div class="content">
        <div class="devOpsData-l">
          <div class="p-icon"></div>
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(0, 1) || []" :key="index">
            <div class="value">
              {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
            <div class="label">{{ item.label || '' }}</div>
          </div>
        </div>
        <div class="devOpsData-r">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(1) || []" :key="index">
            <div class="label">{{ item.label || '' }}</div>
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="stationData content-item">
      <MTittle :text="stationData.label || ''"></MTittle>
      <div class="content">
        <div class="stationData-t">
          <div class="stationData-l">
            <div class="stationData-item" v-for="(item, index) in stationData.childs?.slice(0, 1) || []" :key="index">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
          <div class="stationData-r">
            <div class="stationData-item" v-for="(item, index) in stationData.childs?.slice(1) || []" :key="index">
              <div class="label">{{ item.label || '' }}</div>
              <div class="value">
                {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="multipleData-c">
          <div class="label" v-if="multipleData.childs?.length">{{ multipleData.childs[0].label || '' }}</div>
          <div class="value" v-if="multipleData.childs?.length">
            {{ multipleData.childs[0].value }}
            <span class="unit" v-if="multipleData.childs[0].unit">{{ multipleData.childs[0].unit ? `/${multipleData.childs[0].unit}` : '' }}</span>
          </div>
        </div> -->
        <!-- <div class="multipleData-b" v-if="multipleData.childs?.length">
          <div class="multipleData-item" v-for="(item, index) in multipleData.childs.slice(1) || []" :key="index">
            <div class="value">
              {{ item.value || '' }}
            </div>
            <div class="label">
              {{ item.label || '' }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div> -->
      </div>
    </div>
    <div class="inUseData content-item">
      <MTittle :text="inUseData.label || ''"></MTittle>
      <div class="content">
        <div class="inUseData-item" :class="{ err: item.childs[0].value == '故障报警', normal: item.childs[0].value == '使用中', notEnabled: item.childs[0].value == '空闲中' }" v-for="(item, index) in inUseData.childs || []" :key="index">
          <div class="inUseData-item-row">
            <div class="label">
              <div class="p-icon"></div>
              {{ item.label || '' }}
            </div>
            <div class="value">{{ item.childs[0].value || '' }}</div>
          </div>
          <div class="inUseData-item-content" v-if="item.childs && item.childs.length && item.childs[0].value == '使用中'">
            <div class="l">
              <div class="inUseData-item-content-item">
                <div class="label">{{ computenValue(item, 3, 'label') ? item.childs[3].label : '' }}{{ computenValue(item, 3, 'unit') ? `/${item.childs[3].unit}` : '' }}</div>
                <div class="value">{{ computenValue(item, 3, 'value') ? item.childs[3].value : '' }}</div>
              </div>
              <div class="inUseData-item-content-item">
                <div class="label">{{ computenValue(item, 4, 'label') ? item.childs[4].label : '' }}{{ computenValue(item, 4, 'unit') ? `/${item.childs[4].unit}` : '' }}</div>
                <div class="value">{{ computenValue(item, 4, 'value') ? item.childs[4].value : '' }}</div>
              </div>
              <div class="inUseData-item-content-item">
                <div class="label">{{ computenValue(item, 5, 'label') ? item.childs[5].label : '' }}{{ computenValue(item, 5, 'unit') ? `/${item.childs[5].unit}` : '' }}</div>
                <div class="value">{{ computenValue(item, 5, 'value') ? item.childs[5].value : '' }}</div>
              </div>
            </div>
            <div class="r">
              <div class="inUseData-item-content-item">
                <div class="label">{{ computenValue(item, 2, 'label') ? item.childs[2].label : '' }}{{ computenValue(item, 2, 'unit') ? `/${item.childs[2].unit}` : '' }}</div>
                <div class="value">{{ computenValue(item, 2, 'value') ? item.childs[2].value : '' }}</div>
              </div>
              <div class="inUseData-item-content-item">
                <div class="label">{{ computenValue(item, 1, 'label') ? item.childs[1].label : '' }}{{ computenValue(item, 1, 'unit') ? `/${item.childs[1].unit}` : '' }}</div>
                <div class="value">{{ computenValue(item, 1, 'value') ? item.childs[1].value : '' }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="stationStatusData content-item">
      <MTittle :text="stationStatusData.label || ''"></MTittle>
      <div class="content">
        <div class="chart-item" v-for="(item, index) in stationStatusData.childs || []" :key="index">
          <div class="item-title">
            <div class="label">{{ item.label || '' }}</div>
          </div>
          <div class="item-value">
            <div class="progress">
              <div class="progress-bar" :style="{ width: item.value + '%' }"></div>
            </div>
            <div class="value">
              {{ item.value }}<span class="unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="spearStatusData content-item">
      <MTittle :text="spearStatusData.label || ''"></MTittle>
      <div class="content">
        <div class="charts">
          <div class="chart1" ref="chart1"></div>
          <div class="chart2" ref="chart2"></div>
        </div>
        <div class="spearStatusData-b" v-if="spearStatusData.childs?.length">
          <div class="label">{{ spearStatusData.childs[2].label || '' }}</div>
          <div class="value">
            {{ spearStatusData.childs[2].value }}<span class="unit" v-if="spearStatusData.childs[2].unit">{{ spearStatusData.childs[2].unit }}</span>
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
    <div class="chart content-item">
      <MTittle :text="chart5Data.label || ''"></MTittle>
      <div class="chart5" ref="chart5"></div>
    </div>
  </div>
</template>
<script>
import { chargeStationMonitor } from '@/api/cdxm'
export default {
  name: 'chargingStation',
  data() {
    return {
      devOpsData: {},
      multipleData: {},
      inUseData: {},
      stationStatusData: {},
      stationData: {},
      spearStatusData: {},
      chart1Data: {},
      chart1Dom: null,
      chart2Data: {},
      chart2Dom: null,
      chart3Data: {},
      chart3Dom: null,
      chart4Data: {},
      chart4Dom: null,
      chart5Data: {},
      chart5Dom: null,
    }
  },
  computed: {
    computenValue() {
      return (val, index, key) => {
        return val && val.childs && val.childs[index] && val.childs[index][key]
      }
    },
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      chargeStationMonitor().then((res) => {
        let data = res.data
        this.devOpsData = data.devOpsData || {}
        this.multipleData = data.multipleData || {}
        this.inUseData = data.inUseData || {}
        this.stationStatusData = data.stationStatusData || {}
        this.stationData = data.stationData || {}
        this.spearStatusData = data.spearStatusData || {}
        this.chart1Data = data.chartList[3] || {}
        this.chart1Data.childs[0].style = '2'
        this.chart2Data = {
          label: data.spearStatusData.label,
          childs: [{ label: '', style: null, unit: '', value: null, childs: data.spearStatusData.childs.slice(0, 2) }],
        }
        this.chart2Data.childs[0].style = '2'
        this.chart3Data = data.chartList[1] || {}
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '77, 105, 237'
        this.chart3Data.childs[1].style = '0'
        this.chart3Data.childs[1].color = '219, 92, 131'
        this.chart4Data = data.chartList[0] || {}
        this.chart4Data.childs[0].style = '0'
        this.chart4Data.childs[0].color = '0, 188, 134'
        this.chart5Data = data.chartList[2] || {}
        this.chart5Data.childs[0].style = '1'
        this.chart5Data.childs[0].color = '77, 105, 237'
        // this.chart5Data.childs[1].style = '1'
        // this.chart5Data.childs[1].color = '0, 188, 134'
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          this.chart5Dom = this.$echarts.init(this.$refs.chart5, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        let chart2Options = {
          data: this.chart2Data,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needShadow: false,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          needShadow: false,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
        let chart5Options = {
          data: this.chart5Data,
          needStart: true,
          needShadow: false,
          stackTotal: true,
          smooth: false,
          colorStops: false,
          vm: this,
        }
        this.chart5Dom.setOption(this.$initChart(chart5Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.cdxm_chargingStation {
  display: flex;
  flex-direction: column;
  .devOpsData {
    .content {
      padding: 35px 30px;
      display: flex;
      align-items: center;
      .devOpsData-l {
        width: 280px;
        display: flex;
        border-right: 1px solid #e0e5ec;
        .p-icon {
          width: 80px;
          height: 80px;
          background-image: url('~@/assets/img/common/icon_charging01.png');
        }
        .devOpsData-item {
          flex: 1;
          margin-left: 20px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .value {
            font-family: douyv;
            font-size: 38px;
            color: #292929;
            .unit {
              font-size: 24px;
            }
          }
          .label {
            color: #333333;
            font-size: 24px;
          }
        }
      }
      .devOpsData-r {
        flex: 1;
        .devOpsData-item {
          display: flex;
          justify-content: space-between;
          padding-left: 40px;
          margin-top: 20px;
          &:first-child {
            margin-top: 0;
          }
          .label {
            width: 180px;
            color: #666666;
            font-size: 26px;
          }
          .value {
            max-width: 120px;
            font-weight: bold;
            font-size: 28px;
            color: #333333;
            .unit {
              font-size: 24px;
              font-weight: 400;
            }
          }
        }
      }
    }
  }
  .stationData {
    .content {
      .stationData-t {
        display: flex;
        align-items: center;
        width: 662px;
        margin: 0 auto;
        padding: 35px 0;
        background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
        border-radius: 14px;
        .stationData-l {
          width: 280px;
          display: flex;
          border-right: 1px solid #e0e5ec;
          .stationData-item {
            flex: 1;
            margin-left: 30px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            .value {
              font-family: douyv;
              font-size: 38px;
              color: #004fdf;
              .unit {
                font-size: 24px;
                font-family: PingFang SC, wryh;
              }
            }
            .label {
              color: #333333;
              font-size: 24px;
            }
          }
        }
        .stationData-r {
          flex: 1;
          .stationData-item {
            display: flex;
            justify-content: space-between;
            padding-left: 80px;
            padding-right: 30px;
            margin-top: 20px;
            &:first-child {
              margin-top: 0;
            }
            .label {
              width: 100px;
              color: #666666;
              font-size: 26px;
            }
            .value {
              max-width: 120px;
              font-weight: bold;
              font-size: 28px;
              color: #333333;
              .unit {
                font-size: 24px;
                font-weight: 400;
              }
            }
          }
        }
      }
      .multipleData-c {
        min-height: 80px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 10px;
        padding: 0 20px;
        margin-top: 20px;
        .label {
          color: #333333;
          font-size: 28px;
          width: 150px;
        }
        .value {
          color: #f18a05;
          font-family: douyv;
          width: calc(100% - 150px);
          font-size: 38px;
          text-align: right;
          .unit {
            font-size: 24px;
            font-family: PingFang SC, wryh;
          }
        }
      }
      .multipleData-b {
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
        .multipleData-item {
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
  .stationStatusData {
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
            width: 550px;
            .progress-bar {
              border-radius: 8px;
              height: 16px;
              background: linear-gradient(-90deg, #367bf8 0%, #6bb1ff 100%);
            }
          }
          .value {
            color: #333333;
            font-size: 28px;
            width: calc(100% - 550px);
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
  .inUseData {
    .inUseData-item {
      margin-top: 12px;
      border-radius: 10px;
      padding: 0 20px;
      background-color: rgba(#b5b5b5, 0.08);
      &:first-child {
        margin-top: 0;
      }
      .inUseData-item-row {
        display: flex;
        justify-content: space-between;
        min-height: 80px;
        align-items: center;
        font-size: 28px;
        .label {
          color: #333333;
          width: 400px;
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          .p-icon {
            background-image: url('~@/assets/img/cdxm/icon_charging03.png');
            height: 32px;
            width: 32px;
            margin-right: 12px;
          }
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
      .inUseData-item-content {
        border-top: 1px solid #e0e5ec;
        padding: 23px 0 23px;
        display: flex;
        .l,
        .r {
          flex: 1;
          .inUseData-item-content-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 26px;
            min-height: 50px;
            .label {
              color: #666666;
            }
            .value {
              font-weight: bold;
              font-size: 28px;
              color: #333333;
              max-width: 130px;
            }
          }
        }
        .l {
          border-right: 1px solid #e0e5ec;
          padding-right: 14px;
        }
        .r {
          padding-left: 14px;
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
      background-color: rgba(#00b827, 0.07);
      .value {
        color: #00b348;
        &::before {
          background-color: #00d957;
        }
      }
    }
    .notEnabled {
      .value {
        color: #666666;
        &::before {
          background-color: #b5b5b5;
        }
      }
    }
  }
  .spearStatusData {
    .charts {
      display: flex;
      justify-content: space-between;
      .chart1,
      .chart2 {
        height: 226px;
        width: 320px;
        background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
        border-radius: 14px;
      }
    }
    .spearStatusData-b {
      margin-top: 20px;
      height: 80px;
      background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
      border-radius: 10px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 20px;
      .label {
        font-size: 28px;
        color: #333333;
      }
      .value {
        font-family: douyv;
        font-size: 38px;
        color: #f18a05;
        .unit {
          font-size: 24px;
        }
      }
    }
  }
  .chart {
    .chart3,
    .chart4,
    .chart5 {
      height: 422px;
    }
  }
}
</style>
