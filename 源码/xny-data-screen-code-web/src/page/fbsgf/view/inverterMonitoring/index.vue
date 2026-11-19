<template>
  <div class="fbsgf-inverterMonitoring view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="top">
        <div class="t-l">
          <TextTittle :label="pageData?.inverterDataStatistics?.label || ''"></TextTittle>
          <div class="t-l-content">
            <div class="t-l-content-statistics" v-for="(item, index) in pageData?.inverterDataStatistics?.childs || []" :key="index">
              <div class="label">{{ item.label || '' }}{{ item.unit ? `(${item.unit})` : '' }}</div>
              <div class="value">
                <div v-for="(i, _index) in formatNumber(item.value)" :key="_index" :class="{ isNum: i != ',', comma: i == ',' }">{{ i }}</div>
              </div>
            </div>
            <div class="t-l-content-bottom">
              <div class="t-l-content-bottom-item" v-for="(item, index) in pageData?.inverterData?.childs" :key="index" :style="{ backgroundImage: `url(${projectIcons.fbsgf_inverter[index < 2 ? 1 : 2]})` }">
                <div class="label">{{ item.label || '' }}</div>
                <div class="value">
                  {{ item.value || '' }} <span class="unit" v-if="item.unit"> {{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="t-r">
          <TextTittle :label="(pageData?.chartList && pageData?.chartList[0]?.label) || ''"></TextTittle>
          <div class="chart-content" ref="chart1"></div>
        </div>
      </div>
      <div class="bottom">
        <TextTittle :label="'逆变器实时数据'"></TextTittle>
        <div class="bottom-content">
          <div class="table-content">
            <div class="scroll">
              <div v-for="(item, index) in pageData?.inverterCurrentData?.list || []" :key="index">
                <div v-for="(item1, index1) in item" :class="{ err: item1.status == 1, normal: item1.status == 0 }" :key="index1">
                  <em>{{ item1.value }}</em>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { inverterMonitoring } from '@/api/fbsgf.js'
import icons from '@/utils/icons.js'
export default {
  data() {
    return {
      projectIcons: icons,
      chart1Dom: null,
      chart1Data: {},
      pageData: {},
      times: null,
      scrollTimer: null,
      scrollTopNum: 0,
      column: '',
    }
  },
  computed: {
    formatNumber() {
      return (num) => {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      }
    },
  },
  created() {
    this.initData()
  },
  mounted() {
    if (this.times) {
      return
    }
    this.times = setInterval(() => {
      inverterMonitoring().then((res) => {
        if (res) {
          let backData = res.data
          this.pageData.inverterCurrentData = {
            list: backData.realTimeData,
          }
          this.column = backData.realTimeData[0].length * 10.9375 + 'rem'
        }
      })
    }, 30000)
    this.$bus.$on('onresize', () => {
      this.initChart()
    })
    this.$nextTick(() => {
      this.roomdetailscroll()
    })
    let tableContent = document.querySelector('.table-content')
    tableContent.addEventListener('scroll', () => {
      clearInterval(this.scrollTimer)
      this.scrollTopNum = tableContent.scrollTop
      this.roomdetailscroll()
    })

    this.$bus.$on('refreshData', () => {
      this.initData('1')
    })
  },
  methods: {
    roomdetailscroll() {
      let tableContent = document.querySelector('.table-content')
      let tableScroll = document.querySelector('.scroll')
      if (this.scrollTimer) {
        clearInterval(this.scrollTimer)
      }
      this.scrollTimer = setInterval(() => {
        if (this.scrollTopNum > tableScroll.clientHeight - tableContent.clientHeight) {
          this.scrollTopNum = 0
        }
        this.scrollTopNum += 1
        tableContent.scrollTo({ top: this.scrollTopNum, left: tableContent.scrollLeft, behavior: 'smooth' })
      }, 100)
    },
    initData(needInitChart) {
      inverterMonitoring().then((res) => {
        if (res) {
          let backData = res.data
          this.pageData = {
            chartList: backData.chartList,
            inverterDataStatistics: backData.inverterDataStatistics,
            inverterData: backData.otherStatistics,
            inverterCurrentData: {
              list: backData.realTimeData,
            },
          }
          this.column = backData.realTimeData[0].length * 10.9375 + 'rem'
          this.chart1Data = backData.chartList[0]
          this.chart1Data.childs[0].style = '0'
          this.chart1Data.childs[0].color = '50, 195, 104'
          this.$nextTick(() => {
            if (!needInitChart) {
              this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
            }
            this.initChart()
          })
        }
      })
    },
    initChart() {
      let chart1Options = {
        data: this.chart1Data,
        needStart: true,
      }
      this.chart1Dom.setOption(this.$initChart(chart1Options))
      this.chart1Dom.resize()
    },
  },
  beforeDestroy() {
    clearInterval(this.times)
    clearInterval(this.scrollTimer)
    this.$bus.$off('onresize')
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.fbsgf-inverterMonitoring {
  overflow-y: auto;
  .top {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    .t-l {
      width: 620px;
      .t-l-content-statistics {
        margin-top: 16px;
        .label {
          color: #15a3f2;
          font-size: 16px;
        }
        .value {
          display: flex;
          align-items: center;
          font-size: 40px;
          color: #fff;
          margin-top: 8px;
          .isNum {
            width: 44px;
            height: 61px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/fbsgf/bg_pim0.png');
            line-height: 50px;
            text-align: center;
            margin-left: 15px;
            &:first-child {
              margin-left: 0;
            }
          }
          .comma + .isNum {
            margin-left: 0;
          }
        }
      }
      .t-l-content-bottom {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        margin-top: 15px;
        gap: 8px 0;
        .t-l-content-bottom-item {
          width: 304px;
          height: 71px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          padding-left: 92px;
          padding-top: 8px;
          .label {
            font-size: 14px;
            color: #b4ebff;
          }
          .value {
            font-size: 26px;
            font-weight: bold;
          }
          .unit {
            font-size: 16px;
          }
          &:nth-child(1),
          &:nth-child(2) {
            .value,
            .unit {
              color: #b4ebff;
              background: linear-gradient(180deg, rgba(#fff, 1) 0%, #4bbefa 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
              text-shadow: 0 6px 0px rgba(#4bbefa, 0.25);
            }
          }
          &:nth-child(3),
          &:nth-child(4) {
            .value,
            .unit {
              color: #b4ebff;
              background: linear-gradient(180deg, rgba(#fff, 1) 0%, #03d9b5 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
              text-shadow: 0 6px 0px rgba(#03d9b5, 0.25);
            }
          }
        }
      }
    }
    .t-r {
      width: calc(100% - 640px);
      .chart-content {
        height: 270px;
      }
    }
  }
  .bottom {
    margin-top: 30px;
    .bottom-content {
      overflow: auto;
      .title {
        & > div {
          color: #a1b8cd;
          display: flex;
          height: 68px;
          background: linear-gradient(90deg, rgba(3, 0, 0, 0.17), rgba(255, 255, 255, 0.17), rgba(43, 40, 40, 0.17));
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fbsgf/bg_pim04.png');
          .normal {
            color: #4cdf5d;
          }
          .err {
            color: #ff5858;
          }
          & > div {
            min-width: 210px;
            display: flex;
            align-items: center;
            color: #afbed8 !important;
            font-size: 20px;
            border: none;
          }
        }
      }
      .table-content {
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/fbsgf/bg_pim05.png');
        background-size: 100% 104px;
        background-position: -21px 334px;
        height: 506px;
        overflow-y: auto;
        margin-top: 9px;
        &::-webkit-scrollbar {
          display: block;
          width: 0;
          /*  设置纵轴（y轴）轴滚动条 */
          height: 8px;
          /*  设置横轴（x轴）轴滚动条 */
        }

        &::-webkit-scrollbar-thumb {
          border-radius: 3px;
          box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
          background: #eef3ff;
        }

        &::-webkit-scrollbar-track {
          border-radius: 3px;
          box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
          background: #999;
        }
        .scroll {
          position: relative;
          & > div {
            height: 62px;
            margin-top: 9px;
            border: 1px solid #6e87c5;
            width: v-bind(column);
            margin-left: 5px;
            color: #a1b8cd;
            display: flex;
            background: linear-gradient(90deg, rgba(3, 0, 0, 0.17), rgba(255, 255, 255, 0.17), rgba(43, 40, 40, 0.17));
            &:first-child {
              position: sticky;
              top: 0;
              background-color: rgb(3, 0, 0);
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-image: url('~@/assets/img/fbsgf/bg_pim04.png');
              border: none;
              margin-top: 0;
              * {
                color: #afbed8 !important;
                font-size: 20px;
              }
            }
            .normal {
              color: #4cdf5d;
            }
            .err {
              color: #ff5858;
            }
            & > div {
              min-width: 210px;
              max-width: 210px;
              display: flex;
              align-items: center;
              word-break: break-all;
              &:first-child {
                padding-left: 25px;
                color: #afbed8;
              }
            }
          }
        }
      }
    }
  }
}
</style>
