<template>
  <div class="fbsgf_pvGlobalOverview container">
    <div class="devOpsData content-item">
      <div class="content">
        <div class="devOpsData-t">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(0, 2) || []" :key="index">
            <div class="p-icon"></div>
            <div class="values">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
        </div>
        <div class="devOpsData-b">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(2) || []" :key="index">
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
            <div class="label">{{ item.label || '' }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="dataView content-item">
      <MTittle :text="dataView.label || ''"></MTittle>
      <div class="content">
        <div class="dataView-t" v-if="dataView.childs?.length >= 6">
          <div class="dataView-l">
            <div class="dataView-item" v-for="(item, index) in [dataView.childs[5]] || []" :key="index">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
          <div class="dataView-r">
            <div class="dataView-item" v-for="(item, index) in [dataView.childs[1], dataView.childs[3]] || []" :key="index">
              <div class="label">{{ item.label || '' }}</div>
              <div class="value">
                {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="dataView-t syData" v-if="dataView.childs?.length >= 6">
          <div class="dataView-l">
            <div class="dataView-item" v-for="(item, index) in [dataView.childs[4]] || []" :key="index">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
          <div class="dataView-r">
            <div class="dataView-item" v-for="(item, index) in [dataView.childs[0], dataView.childs[2]] || []" :key="index">
              <div class="label">{{ item.label || '' }}</div>
              <div class="value">
                {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="multipleData-b">
          <div class="multipleData-item" v-for="(item, index) in otherStatistics.childs || []" :key="index">
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
    <div class="content-item socialBenefit">
      <MTittle :text="socialBenefit.label || ''"></MTittle>
      <div class="content">
        <div class="socialBenefit-item" v-for="(item, index) in socialBenefit.childs || []" :key="index">
          <div class="p-icon" :style="{ backgroundImage: `url(${icons.socialBenefit[index + 1]})` }"></div>
          <div class="values">
            <div class="value">
              {{ item.value }}<span class="unit" v-if="item.unit"> /{{ item.unit }}</span>
            </div>
            <div class="label" v-html="item.label"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="envDataToday content-item">
      <MTittle :text="envDataToday.label || ''"></MTittle>
      <div class="content">
        <div class="envDataToday-item p-icon" v-for="(item, index) in envDataToday.childs || []" :key="index" :style="{ backgroundImage: `url(${icons.envDataToday[index + 2]})` }">
          <div class="label">{{ item.label }}</div>
          <div class="value">
            {{ item.value }} <span class="unit" v-if="item.unit"> {{ item.unit }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart1"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart2Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart2"></div>
    </div>
    <!-- <div class="chart content-item">
      <MTittle :text="chart3Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart3"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart4Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart4"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart5Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart5"></div>
    </div>
    <div class="chart content-item">
      <MTittle :text="chart6Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart6"></div>
    </div> -->
  </div>
</template>
<script>
import { pvGlobalOverview } from '@/api/fbsgf'
import icons from '@/utils/icons'
export default {
  name: 'pvGlobalOverview',
  data() {
    return {
      icons: icons,

      devOpsData: {},
      dataView: {},
      socialBenefit: {},
      envDataToday: {},
      otherStatistics: {},
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
    }
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      pvGlobalOverview().then((res) => {
        let data = res.data
        this.devOpsData = data.devOpsData || {}
        this.dataView = data.dataView || {}
        this.socialBenefit = data.socialBenefit || {}
        this.envDataToday = data.envDataToday || {}
        this.otherStatistics = data.otherStatistics || {}
        this.chart1Data = data.chartList[0] || {}
        this.chart2Data = data.chartList[1] || {}
        // this.chart3Data = data.chartList[2] || {}
        // this.chart4Data = data.chartList[3] || {}
        // this.chart5Data = data.chartList[4] || {}
        // this.chart6Data = data.chartList[5] || {}
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '0, 79, 223'
        this.chart2Data.childs[0].style = '1'
        this.chart2Data.childs[0].color = '241, 138, 5'
        // this.chart3Data.childs[0].style = '1'
        // this.chart3Data.childs[0].color = '55, 123, 247'
        // this.chart4Data.childs[0].style = '0'
        // this.chart4Data.childs[0].color = '0, 188, 134'
        // this.chart5Data.childs[0].style = '1'
        // this.chart5Data.childs[0].color = '238, 168, 77'
        // this.chart6Data.childs[0].style = '1'
        // this.chart6Data.childs[0].color = '0, 188, 134'

        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          // this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          // this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          // this.chart5Dom = this.$echarts.init(this.$refs.chart5, null, { renderer: 'svg' })
          // this.chart6Dom = this.$echarts.init(this.$refs.chart6, null, { renderer: 'svg' })

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
          needLegend: false,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          vm: this,
          colorStops: false,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        // let chart3Options = {
        //   data: this.chart3Data,
        //   needStart: true,
        //   needShadow: false,
        //   needLegend: false,
        //   vm: this,
        //   colorStops: false,
        // }
        // this.chart3Dom.setOption(this.$initChart(chart3Options))
        // let chart4Options = {
        //   data: this.chart4Data,
        //   needStart: true,
        //   needShadow: false,
        //   needLegend: false,
        // }
        // this.chart4Dom.setOption(this.$initChart(chart4Options))
        // let chart5Options = {
        //   data: this.chart5Data,
        //   needStart: true,
        //   needShadow: false,
        //   needLegend: false,
        //   vm: this,
        //   colorStops: false,
        // }
        // this.chart5Dom.setOption(this.$initChart(chart5Options))
        // let chart6Options = {
        //   data: this.chart6Data,
        //   needStart: true,
        //   needShadow: false,
        //   needLegend: false,
        //   vm: this,
        //   colorStops: false,
        // }
        // this.chart6Dom.setOption(this.$initChart(chart6Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fbsgf_pvGlobalOverview {
  display: flex;
  flex-direction: column;
  .devOpsData {
    .content {
      .devOpsData-t {
        display: flex;
        .p-icon {
          min-width: 80px;
          width: 80px;
          height: 80px;
          margin-right: 20px;
        }
        .devOpsData-item {
          flex: 1;
          display: flex;
          padding: 0 0 0 20px;
          &:nth-child(1) {
            .p-icon {
              background-image: url('~@/assets/img/common/icon_charging01.png');
            }
          }
          &:nth-child(2) {
            .p-icon {
              background-image: url('~@/assets/img/common/photovoltaic_icon_01.png');
            }
          }
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
      .devOpsData-b {
        // flex: 1;
        margin-top: 30px;
        background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
        border-radius: 14px;
        min-height: 150px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .devOpsData-item {
          text-align: center;
          flex: 1;
          padding: 0 20px;
          border-right: 1px solid #e0e5ec;
          &:last-child {
            border-right: none;
          }
          .label {
            color: #666666;
            font-size: 24px;
          }
          .value {
            font-weight: bold;
            font-size: 36px;
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
  .socialBenefit {
    .content {
      display: flex;
      justify-content: space-between;
      .socialBenefit-item {
        width: 316px;
        min-height: 120px;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        padding: 22px 30px;
        display: flex;
        align-items: center;
        .p-icon {
          height: 70px;
          width: 70px;
          margin-right: 20px;
        }
        .values {
          width: calc(100% - 90px);
          .value {
            font-weight: bold;
            font-size: 38px;
            color: #292929;
            .unit {
              font-size: 24px;
            }
          }
          .label {
            font-size: 24px;
            color: #333333;
          }
        }
      }
    }
  }
  .envDataToday {
    .content {
      display: flex;
      flex-wrap: wrap;
      gap: 20px 20px;
      .envDataToday-item {
        width: 206px;
        height: 180px;
        padding: 20px;
        .label {
          font-size: 24px;
          color: #333333;
        }
        .value {
          font-weight: bold;
          font-size: 38px;
          color: #333333;
          margin-top: 55px;
          .unit {
            font-size: 26px;
          }
        }
      }
    }
  }
  .dataView {
    .content {
      .dataView-t {
        display: flex;
        align-items: center;
        width: 662px;
        margin: 0 auto;
        padding: 35px 0;
        margin-top: 20px;
        background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
        border-radius: 14px;
        &:first-child {
          margin-top: 0;
        }
        .dataView-l {
          width: 280px;
          display: flex;
          border-right: 1px solid #e0e5ec;
          .dataView-item {
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
        .dataView-r {
          flex: 1;
          .dataView-item {
            display: flex;
            justify-content: space-between;
            padding-left: 40px;
            padding-right: 30px;
            margin-top: 20px;
            &:first-child {
              margin-top: 0;
            }
            .label {
              width: 140px;
              color: #666666;
              font-size: 26px;
            }
            .value {
              max-width: calc(100% - 150px);
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

      .syData {
        .dataView-l {
          .value {
            color: #f18a05 !important;
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
  .chart {
    .chart-item {
      height: 422px;
    }
  }
}
</style>
