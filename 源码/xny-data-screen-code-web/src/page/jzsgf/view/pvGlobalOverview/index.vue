<template>
  <div class="jzsgf-pvGlobalOverview view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="content-view">
        <div class="l">
          <TextTittle :label="fdData?.label || ''"></TextTittle>
          <div class="fdAndsy-data">
            <div class="fdData-content" v-if="fdData.childs?.length">
              <div class="fdData-item-row" v-for="(item, index) in fdData.childs || []" :key="index">
                <!-- <img src="@/assets/img/common/bg_wpover03.png" class="icon" v-if="index == 0" /> -->
                <div class="label"><img src="@/assets/img/common/bg_wpover05.png" v-if="index == 0" />{{ item.label }}</div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
                </div>
              </div>
            </div>
            <div class="syData-content" v-if="syData.childs?.length">
              <div class="syData-item-row" v-for="(item, index) in syData.childs || []" :key="index">
                <!-- <img src="@/assets/img/common/bg_wpover04.png" class="icon" v-if="index == 0" /> -->
                <div class="label"><img src="@/assets/img/common/bg_wpover05.png" v-if="index == 0" />{{ item.label }}</div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="otherData-content">
            <div class="otherData-item" v-for="(item, index) in otherData.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">
                <span>
                  {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span></span
                >
              </div>
            </div>
          </div>
          <TextTittle :label="envData?.label || ''"></TextTittle>
          <div class="envData-content">
            <div class="envData-item" v-for="(item, index) in envData.childs || []" :key="index">
              <div class="icon" :style="{ backgroundImage: `url(${projectIcons.fbsgf.envDataToday[index + 1]})` }"></div>
              <div class="values">
                <div class="label" v-html="item.label"></div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="c">
          <div class="devOpsData-content">
            <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
              <div class="icon" :style="{ backgroundImage: `url(${projectIcons.jzsgf.devOpsData[index]})` }"></div>
              <div class="values">
                <div class="label" v-html="item.label"></div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div> -->
        <div class="r">
          <!-- <TextTittle :label="inverterData?.label || ''"></TextTittle>
          <div class="inverterData-content">
            <div class="inverterData-item" v-for="(item, index) in inverterData.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div> -->
          <TextTittle :label="devOpsData?.label || '运维数据'"></TextTittle>
          <div class="socialBenefit-content">
            <div class="socialBenefit-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
              <div class="label" v-html="item.label + (item.unit ? '/' + item.unit : '')"></div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div>
          <TextTittle :label="socialBenefit?.label || ''"></TextTittle>
          <div class="socialBenefit-content">
            <div class="socialBenefit-item" v-for="(item, index) in socialBenefit.childs || []" :key="index">
              <div class="label" v-html="item.label + (item.unit ? '/' + item.unit : '')"></div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="chart-view">
        <div class="chart-item">
          <TextTittle v-if="chart1Data?.label" :label="chart1Data?.label || ''"></TextTittle>
          <div class="chart-content" ref="chart1"></div>
        </div>
        <!-- <div class="chart-item">
          <TextTittle v-if="chart2Data?.label" :label="chart2Data?.label || ''"></TextTittle>
          <div class="chart-content" ref="chart2"></div>
        </div> -->
        <div class="chart-item">
          <TextTittle v-if="chart3Data?.label" :label="chart3Data?.label || ''"></TextTittle>
          <div class="chart-content" ref="chart3"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { burstingGlobalView } from '@/api/jzsgf.js'
import icons from '@/utils/icons.js'
export default {
  name: 'jzsgfPvGlobalOverview',
  data() {
    return {
      projectIcons: icons,

      // 累计发电
      fdData: {},
      // 累计收益
      syData: {},
      // 其他数据
      otherData: {},
      // 环境数据
      envData: {},
      // 运行数据
      devOpsData: {},
      // 逆变器监测
      inverterData: {},
      // 社会贡献
      socialBenefit: {},

      chart1Dom: null,
      chart2Dom: null,
      chart3Dom: null,
      chart1Data: {},
      chart2Data: {},
      chart3Data: {},
    }
  },
  created() {
    this.initData()
  },
  mounted() {
    this.$bus.$on('onresize', () => {
      this.initChart()
    })
    this.$bus.$on('refreshData', () => {
      this.initData('1')
    })
  },
  methods: {
    initData(needInitChart) {
      burstingGlobalView().then((res) => {
        let data = res.data
        this.fdData = data.fdData || {}
        this.syData = data.syData || {}
        this.envData = data.envData || {}
        this.otherData = data.otherData || {}
        this.devOpsData = data.devOpsData || {}
        if (this.devOpsData.childs) {
          this.devOpsData.childs = this.devOpsData.childs.concat(data.failureData?.childs || [])
        }
        this.inverterData = data.inverterData || {}
        this.socialBenefit = data.socialBenefit || {}
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '29, 210, 255'
        // this.chart2Data = data.chartList[1]
        // this.chart2Data.childs[0].style = '0'
        // this.chart2Data.childs[0].color = '41, 245, 175'
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '126, 184, 255'
        this.$nextTick(() => {
          if (!needInitChart) {
            this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
            // this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
            this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          }
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needStart: true,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
        // let chart2Options = {
        //   data: this.chart2Data,
        //   needStart: true,
        // }
        // this.chart2Dom.setOption(this.$initChart(chart2Options))
        // this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needShadow: false,
          smooth: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        this.chart3Dom.resize()
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.jzsgf-pvGlobalOverview {
  background-image: url('~@/assets/img/jzsgf/bg_cpvo03.png');
  .view-container {
    .content-view {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .l {
        width: 420px;
        .fdAndsy-data {
          display: flex;
          justify-content: space-between;
          margin-top: 6px;
          .fdData-content,
          .syData-content {
            width: 192px;
            min-height: 240px;
            background-image: url('~@/assets/img/common/bg_wpover02.png');
            background-size: 100% 100%;
            background-repeat: no-repeat;
            position: relative;
            .icon {
              height: 56px;
              width: 51px;
              position: absolute;
              left: 10px;
              top: 16px;
            }
            .fdData-item-row,
            .syData-item-row {
              padding-top: 22px;
              text-align: center;
              min-height: 70px;
              &:first-child {
                // padding-left: 10px;
                // text-align: left;
                .label {
                  img {
                    width: 16px;
                    height: 16px;
                    vertical-align: text-top;
                    margin-right: 6px;
                  }
                }
              }
              &:nth-child(2) {
                border: 2px solid;
                border-image: linear-gradient(to right, rgba(#002458, 0) 0%, rgba(#002458, 0) 8.2%, rgba(#002458, 1) 8.3%, rgba(#002458, 1) 91.83%, rgba(#002458, 0) 91.84%, rgba(#002458, 0)) 1;
                border-left: none;
                border-right: none;
                border-top: none;
              }
              .label {
                font-size: 14px;
                color: #fff;
                font-family: douyv;
              }
              .value {
                font-size: 20px;
                font-family: douyv;
                background: linear-gradient(0deg, #0064f8 0.1953125%, #70fffa 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                .unit {
                  font-size: 10px;
                  background: linear-gradient(0deg, #fff 0.1953125%, #fff 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }
              }
            }
          }
        }
        .otherData-content {
          margin-top: 10px;
          margin-bottom: 12px;
          overflow: hidden;
          .otherData-item {
            width: 420px;
            height: 61px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/common/bg_nationwide04.png');
            margin-top: 3px;
            &:first-child {
              margin-top: 0;
            }
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-left: 67px;
            .label {
              font-size: 16px;
              font-weight: bold;
              color: #eef3ff;
              width: 140px;
            }
            .value {
              width: 172px;
              text-align: center;
              span {
                font-size: 28px;
                font-weight: bold;
                background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
              }
              .unit {
                font-size: 16px;
                font-weight: 400;
                margin-left: 5px;
              }
            }
          }
        }
        .envData-content {
          display: flex;
          flex-wrap: wrap;
          margin-top: 20px;
          width: 430px;
          gap: 15px 2px;
          .envData-item {
            display: flex;
            .icon {
              width: 55px;
              height: 55px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              margin-right: 7px;
            }
            .values {
              width: 80px;
              .label {
                color: #fff;
                font-size: 14px;
                background: linear-gradient(90deg, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0));
                border-left: 2px solid #80c5f0;
                padding: 0 6px;
              }
              .value {
                color: #86d9ff;
                font-size: 18px;
                font-weight: bold;
                margin-top: 10px;
                .unit {
                  font-size: 12px;
                  font-weight: 400;
                }
              }
            }
          }
        }
      }
      .c {
        flex: 1;
        .devOpsData-content {
          position: absolute;
          display: flex;
          flex-wrap: wrap;
          bottom: 310px;
          left: 560px;
          padding: 20px 0;
          justify-content: space-around;
          border: 1px solid;
          border-image: linear-gradient(to right, rgba(#51c8ff, 0) 0%, rgba(#51c8ff, 0.8) 50%, rgba(#51c8ff, 0)) 1;
          border-left: none;
          border-right: none;
          background-image: url('~@/assets/img/common/bg_pgo05.png');
          background-repeat: no-repeat;
          background-size: 100% 100%;
          .devOpsData-item {
            display: flex;
            .icon {
              width: 58px;
              height: 57px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              margin-right: 12px;
            }
            .values {
              width: 110px;
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
                margin-top: 8px;
                .unit {
                  font-size: 14px;
                  font-weight: 400;
                }
              }
            }
          }
        }
      }
      .r {
        width: 420px;
        .inverterData-content {
          display: flex;
          flex-wrap: wrap;
          margin-top: 10px;
          margin-bottom: 14px;
          .inverterData-item {
            width: 202px;
            height: 92px;
            padding-top: 20px;
            text-align: center;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            background-image: url('~@/assets/img/common/bg_nationwide05.png');
            .label {
              font-size: 16px;
              color: #00ddff;
            }
            .value {
              margin-top: 10px;
              font-size: 24px;
              font-weight: bold;
              background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
        .socialBenefit-content {
          margin-top: 10px;
          margin-bottom: 12px;
          overflow: hidden;
          .socialBenefit-item {
            width: 420px;
            height: 61px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/common/bg_nationwide04.png');
            margin-top: 3px;
            &:first-child {
              margin-top: 0;
            }
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-left: 67px;
            .label {
              font-size: 16px;
              font-weight: bold;
              color: #eef3ff;
              width: 140px;
            }
            .value {
              width: 172px;
              text-align: center;
              span {
                font-size: 28px;
                font-weight: bold;
                background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
              }
            }
          }
        }
      }
    }
    .chart-view {
      height: 300px;
      margin-top: 10px;
      display: flex;
      align-items: flex-end;
      .chart-item {
        flex: 1;
        .chart-content {
          height: 250px;
        }
      }
    }
  }
}
</style>
