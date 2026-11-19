<template>
  <div class="jzsgf_pvGlobalOverview container">
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
      <MTittle :text="fdData.label || ''"></MTittle>
      <div class="content">
        <div class="dataView-t" v-if="fdData.childs?.length">
          <div class="dataView-l">
            <div class="dataView-item" v-for="(item, index) in fdData.childs.slice(0, 1) || []" :key="index">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
          <div class="dataView-r">
            <div class="dataView-item" v-for="(item, index) in fdData.childs.slice(1) || []" :key="index">
              <div class="label">{{ item.label || '' }}</div>
              <div class="value">
                {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="dataView-t syData" v-if="syData.childs?.length">
          <div class="dataView-l">
            <div class="dataView-item" v-for="(item, index) in syData.childs.slice(0, 1) || []" :key="index">
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
              <div class="label">{{ item.label || '' }}</div>
            </div>
          </div>
          <div class="dataView-r">
            <div class="dataView-item" v-for="(item, index) in syData.childs.slice(1) || []" :key="index">
              <div class="label">{{ item.label || '' }}</div>
              <div class="value">
                {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="multipleData-b">
          <div class="multipleData-item" v-for="(item, index) in otherData.childs || []" :key="index">
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
      <MTittle :text="envData.label || ''"></MTittle>
      <div class="content">
        <div class="envDataToday-item p-icon" v-for="(item, index) in envData.childs || []" :key="index" :style="{ backgroundImage: `url(${icons.envDataToday[index + 2]})` }">
          <div class="label">{{ item.label }}</div>
          <div class="value">
            {{ item.value }} <span class="unit" v-if="item.unit"> {{ item.unit }}</span>
          </div>
        </div>
      </div>
    </div>
    <!-- <div class="inverterData content-item">
      <MTittle :text="inverterData.label || ''"></MTittle>
      <div class="content">
        <div class="inverterData-item">
          <div class="inverterData-item-values">
            <div class="inverterData-item-value" v-for="(item, index) in inverterData.childs || []" :key="index">
              <div class="label">{{ item.label || '' }}{{ item.unit ? `/${item.unit}` : '' }}</div>
              <div class="value">{{ item.value || '' }}</div>
            </div>
          </div>
        </div>
      </div>
    </div> -->
    <div class="chart content-item">
      <MTittle :text="chart1Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart1"></div>
    </div>
    <!-- <div class="chart content-item">
      <MTittle :text="chart2Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart2"></div>
    </div> -->
    <div class="chart content-item">
      <MTittle :text="chart3Data.label || ''"></MTittle>
      <div class="chart-item" ref="chart3"></div>
    </div>
  </div>
</template>
<script>
import { burstingGlobalView } from '@/api/jzsgf'
import icons from '@/utils/icons'
export default {
  name: 'jzsgf_pvGlobalOverview',
  data() {
    return {
      icons: icons,

      devOpsData: {},
      failureData: {},
      fdData: {},
      syData: {},
      otherData: {},
      socialBenefit: {},
      envData: {},
      inverterData: {},

      chart1Dom: null,
      chart1Data: {},
      chart2Dom: null,
      chart2Data: {},
      chart3Dom: null,
      chart3Data: {},
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      burstingGlobalView().then((res) => {
        let data = res.data

        this.devOpsData = data.devOpsData || {}
        this.failureData = data.failureData || {}
        this.fdData = data.fdData || {}
        this.syData = data.syData || {}
        this.otherData = data.otherData || {}
        this.socialBenefit = data.socialBenefit || {}
        this.envData = data.envData || {}
        this.inverterData = data.inverterData || {}

        this.chart1Data = data.chartList[0] || {}
        this.chart2Data = data.chartList[1] || {}
        this.chart3Data = data.chartList[2] || {}

        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '0, 79, 223'
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '241, 138, 5'
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '55, 123, 247'

        if (this.devOpsData.childs?.length && this.failureData.childs?.length) {
          this.devOpsData.childs = this.devOpsData.childs.concat(this.failureData.childs)
        }
        
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          // this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })

          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needStart: true,
          // needShadow: false,
          needLegend: false,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        // let chart2Options = {
        //   data: this.chart2Data,
        //   needStart: true,
        //   // needShadow: false,
        //   needLegend: false,
        // }
        // this.chart2Dom.setOption(this.$initChart(chart2Options))
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          vm: this,
          colorStops: false,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.jzsgf_pvGlobalOverview {
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
  .inverterData {
    .inverterData-item {
      .inverterData-item-values {
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 10px;
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
        .inverterData-item-value {
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
  .chart {
    .chart-item {
      height: 422px;
    }
  }
}
</style>
