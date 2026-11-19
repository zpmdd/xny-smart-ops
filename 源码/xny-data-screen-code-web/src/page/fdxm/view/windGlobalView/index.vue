<template>
  <div class="fdxm-windGlobalView view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="l">
        <TextTittle :label="dataView?.label || ''"></TextTittle>
        <div class="dataView-content">
          <div class="dataView-item" v-for="(item, index) in dataView.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <div class="fdAndsy-data">
          <div class="fdData-content" v-if="fdData.childs?.length">
            <div class="fdData-item-row" v-for="(item, index) in fdData.childs || []" :key="index">
              <img src="@/assets/img/common/bg_wpover03.png" class="icon" v-if="index == 0" />
              <div class="label"><img src="@/assets/img/common/bg_wpover05.png" v-if="index == 0" />{{ item.label }}</div>
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
              </div>
            </div>
          </div>
          <div class="syData-content" v-if="syData.childs?.length">
            <div class="syData-item-row" v-for="(item, index) in syData.childs || []" :key="index">
              <img src="@/assets/img/common/bg_wpover04.png" class="icon" v-if="index == 0" />
              <div class="label"><img src="@/assets/img/common/bg_wpover05.png" v-if="index == 0" />{{ item.label }}</div>
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
        <TextTittle :label="chart1Data?.label || ''"></TextTittle>
        <div class="chart-content" ref="chart1"></div>
      </div>
      <div class="c">
        <div class="currentData-content">
          <div class="currentData-item" v-for="(item, index) in currentData.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">
              {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
            </div>
          </div>
        </div>
        <div class="fanMonitor-content">
          <div class="fanMonitor-item" v-for="(item, index) in fanMonitor.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.fdxm.fanMonitor[index]})` }"></div>
            <div class="values">
              <div class="label" v-html="item.label"></div>
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="r">
        <TextTittle :label="socialContribution?.label || ''"></TextTittle>
        <div class="socialContribution-content">
          <div class="socialContribution-item" v-for="(item, index) in socialContribution?.childs || []" :key="index">
            <div class="label" v-html="item.label + (item.unit ? `(${item.unit})` : '')"></div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
        <TextTittle :label="devOpsData?.label || ''"></TextTittle>
        <div class="devOpsData-content">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="chart2Data?.label || ''"></TextTittle>
        <div class="chart-content" ref="chart2"></div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { windGlobalView } from '@/api/fdxm.js'
export default {
  data() {
    return {
      projectIcons: icons,

      // 实时数据
      currentData: {},
      // 数据一览
      dataView: {},
      // 风机状态
      fanMonitor: {},
      // 发电数据
      fdData: {},
      // 收益数据
      syData: {},
      // 社会效益
      socialContribution: {},
      // 运行数据
      devOpsData: {},

      chart1Dom: null,
      chart2Dom: null,
      chart1Data: {},
      chart2Data: {},
    }
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
    initData() {
      windGlobalView().then((res) => {
        let data = res.data
        this.currentData = data.currentData
        this.dataView = data.dataView
        this.fanMonitor = data.fanMonitor
        this.fdData = data.fdData
        this.syData = data.syData
        this.socialContribution = data.socialContribution
        this.devOpsData = data.devOpsData
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '41, 245, 175'
        this.chart2Data = data.chartList[1]
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '29, 210, 255'

        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needLegend: false,
          needStart: true,
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
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
  },
}
</script>
<style lang="scss" scoped>
.fdxm-windGlobalView {
  height: 100%;
  background-image: url('~@/assets/img/fdxm/1920-1080.gif');
  background-repeat: no-repeat;
  background-size: 100% 100%;
  .view-container {
    display: flex;

    .l {
      width: 440px;
      .dataView-content {
        margin-top: 10px;
        .dataView-item {
          width: 440px;
          height: 147px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fdxm/bg_wpover01.png');
          padding-left: 190px;
          padding-top: 46px;
          .label {
            color: #cde3f6;
            font-size: 14px;
          }
          .value {
            font-size: 30px;
            color: #f4fdff;
            font-family: douyv;
            word-break: break-all;
          }
        }
      }
      .fdAndsy-data {
        display: flex;
        justify-content: space-between;
        margin-top: 17px;
        margin-bottom: 30px;
        .fdData-content,
        .syData-content {
          width: 205px;
          min-height: 256px;
          background-image: url('~@/assets/img/common/bg_wpover02.png');
          background-size: 100%;
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
            min-height: 74px;
            &:first-child {
              padding-left: 70px;
              text-align: left;
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
      .chart-content {
        height: 346px;
      }
    }
    .c {
      flex: 1;
      .fanMonitor-content {
        position: absolute;
        display: flex;
        flex-wrap: wrap;
        bottom: 50px;
        left: 552px;
        padding: 22px 30px 17px;
        justify-content: space-around;
        border: 1px solid;
        border-image: linear-gradient(to right, rgba(#51c8ff, 0) 3%, rgba(#51c8ff, 0.8) 50%, rgba(#51c8ff, 0) 97%) 1;
        border-left: none;
        border-right: none;
        background-image: url('~@/assets/img/common/bg_pgo05.png');
        background-repeat: no-repeat;
        background-size: 100% 100%;
        .fanMonitor-item {
          display: flex;
          .icon {
            width: 67px;
            height: 67px;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            margin-right: 8px;
          }
          .values {
            width: 108px;
            .label {
              color: #fff;
              font-size: 16px;
              background: linear-gradient(90deg, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0));
              border-left: 2px solid #80c5f0;
              padding: 0 6px;
            }
            .value {
              color: #86d9ff;
              font-size: 26px;
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
      .currentData-content {
        position: absolute;
        top: 310px;
        left: 666px;
        width: 263px;
        min-height: 131px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding: 8px 0 0 17px;
        background-image: url('~@/assets/img/fdxm/bg_wpover07.png');
        .currentData-item {
          display: flex;
          align-items: flex-end;
          &:nth-child(1) {
            .value {
              font-size: 20px;
            }
          }
          &:nth-child(3) {
            .value {
              color: #e3cb78;
            }
          }
          .label {
            font-size: 14px;
            color: #fff;
            padding-left: 6px;
            border-left: 3px solid #00f6ff;
            width: 100px;
          }
          .value {
            color: #2ccac8;
            font-size: 28px;
            font-weight: bold;
            font-style: italic;
            word-break: break-all;
            .unit {
              font-size: 14px;
              color: #fff;
              font-weight: 400;
            }
          }
        }
      }
    }
    .r {
      width: 480px;
      .socialContribution-content {
        margin-top: 15px;
        overflow: hidden;
        margin-bottom: 26px;
        .socialContribution-item {
          width: 474px;
          height: 68px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fdxm/bg_wpover06.png');
          margin-top: 4px;
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
            width: 222px;
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
      .devOpsData-content {
        display: flex;
        flex-wrap: wrap;
        margin-top: 12px;
        margin-bottom: 20px;
        padding-left: 23px;
        .devOpsData-item {
          width: 210px;
          height: 101px;
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
      .chart-content {
        height: 356px;
      }
    }
  }
}
</style>
