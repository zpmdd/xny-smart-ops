<template>
  <div class="sdxm">
    <div class="sdxm-content">
      <div class="top">
        <div class="top-l">
          <div class="title">
            <div class="title-text">{{ sdxmData?.topL?.label || '' }}</div>
          </div>
          <div class="value">
            <div class="item" v-for="(item, index) in sdxmData?.topL?.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? `/${item.unit}` : '' }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="top-c">
          <div class="item" v-for="(item, index) in sdxmData?.topC?.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.sdxmTopCenter[index + 1]})` }">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <div class="top-r">
          <div class="title">
            <div class="title-text">{{ sdxmData?.topR?.label || '' }}</div>
          </div>
          <div class="value">
            <div class="item" v-for="(item, index) in sdxmData?.topR?.childs || []" :key="index">
              <div class="value">
                {{ item.value | formatNum }} <span class="unit" v-if="item.unit">{{ item.unit ? `${item.unit}` : '' }}</span>
              </div>
              <div class="label">
                <div class="l-icon"></div>
                <div class="name" v-html="item.label"></div>
                <div class="r-icon"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom">
        <div class="bottom-l">
          <div class="title">
            <div class="title-text">{{ '当日潮汐预报-高低潮' }}</div>
          </div>
          <div class="bottomLT-value">
            <div v-for="(item, index) in sdxmData?.bottomLT || []" :key="index">
              <div v-for="(item1, index1) in item" :key="index1">
                {{ item1.value || '' }}
              </div>
            </div>
          </div>
          <div class="title">
            <div class="title-text">{{ '当日潮流预报-高低潮' }}</div>
          </div>
          <div class="bottomLB-value">
            <div v-for="(item, index) in sdxmData?.bottomLB || []" :key="index">
              <div v-for="(item1, index1) in item.slice(0, 5)" :key="index1">
                {{ item1.value || '' }}
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-c">
          <div class="title">
            <div class="title-text">{{ '当日潮流预报-流速' }}</div>
          </div>
          <div class="chart-content" ref="chart1"></div>
        </div>
        <div class="bottom-r">
          <div class="title">
            <div class="title-text">{{ '当日潮汐预报-潮位' }}</div>
          </div>
          <div class="chart-content" ref="chart2"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { waterGlobalView } from '@/api/sdxm.js'
export default {
  name: 'sdxmdetail',
  data() {
    return {
      projectIcons: icons,
      sdxmData: {
        topL: {},
        topC: {},
        topR: {},
        bottomLT: {},
        bottomLB: {},
      },
      chart1: {},
      chart2: {},
    }
  },
  created() {
    waterGlobalView({}).then((res) => {
      if (res) {
        let backData = res.data
        this.sdxmData.topR = backData.socialContribution
        this.sdxmData.topL = backData.dataView
        this.sdxmData.topC = backData.realTime
        this.sdxmData.bottomLT = backData.timeAndLevel
        this.sdxmData.bottomLB = backData.velocityOfFlow
        this.chart1 = backData.chartList[0]
        this.chart2 = backData.chartList[1]
        this.initCharts()
      }
    })
  },
  methods: {
    initCharts() {
      this.$nextTick(() => {
        this.chart1.childs[0].style = '0'
        this.chart1.childs[0].color = '29, 210, 255'
        let chart1Data = {
          data: this.chart1,
          needLegend: false,
        }
        let chart1 = this.$echarts.init(this.$refs.chart1)
        chart1.setOption(this.$initChart(chart1Data))

        this.chart2.childs[0].style = '0'
        this.chart2.childs[0].color = '41, 245, 175'
        let chart2Data = {
          data: this.chart2,
          needLegend: false,
        }
        let chart2 = this.$echarts.init(this.$refs.chart2)
        chart2.setOption(this.$initChart(chart2Data))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.sdxm {
  height: 1600px;
  margin-top: -198px;
  padding-top: 198px;
  background-size: 1750px 1137px;
  background-image: url('~@/assets/img/sdxm/wae_bg_base.png');
  background-repeat: no-repeat;
  background-position: center -82px;
  .title-text {
    color: #fff;
    font-size: 24px;
    line-height: 53px;
    height: 53px;
    width: 600px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/data/bg_title.png');
    font-family: douyv;
    padding-left: 122px;
  }
  .sdxm-content {
    height: 100%;
    overflow-y: auto;
    .top {
      display: flex;
      align-items: flex-start;
      .top-l {
        width: 600px;
        & > .value {
          padding-top: 8px;
          margin-left: 16px;
          .item {
            margin-top: 12px;
            width: 584px;
            height: 80px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/sdxm/wae_bg03.png');
            padding-left: 86px;
            .label {
              color: #eef3ff;
              font-size: 21px;
            }
            .value {
              min-width: 194px;
              text-align: center;
              background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
              font-size: 36px;
              font-weight: bold;
            }
          }
        }
      }
      .top-c {
        flex: 1;
        padding: 20px 90px;
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        .item {
          width: 193px;
          height: 69px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          padding-left: 53px;
          padding-top: 10px;
          .label {
            font-size: 14px;
            color: #1ff2ff;
          }
          .value {
            color: #e8feff;
            font-size: 22px;
            margin-top: 4px;
            padding-left: 8px;
          }
        }
      }
      .top-r {
        & > .value {
          margin-left: 116px;
          .item {
            .value {
              width: 462px;
              height: 96px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-image: url('~@/assets/img/sdxm/wae_bg06.png');
              font-size: 42px;
              line-height: 96px;
              color: #fff;
              font-weight: bold;
              text-align: center;
              .unit {
                font-weight: 400;
                font-size: 24px;
              }
            }
            .label {
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 24px;
              margin-top: 12px;
              color: #fff;
              .name {
                text-align: center;
                padding: 0 40px;
              }
              .l-icon,
              .r-icon {
                height: 24px;
                width: 21px;
                background-repeat: no-repeat;
                background-size: 100% 100%;
                background-position: center center;
              }
              .l-icon {
                background-image: url('~@/assets/img/energyStorage/bg_energy_equipment08.png');
              }
              .r-icon {
                background-image: url('~@/assets/img/energyStorage/bg_energy_equipment0509.png');
              }
            }
          }
        }
      }
    }
    .bottom {
      margin-top: 50px;
      display: flex;
      gap: 0px 80px;
      .bottom-l {
        width: 568px;
        .bottomLT-value,
        .bottomLB-value {
          margin-top: 20px;
          margin-bottom: 20px;
          & > div {
            height: 64px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/sdxm/wae_bg05.png');
            margin-top: 10px;
            color: #eef3ff;
            font-size: 22px;
            &:first-child {
              background-image: url('~@/assets/img/sdxm/wae_bg04.png');
              margin-top: 0;
            }
            & > div {
              flex: 1;
              padding: 0 0 0 20px;
              &:first-child {
                min-width: 140px;
              }
            }
          }
        }
      }
      .bottom-c {
        flex: 1;
        .chart-content {
          height: 460px;
        }
      }
      .bottom-r {
        flex: 1;
        .chart-content {
          height: 460px;
        }
      }
    }
  }
}
</style>
