<template>
  <div class="fdxmdetail">
    <Tabbar :tabbar="tabbarList" :active.sync="active"></Tabbar>
    <div class="active2" key="active2" v-if="active == 0">
      <div class="top">
        <div class="top-l">
          <div class="title">
            <div class="title-text">{{ active1Data?.topLT?.label || '' }}</div>
          </div>
          <div class="top-l-content">
            <div class="content-t">
              <div class="content-t-item" v-for="(item, index) in active1Data?.topLT?.childs || []" :key="index">
                <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
            <div class="content-b">
              <div class="content-b-item" v-for="(item, index) in active1Data?.topLB?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.jzsgfDetail1TopL[index + 1] + ')' }">
                <div class="item" v-for="(item1, index1) in item.childs || []" :key="index1">
                  <div class="label">
                    <div class="tip-icon" v-if="index1 == 0"></div>
                    {{ item1.label }}
                  </div>
                  <div class="value">
                    {{ item1.value }} <span class="unit" v-if="item1.unit">/{{ item1.unit }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="top-c">
          <div class="popover" v-if="active1Data.topC">
            <div class="popover-item" v-for="(item, index) in active1Data.topC?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">
                <em>
                  {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit }}</span></em
                >
              </div>
            </div>
          </div>
        </div>
        <div class="top-r">
          <div class="top-r-t">
            <div class="title">
              <div class="title-text">{{ active1Data?.topRT?.label || '' }}</div>
            </div>
            <div class="top-r-content">
              <div class="top-r-content-item" v-for="(item, index) in active1Data?.topRT?.childs || []" :key="index">
                <div class="label" v-html="item.label + (item.unit ? `/${item.unit}` : '')"></div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
          </div>
          <div class="top-r-b">
            <div class="title">
              <div class="title-text">{{ active1Data?.topRB?.label || '' }}</div>
            </div>
            <div class="top-r-content">
              <div class="top-r-content-item" v-for="(item, index) in active1Data?.topRB?.childs || []" :key="index">
                <div class="label" v-html="item.label + (item.unit ? `/${item.unit}` : '')"></div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom">
        <div class="b-l">
          <div class="title">
            <div class="title-text">{{ chart1.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart1"></div>
        </div>
        <div class="b-c">
          <div class="b-c-content">
            <div class="b-c-content-item" v-for="(item, index) in active1Data?.bottomC?.childs || []" :key="index">
              <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fdxmGlobalViewBottom[index + 1] + ')' }"></div>
              <div class="values">
                <div class="label">{{ item.label }}</div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="b-r">
          <div class="title">
            <div class="title-text">{{ chart2.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart2"></div>
        </div>
      </div>
    </div>
    <div class="active1" key="active1" v-if="active == 1">
      <div class="fdxm-top">
        <div class="fdxm-top-item" v-for="(item, index) in active2Data?.top?.childs || []" :key="index">
          <div class="value">{{ item.value }}</div>
          <div class="label">
            <div class="l-icon"></div>
            <div class="name">{{ item.label }}{{ item.unit ? `${item.unit}` : '' }}</div>
            <div class="r-icon"></div>
          </div>
        </div>
      </div>
      <div class="fdxm-bottom">
        <div
          class="fdxm-bottom-item"
          v-for="(item, index) in active2Data?.bottom?.childs || []"
          :key="index"
          :class="{ err: item?.childs[0]?.value == '故障', normal: item?.childs[0]?.value == '正常发电', notEnabled: item?.childs[0]?.value == '未启用' }"
        >
          <div class="values" v-for="(item1, index1) in item.childs.slice(1) || []" :key="index1">
            <div class="label">{{ item1.label }}</div>
            <div class="value">
              {{ item1.value }} <span class="unit" v-if="item1.unit">{{ item1.unit || '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Tabbar from '../components/Tabbar'
import icons from '@/utils/icons.js'
import { blowerMonitor, windGlobalView } from '@/api/fdxm.js'
export default {
  name: 'fdxmdetail',
  components: {
    Tabbar,
  },
  data() {
    return {
      projectIcons: icons,
      tabbarList: [{ label: '全局总览' }, { label: '风机监测' }],
      active: 0,
      active2Data: {
        top: {},
        bottom: {},
      },
      active1Data: { topLT: {}, topLB: {}, topC: {}, topR: {}, bottomC: {} },
      chart1: {},
      chart2: {},
    }
  },
  watch: {
    active: {
      handler() {
        this.$store.commit('status/windmill', this.active === 0)
        if (this.active == 0) {
          this.getWindGlobalView()
        }
        if (this.active == 1) {
          this.getBlowerMonitor()
        }
      },
      immediate: true,
    },
  },
  created() {},
  methods: {
    // 获取全局概览
    getWindGlobalView() {
      windGlobalView().then((res) => {
        let data = res.data
        this.active1Data = {
          topLT: data.dataView,
          topLB: {
            childs: [data.fdData, data.syData],
          },
          topC: data.currentData,
          topRB: data.socialContribution,
          topRT: data.devOpsData,
          bottomC: data.fanMonitor,
        }
        this.chart1 = data.chartList[0]
        this.chart2 = data.chartList[1]
        this.initCharts()
      })
    },
    // 获取风机数据
    getBlowerMonitor() {
      blowerMonitor().then((res) => {
        this.active2Data.top = res.data.topInfo
        this.active2Data.bottom = res.data.blowerInfo
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.chart1.childs[0].style = '0'
        this.chart1.childs[0].color = '41, 245, 175'
        let chart1Data = {
          data: this.chart1,
          needLegend: false,
          needStart: true,
          chartFull: true,
        }
        let chart1 = this.$echarts.init(this.$refs.chart1)
        chart1.setOption(this.$initChart(chart1Data))

        this.chart2.childs[0].style = '0'
        this.chart2.childs[0].color = '29, 210, 255'
        let chart2Data = {
          data: this.chart2,
          needLegend: false,
          needStart: true,
          chartFull: true,
        }
        let chart2 = this.$echarts.init(this.$refs.chart2)
        chart2.setOption(this.$initChart(chart2Data))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fdxmdetail {
  height: 100%;
  .title-text {
    color: #fff;
    font-size: 24px;
    line-height: 53px;
    height: 53px;
    width: 570px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/data/bg_title.png');
    font-family: douyv;
    padding-left: 122px;
  }
  .active2 {
    .top {
      display: flex;
      justify-content: space-between;
      .top-l {
        width: 590px;
        .top-l-content {
          margin-top: 26px;
          .content-t {
            .content-t-item {
              width: 571px;
              height: 189px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              background-image: url('~@/assets/img/fdxm/bg_blower.png');
              padding-left: 258px;
              display: flex;
              justify-content: center;
              flex-direction: column;
              .label {
                color: #cde3f6;
                font-size: 20px;
              }
              .value {
                color: #f4fdff;
                font-size: 40px;
                font-family: douyv;
              }
            }
          }
          .content-b {
            display: flex;
            width: 578px;
            justify-content: space-between;
            margin-top: 20px;
            .content-b-item {
              min-width: 268px;
              width: 268px;
              height: 334px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              .item {
                display: flex;
                flex-direction: column;
                .label {
                  font-size: 16px;
                  color: #fff;
                  font-family: douyv;
                  //   display: flex;
                  //   align-items: flex-start;
                }
                .value {
                  font-size: 28px;
                  font-family: douyv;
                  background: linear-gradient(0deg, #0064f8 0.1953125%, #70fffa 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                  .unit {
                    font-size: 12px;
                    color: #fff;
                    background: linear-gradient(0deg, #fff, #fff);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                  }
                }
                &:first-child {
                  padding-left: 94px;
                  padding-top: 14px;
                  height: 102px;
                  justify-content: center;
                }
                &:nth-child(2) {
                  text-align: center;
                  width: 224px;
                  margin: 0 auto;
                  height: 100px;
                  justify-content: center;
                  border-bottom: 2px solid #002458;
                }
                &:nth-child(3) {
                  text-align: center;
                  width: 224px;
                  margin: 0 auto;
                  height: 100px;
                  justify-content: center;
                }
                .tip-icon {
                  width: 20px;
                  height: 21px;
                  margin-right: 10px;
                  background-size: 100% 100%;
                  background-repeat: no-repeat;
                  display: inline-block;
                  vertical-align: middle;
                  background-image: url('~@/assets/img/jzsgf/bg_sp_os05.png');
                }
              }
            }
          }
        }
      }
      .top-c {
        flex: 1;
        .popover {
          min-width: 325px;
          min-height: 162px;
          display: inline-block;
          padding: 15px 23px 38px;
          margin-left: 200px;
          margin-top: 200px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fdxm/bg_blower01.png');
          .popover-item {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            .label {
              padding-left: 10px;
              border-left: 4px solid #00f6ff;
              margin-top: 12px;
              color: #fff;
              font-size: 18px;
            }
            .value {
              em {
                font-size: 24px;
                color: #2ccac8;
                font-weight: bold;
                min-width: 130px;
                .unit {
                  font-size: 18px;
                  color: #fff;
                  font-weight: 400;
                }
              }
            }
          }
        }
      }
      .top-r-t,
      .top-r-b {
        width: 590px;
        .top-r-content {
          margin-top: 10px;
          .top-r-content-item {
            height: 80px;
            width: 584px;
            line-height: 80px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/jzsgf/bg_sp_os08.png');
            padding-left: 86px;
            display: flex;
            justify-content: space-between;
            margin-top: 4px;
            &:first-child {
              margin-top: 0;
            }
            .label {
              font-size: 21px;
              color: #eef3ff;
              font-weight: bold;
            }
            .value {
              min-width: 250px;
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
      .top-r-b{
        margin-top: 30px;
      }
    }
    .bottom {
      display: flex;
      justify-content: space-between;
      align-items: flex-end;
      margin-top: 40px;
      height: 520px;
      .chart-content {
        height: 467px;
      }
      .b-c {
        flex: 1;
        padding: 0 23px;
        margin-bottom: 30px;
        .b-c-content {
          padding: 25px 0;
          display: flex;
          justify-content: space-around;
          background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
          border: 2px solid;
          border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
          border-image-slice: 1;
          .b-c-content-item {
            display: flex;
            align-items: center;
            .icon {
              width: 94px;
              height: 94px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              margin-right: 16px;
            }
            .values {
              .label {
                min-width: 150px;
                background: linear-gradient(90deg, #286eab 0%, rgba(#286eab, 0));
                color: #fff;
                font-size: 24px;
                padding-left: 10px;
                border-left: 3px solid #80c5f0;
              }
              .value {
                color: #86d9ff;
                font-size: 38px;
                margin-top: 12px;
                font-weight: bold;
              }
            }
          }
        }
      }
    }
  }
  .active1 {
    // height: calc(100% - 20px);
    overflow-y: auto;
    margin-top: 20px;
    .fdxm-top {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .fdxm-top-item {
        .value {
          width: 336px;
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
    .fdxm-bottom {
      margin-top: 44px;
      display: flex;
      flex-wrap: wrap;
      gap: 50px;
      .fdxm-bottom-item {
        width: 540px;
        height: 213px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding: 6px 42px 0 240px;
        .values {
          min-height: 60px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          .label {
            font-size: 22px;
            color: #fff;
          }
          .value {
            background: linear-gradient(to top, #4bbefa 0%, #fff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            font-size: 30px;
            font-weight: bold;
            .unit {
              font-size: 20px;
            }
          }
        }
      }
      .err {
        background-image: url('~@/assets/img/fdxm/bg_blower002.png');
      }
      .normal {
        background-image: url('~@/assets/img/fdxm/bg_blower001.png');
      }
    }
  }
}
</style>
