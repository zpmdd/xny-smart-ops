<template>
  <div class="cockpit">
    <div class="header">
      <div class="name">新能智维</div>
      <div class="user" @click="goUser"></div>
    </div>
    <div class="scroll-content page-content scroll">
      <div class="container">
        <div class="content-item top">
          <div class="content">
            <div class="t">
              <div class="l">
                <div class="top-chart" ref="top-chart">
                  <!-- 发电量
                  <br />
                  占比分析 -->
                  <div class="ring" ref="chart4"></div>
                </div>
                <div class="legend">
                  <div class="legend-item" v-for="(item, index) in powerScale.childs || []" :key="index">
                    <div class="color" :style="{ background: colorList[index] }"></div>
                    <div class="value">{{ item.label }}</div>
                  </div>
                </div>
              </div>
              <div class="r powerCount">
                <div class="powerCount-item" v-for="(item, index) in powerCount.childs || []" :key="index">
                  <div class="label">{{ item.label }}</div>
                  <div class="value">
                    {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="b devOpsData">
              <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
                <div class="label">{{ item.label }}</div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">/{{ item.unit }}</span>
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
        <div class="content-item chart">
          <MTittle :text="chart1Data.label || ''"></MTittle>
          <div class="chart-item" ref="chart1"></div>
        </div>
        <div class="content-item chart">
          <MTittle :text="chart2Data.label || ''"></MTittle>
          <div class="chart-item" ref="chart2"></div>
        </div>
        <div class="content-item chart">
          <MTittle :text="chart3Data.label || ''"></MTittle>
          <div class="chart-item" ref="chart3"></div>
        </div>
      </div>
    </div>
    <div class="footer">
      <div class="project" @click="goProject">
        <div class="icon"></div>
        <span>项目</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { moBileHomePage } from '@/api/home'
import icons from '@/utils/icons'
export default {
  data() {
    return {
      icons: icons,

      socialBenefit: {},
      powerScale: {},
      powerCount: {},
      devOpsData: {},

      chart1Dom: null,
      chart1Data: {},
      chart2Dom: null,
      chart2Data: {},
      chart3Dom: null,
      chart3Data: {},
      chart4Dom: null,
      chart4Data: {},
      chartBG: '',
      colorList: ['#688CFD', '#37D175', '#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3'],
    }
  },
  computed: {
    ...mapState({}),
  },
  created() {
    this.getData()
  },
  mounted() {},
  methods: {
    getData() {
      moBileHomePage().then((res) => {
        let data = res.data
        this.socialBenefit = data.socialBenefit || {}
        this.powerScale = data.powerScale || {}
        this.powerCount = data.powerCount || {}
        this.devOpsData = data.devOpsData || {}
        this.chart1Data = data.chartList[0] || {}
        this.chart2Data = data.chartList[1] || {}
        this.chart3Data = data.chartList[2] || {}
        this.chart4Data = data.powerScale ? JSON.parse(JSON.stringify(data.powerScale)) : {}
        if (this.chart4Data.childs?.length) {
          let chart4DataKeep = JSON.parse(JSON.stringify(this.chart4Data.childs))
          this.chart4Data.childs = [
            {
              childs: chart4DataKeep,
            },
          ]
        }
        this.chart4Data.childs[0].style = '2'
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '77, 105, 237'
        this.chart1Data.childs[1].style = '0'
        this.chart1Data.childs[1].color = '219, 92, 131'
        if (this.chart2Data.childs?.length) {
          this.chart2Data.childs[0].style = '0'
          this.chart2Data.childs[0].color = '77, 105, 237'
          this.chart2Data.childs[1].style = '0'
          this.chart2Data.childs[1].color = '219, 92, 131'
        }
        if (this.chart3Data.childs?.length) {
          this.chart3Data.childs[0].style = '0'
          this.chart3Data.childs[0].color = '77, 105, 237'
          this.chart3Data.childs[1].style = '0'
          this.chart3Data.childs[1].color = '219, 92, 131'
        }
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          this.initChart()
        })
      })
    },
    formarVal(list, index) {
      let total = list.slice(0, index == 0 ? index + 1 : index).reduce((pre, cur) => {
        return pre + Number(cur.value) * 100
      }, 0)
      return total
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needShadow: false,
          needStart: true,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        if (this.chart2Data.childs?.length) {
          let chart2Options = {
            data: this.chart2Data,
            needShadow: false,
            needStart: true,
          }
          this.chart2Dom.setOption(this.$initChart(chart2Options))
        }
        if (this.chart3Data.childs?.length) {
          let chart3Options = {
            data: this.chart3Data,
            needShadow: false,
            needStart: true,
          }
          this.chart3Dom.setOption(this.$initChart(chart3Options))
        }
        let chart4Options = {
          data: this.chart4Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          needPieLable: false,
          chartFull: true,
          colorList: this.colorList,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
      })
    },
    goProject() {
      this.$router.push({ path: '/project' })
    },
    goUser() {
      this.$router.push({ path: '/user' })
    },
  },
  components: {},
}
</script>
<style lang="scss" scoped>
.cockpit {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-size: 750px 342px;
  background-repeat: no-repeat;
  background-image: url('~@/assets/img/home/bg_bigbg.png');
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 108px 25px 0;
    .name {
      font-family: douyv;
      font-weight: 400;
      font-size: 36px;
      color: #ffffff;
    }
    .user {
      height: 31px;
      width: 31px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/common/icon_mine01.png');
    }
  }
  .top {
    .t {
      // min-height: 280px;
      padding: 14px 0;
      display: flex;
      .l {
        width: 330px;
        display: flex;
        flex-direction: column;
        align-items: center;
        .top-chart {
          margin-right: 30px;
          .ring {
            width: 180px;
            height: 180px;
          }
        }
        .legend {
          display: flex;
          justify-content: space-between;
          flex-wrap: wrap;
          gap: 10px 10px;
          margin-top: 14px;
          width: 100%;
          .legend-item {
            display: flex;
            align-items: center;
            width: calc(50% - 5px);
            .color {
              border-radius: 4px;
              width: 14px;
              height: 14px;
            }
            .value {
              width: calc(100% - 24px);
              margin-left: 10px;
              font-size: 22px;
              color: #292929;
            }
          }
        }
      }
      .r {
        width: 330px;
        .powerCount-item {
          min-height: 77px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          .label {
            width: 120px;
            font-size: 24px;
          }
          .value {
            max-width: calc(100% - 130px);
            font-weight: bold;
            font-size: 32px;
            .unit {
              font-size: 20px;
              font-weight: 400;
            }
          }
          &:nth-child(1) {
            .label {
              color: #12a476;
            }
          }
          &:nth-child(2) {
            border: 1px solid #dcdcdc;
            border-left: none;
            border-right: none;
            .label {
              color: #1860b1;
            }
          }
          &:nth-child(3) {
            .label {
              color: #da6b41;
            }
          }
        }
      }
    }
    .b {
      height: 150px;
      overflow-x: auto;
      background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
      border-radius: 14px;
      display: flex;
      align-items: center;
      padding: 0 18px;
      .devOpsData-item {
        height: 112px;
        border-radius: 14px;
        padding: 0 10px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        margin-right: 50px;
        &:last-child {
          margin-right: 0px;
        }
        .label {
          white-space: nowrap;
          font-size: 22px;
          color: #333333;
        }
        .value {
          white-space: nowrap;
          font-weight: bold;
          font-size: 32px;
          color: #292929;
          .unit {
            font-size: 20px;
            font-weight: 400;
          }
        }
        &:first-child {
          min-width: 142px;
          background-color: #ffffff;
          .label,
          .value {
            color: #004fdf;
          }
          .value {
            font-family: douyv;
          }
        }
      }
    }
  }
  .scroll-content {
    flex: 1;
    margin-top: 42px;
    overflow-y: auto;
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
  .chart {
    .chart-item {
      height: 422px;
    }
  }
  .footer {
    height: 166px;
    background-color: #fff;
    display: flex;
    justify-content: center;
    .project {
      display: flex;
      align-items: center;
      width: 228px;
      height: 84px;
      background: #edf5ff;
      border-radius: 42px;
      font-weight: bold;
      font-size: 34px;
      color: #323232;
      justify-content: center;
      margin-top: 8px;
      .icon {
        width: 44px;
        height: 51px;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/home/icon_show.png');
        background-repeat: no-repeat;
        margin-right: 30px;
      }
    }
  }
}
</style>
