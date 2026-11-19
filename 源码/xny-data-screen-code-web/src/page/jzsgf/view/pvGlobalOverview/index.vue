<template>
  <div class="jzsgf-pvGlobalOverview view" :class="{ 'overview-screen weijiazhuang': weijiazhuang }">
    <MyHeader></MyHeader>
    <template v-if="weijiazhuang">
      <router-link to="/province" class="overview-back">‹ 返回陕西省</router-link>
      <div class="station-heading">
        <h1>韦家庄光伏</h1>
        <p>陕西省宝鸡市 · 集中式光伏</p>
        <p>实时有功功率　<strong>{{ power }} MW</strong></p>
      </div>
    </template>
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
        <div class="r">
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
        <div class="chart-item">
          <TextTittle v-if="chart3Data?.label" :label="chart3Data?.label || ''"></TextTittle>
          <div class="chart-content" ref="chart3"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { readSnapshot } from '@/data/overview.mjs'

export default {
  name: 'jzsgfPvGlobalOverview',
  props: { weijiazhuang: Boolean, fetchOverview: Function },
  data() {
    return {
      projectIcons: icons, power: 0,
      fdData: {}, syData: {}, envData: {}, otherData: {}, devOpsData: {}, socialBenefit: {},
      chart1Data: {}, chart3Data: {},
    }
  },
  mounted() {
    this.charts = [this.$refs.chart1, this.$refs.chart3].map(el => this.$echarts.init(el, null, { renderer: 'svg' }))
    this.$bus.$on('onresize', this.drawCharts)
    this.$bus.$on('refreshData', this.loadOverview)
    this.loadOverview()
  },
  methods: {
    async loadOverview() {
      const data = this.weijiazhuang ? readSnapshot().station : (await this.fetchOverview()).data
      if (this._isDestroyed) return
      if (this.weijiazhuang) this.power = data.power
      this.fdData = data.fdData || {}
      this.syData = data.syData || {}
      this.envData = data.envData || {}
      this.otherData = data.otherData || {}
      this.devOpsData = { ...data.devOpsData, childs: [...(data.devOpsData?.childs || []), ...(data.failureData?.childs || [])] }
      this.socialBenefit = data.socialBenefit || {}
      this.chart1Data = this.weijiazhuang ? data.powerChart : data.chartList[0]
      this.chart3Data = this.weijiazhuang ? data.energyChart : data.chartList[2]
      this.$nextTick(this.drawCharts)
    },
    drawCharts() {
      if (!this.charts || !this.chart1Data.childs || !this.chart3Data.childs) return
      const draw = (chart, data, style, color) => {
        const childs = data.childs.map(series => ({ ...series, style, color }))
        chart.resize()
        chart.setOption(this.$initChart({ data: { ...data, childs }, needStart: true, vm: this, smooth: style === '0' }))
      }
      draw(this.charts[0], this.chart1Data, '0', '29, 210, 255')
      draw(this.charts[1], this.chart3Data, '1', '126, 184, 255')
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize', this.drawCharts)
    this.$bus.$off('refreshData', this.loadOverview)
    this.charts.forEach(chart => chart.dispose())
    this.charts = null
  },
}
</script>
<style lang="scss" scoped>
.weijiazhuang.jzsgf-pvGlobalOverview {
  background-color: #071827;
  background-image:
    linear-gradient(0deg, #061424 1%, rgba(4,17,32,.96) 25%, transparent 57%, rgba(5,18,31,.1) 77%, #071523 100%),
    linear-gradient(90deg, rgba(3,13,27,.98) 0%, rgba(3,13,27,.84) 18%, transparent 39%, transparent 61%, rgba(3,13,27,.85) 82%, rgba(3,13,27,.98) 100%),
    url('~station-photo');
  background-size: 100% 100%, 100% 100%, cover;
  background-position: center, center, center 55%;
  .view-container .chart-view {
    height: 250px;
    .chart-item .chart-content { height: 198px; }
  }
  .station-heading {
    position: absolute;
    left: 490px;
    bottom: 355px;
    color: #fff;
    text-shadow: 0 2px 10px #071827;
    h1 { font-size: 34px; letter-spacing: 3px; }
    p { margin-top: 5px; font-size: 18px; color: #d1e6ef; }
  }
}
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
