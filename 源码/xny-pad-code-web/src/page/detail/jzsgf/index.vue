<template>
  <div class="jzsgf" :class="{ 'jzsgf-active1': active === 0 }">
    <Tabbar :tabbar="tabbarList" :active.sync="active"></Tabbar>
    <div class="active1" key="active1" v-if="active == 0">
      <div class="bg"></div>
      <div class="top">
        <div class="top-l">
          <div class="title">
            <div class="title-text">{{ active1Data?.topLT?.label || '' }}</div>
          </div>
          <div class="top-l-content">
            <div class="content-t">
              <div class="content-t-item" v-for="(item, index) in active1Data?.topLT?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.jzsgfDetail1TopL[index + 1] + ')' }">
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
            <div class="content-b">
              <div class="content-b-item" v-for="(item, index) in active1Data?.topLB?.childs || []" :key="index">
                <div class="label">{{ item.label }}{{ item.unit ? `/${item.unit}` : '' }}</div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="top-c">
          <div class="top-c-item" v-for="(item, index) in active1Data?.topC?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.jzsgfDetail1TopC[index + 1] + ')' }">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <div class="top-r">
          <!-- <div class="title">
            <div class="title-text">{{ active1Data?.topR?.label || '' }}</div>
          </div>
          <div class="top-r-content">
            <div class="top-r-content-item" v-for="(item, index) in active1Data?.topR?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div> -->
          <div class="title">
            <div class="title-text">{{ active1Data?.centerC?.label || '运维数据' }}</div>
          </div>
          <div class="center-r-content">
            <div class="content-r-item" v-for="(item, index) in active1Data?.centerC?.childs || []" :key="index">
              <div class="label" v-html="item.label + (item.unit ? `/${item.unit}` : '')"></div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="center">
        <div class="center-l">
          <div class="title">
            <div class="title-text">{{ active1Data?.centerL?.label || '' }}</div>
          </div>
          <div class="center-l-content">
            <div class="center-l-content-item" v-for="(item, index) in active1Data?.centerL?.childs || []" :key="index">
              <div class="item-icon" :style="{ 'background-image': 'url(' + projectIcons.jzsgfDetail1Center[index + 2] + ')' }"></div>
              <div class="values">
                <div class="label">{{ item.label }}</div>
                <div class="value">
                  {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="center-c">
          <div class="data-item" v-for="(item, index) of active1Data?.centerC?.childs || []" :key="index">
            <div class="icon"><img :src="projectIcons.fbsgfSocialBenefitVO[index + 3]" alt="" /></div>
            <div class="value">
              <div class="data-title" v-html="item.label"></div>
              <div class="data-value">
                <span>{{ item.value }}</span>
                <span>{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div> -->
        <div class="center-r">
          <div class="title">
            <div class="title-text">{{ active1Data?.centerR?.label || '' }}</div>
          </div>
          <div class="center-r-content">
            <div class="content-r-item" v-for="(item, index) in active1Data?.centerR?.childs || []" :key="index">
              <div class="label" v-html="item.label + (item.unit ? `/${item.unit}` : '')"></div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom">
        <div class="chart">
          <div class="title">
            <div class="title-text">{{ chart1.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart1"></div>
        </div>
        <!-- <div class="chart">
          <div class="title">
            <div class="title-text" :class="{ noBG: !chart2.label }">{{ chart2.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart2"></div>
        </div> -->
        <div class="chart">
          <div class="title">
            <div class="title-text" :class="{ noBG: !chart3.label }">{{ chart3.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart3"></div>
        </div>
      </div>
    </div>
    <div class="active3 data-content" v-if="active == 1">
      <div class="active3-top">
        <div class="active3-top-l">
          <div class="title">
            <div class="title-text">{{ active3Data?.inverterDataStatistics?.label || '' }}</div>
          </div>
          <div class="active3-top-l-content">
            <div class="active3-top-l-content-statistics" v-for="(item, index) in active3Data?.inverterDataStatistics?.childs || []" :key="index">
              <div class="label">{{ item.label || '' }}{{ item.unit || '' }}</div>
              <div class="value">
                <div v-for="(i, _index) in formatNumber(item.value)" :key="_index" :class="{ isNum: i != ',', comma: i == ',' }">{{ i }}</div>
              </div>
            </div>
            <div class="active3-top-l-content-bottom">
              <div class="active3-top-l-item" v-for="(item, index) in active3Data?.inverterData?.childs" :key="index" :style="{ backgroundImage: `url(${projectIcons.inverter[index < 2 ? 1 : 2]})` }">
                <div class="label">{{ item.label || '' }}</div>
                <div class="value">
                  {{ item.value || '' }} <span class="unit" v-if="item.unit"> {{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="active3-top-r">
          <div class="title">
            <div class="title-text">{{ active3Data?.chartList[0]?.label || '' }}</div>
          </div>
          <div class="chart-content" ref="active3Chart"></div>
        </div>
      </div>
      <!-- <div class="active3-bottom">
        <div class="title-text">{{ '集电线路' }}</div>
        <div class="data-jzsgf"></div>
      </div> -->
      <div class="active3-bottom">
        <div class="title"></div>
        <div class="select">
          <div class="title-text">{{ '逆变器实时数据' }}</div>
          <div class="query">
            <div class="drop" @click.stop="dropSelectShow = true">
              {{ pageForm.submatrix || '' }}
              <i class="el-icon-caret-bottom"></i>
              <div class="drop-box" v-if="dropSelectShow">
                <div class="drop-item" v-for="item in submatrixList" :key="item" @click.stop="handleQuery(item)">{{ item }}</div>
              </div>
            </div>
          </div>
        </div>
        <table>
          <tr v-for="(item, index) in active3Data?.inverterCurrentData?.list || []" :key="index">
            <td v-for="(item1, index1) in item" :key="index1" :class="{ error: item1.status == 1, success: item1.status == 0 }">{{ item1.value }}</td>
          </tr>
        </table>
      </div>
    </div>
    <div class="active2" key="active2" v-if="active == 2">
      <div class="active2-top-statistic">
        <div class="statistic-item" v-for="(item, index) in active2Data?.dataStatistics?.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.inverterDataStatistics[index + 1]})` }">
          <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
          <div class="value">{{ item.value }}</div>
        </div>
      </div>
      <div class="active2-center">
        <div class="active2-center-l">
          <div class="title">
            <div class="title-text">{{ chart4.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart4"></div>
        </div>
        <div class="active2-center-r">
          <div class="title">
            <div class="title-text">{{ chart5.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart5"></div>
        </div>
      </div>
      <div class="active2-bottom">
        <div class="active2-bottom-l">
          <div class="select-title">
            <div class="title-text">
              <span class="label">
                {{ chart6.label || '' }}
              </span>
            </div>
            <div class="query">
              <div class="item" v-for="(item, index) in selectList" :key="index" @click="handleCheck(item)">
                <div class="icon" :class="{ chartActive: item.value === chartActive }"></div>
                <span>{{ item.label }}</span>
              </div>
            </div>
          </div>
          <!-- <div class="title">
            <div class="title-text">{{  }}</div>
          </div> -->
          <div class="chart-content" ref="chart6"></div>
        </div>
        <div class="active2-bottom-r">
          <div class="title">
            <div class="title-text">{{ chart7.label || '' }}</div>
          </div>
          <div class="chart-content" ref="chart7"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Tabbar from '../components/Tabbar'
import icons from '@/utils/icons.js'
import { burstingGlobalView, burstingDataStatistics, listSubmatrix, bpvInverterMonitoring } from '@/api/jzsgf.js'
export default {
  name: 'jzsgf',
  data() {
    return {
      tabbarList: [
        {
          label: '全局总览',
        },
        { label: '逆变器监测' },
        // { label: '数据统计' },
      ],
      active: 0,
      chartActive: 1,
      selectList: [
        {
          label: '日预测',
          value: 1,
        },
        {
          label: '周预测',
          value: 2,
        },
        {
          label: '月预测',
          value: 3,
        },
      ],
      projectIcons: icons,
      active1Data: {
        topLT: {},
        topLB: {},
        topC: {},
        topR: {},
        centerL: {},
        centerR: {},
        centerC: {},
      },
      active2Data: {
        dataStatistics: {},
      },
      active3Data: {},
      chart1: {},
      chart2: {},
      chart3: {},
      chart4: {},
      chart5: {},
      chart6: {},
      chart7: {},
      times: null,

      submatrixList: [],

      dropSelectShow: false,

      pageForm: {
        submatrix: null,
      },
    }
  },
  components: {
    Tabbar,
  },
  computed: {
    formatNumber() {
      return (num) => {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      }
    },
  },
  watch: {
    active: {
      handler(val) {
        if (val == 0) {
          this.initTab1Chart()
        }
        if (val == 1) {
          this.$nextTick(() => {
            this.active3Data.chartList[0].childs[0].style = '0'
            this.active3Data.chartList[0].childs[0].color = '50, 195, 104'
            let chart8Data = {
              data: this.active3Data.chartList[0],
            }
            let chart8 = this.$echarts.init(this.$refs.active3Chart)
            chart8.setOption(this.$initChart(chart8Data))
          })
        }
        if (val == 2) {
          this.$nextTick(() => {
            this.chart4Dom = this.$echarts.init(this.$refs.chart4)
            this.chart5Dom = this.$echarts.init(this.$refs.chart5)
            this.chart6Dom = this.$echarts.init(this.$refs.chart6)
            this.chart7Dom = this.$echarts.init(this.$refs.chart7)
            this.initTab2Chart()
          })
        }
      },
    },
    chartActive() {
      burstingDataStatistics({ type: this.chartActive }).then((res) => {
        if (res) {
          let data = res.data
          this.active2Data = {
            dataStatistics: data.topInfo,
          }
          this.chart4 = data.chartList[0]
          this.chart5 = data.chartList[3]
          this.chart6 = data.chartList[2]
          this.chart7 = data.chartList[1]
          this.initTab2Chart()
        }
      })
    },
  },
  created() {
    // 全局总览
    burstingGlobalView({}).then((res) => {
      if (res) {
        let data = res.data
        this.active1Data = {
          topLT: {
            label: '数据一览',
            childs: [data.fdData, data.syData],
          }, // 数据一览
          topR: data.inverterData,
          topC: data.failureData,
          centerL: data.envData,
          centerC: data.devOpsData,
          centerR: data.socialBenefit,
          topLB: data.otherData,
        }
        this.chart1 = data.chartList[0]
        // this.chart2 = data.chartList[1]
        this.chart3 = data.chartList[2]
        this.initTab1Chart()
      }
    })
    // 后台请求数据 - 逆变器监测
    listSubmatrix()
      .then((res) => {
        this.submatrixList = res.data || []
        if (this.submatrixList.length > 0) {
          this.pageForm.submatrix = this.submatrixList[0]
        }
        this.initInverterMonitoring()
      })
      .catch((err) => {
        this.submatrixList = []
      })
    // 数据统计
    burstingDataStatistics({ type: this.chartActive }).then((res) => {
      if (res) {
        let data = res.data
        this.active2Data = {
          dataStatistics: data.topInfo,
        }
        this.chart4 = data.chartList[0]
        this.chart5 = data.chartList[3]
        this.chart6 = data.chartList[2]
        this.chart7 = data.chartList[1]
      }
    })
  },
  mounted() {
    document.addEventListener('click', () => {
      this.dropSelectShow = false
    })
    if (this.times) {
      return
    }
    this.times = setInterval(() => {
      bpvInverterMonitoring(this.pageForm).then((res) => {
        if (res) {
          let backData = res.data
          this.active3Data.inverterCurrentData = {
            list: backData.realTimeData,
          }
          console.log(this.active3Data.inverterCurrentData)
        }
      })
    }, 30000)
  },
  methods: {
    handleQuery(item) {
      this.pageForm.submatrix = item
      this.dropSelectShow = false
      this.initInverterMonitoring()
    },
    initInverterMonitoring() {
      bpvInverterMonitoring(this.pageForm).then((res) => {
        if (res) {
          let backData = res.data
          this.active3Data = {
            chartList: backData.chartList,
            inverterDataStatistics: backData.inverterDataStatistics,
            inverterData: backData.otherStatistics,
            inverterCurrentData: {
              list: backData.realTimeData,
            },
          }
          console.log(this.active3Data.inverterCurrentData)
        }
      })
    },
    handleCheck(obj) {
      this.chartActive = obj.value
    },
    initTab2Chart() {
      this.$nextTick(() => {
        this.chart4.childs[0].style = '1'
        this.chart4.childs[0].color = '126, 184, 255'
        this.chart4.childs[1].style = '0'
        this.chart4.childs[1].color = '0, 255, 255'
        let chart4Data = {
          data: this.chart4,
          vm: this,
          needStart: true,
          smooth: false,
          needShadow: false,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Data))

        this.chart5.childs[0].style = '1'
        this.chart5.childs[0].color = '206, 173, 103'
        let chart5Data = {
          data: this.chart5,
          vm: this,
          needStart: true,
          needLegend: false,
        }
        this.chart5Dom.setOption(this.$initChart(chart5Data))

        this.chart6.childs[0].style = '0'
        this.chart6.childs[0].color = '126, 184, 255'
        this.chart6.childs[1].style = '0'
        this.chart6.childs[1].color = '224, 172, 101'
        let chart6Data = {
          data: this.chart6,
          needStart: true,
        }
        this.chart6Dom.setOption(this.$initChart(chart6Data))

        this.chart7.childs[0].style = '0'
        this.chart7.childs[0].color = '29, 210, 255'
        let chart7Data = {
          data: this.chart7,
          needLegend: false,
        }
        this.chart7Dom.setOption(this.$initChart(chart7Data))
      })
    },
    initTab1Chart() {
      this.$nextTick(() => {
        this.chart1.childs[0].style = '0'
        this.chart1.childs[0].color = '50, 195, 104'
        let chart1Data = {
          data: this.chart1,
        }
        let chart1 = this.$echarts.init(this.$refs.chart1)
        chart1.setOption(this.$initChart(chart1Data))

        // this.chart2.childs[0].style = '0'
        // this.chart2.childs[0].color = '29, 210, 255'
        // let chart2Data = {
        //   data: this.chart2,
        // }
        // let chart2 = this.$echarts.init(this.$refs.chart2)
        // chart2.setOption(this.$initChart(chart2Data))

        this.chart3.childs[0].style = '1'
        this.chart3.childs[0].color = '126, 184, 255'
        let chart3Data = {
          data: this.chart3,
          vm: this,
          needStart: false,
        }
        let chart3 = this.$echarts.init(this.$refs.chart3)
        chart3.setOption(this.$initChart(chart3Data))
      })
    },
  },
  beforeDestroy() {
    clearInterval(this.times)
  },
}
</script>
<style lang="scss" scoped>
.jzsgf-active1 {
  background-size: 1750px 1137px;
  background-image: url('~@/assets/img/jzsgf/bg_sp_os01.png');
  background-repeat: no-repeat;
  background-position: center -82px;
}
.jzsgf {
  height: 100%;
  overflow-y: auto;
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
  .noBG {
    background: none;
  }
  .active3 {
    margin-top: 20px;
    .active3-bottom {
      margin-top: 40px;
      .data-jzsgf {
        width: 2301px;
        height: 659px;
        margin-top: 20px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/jzsgf/data.png');
      }
      .select {
        position: relative;
        .query {
          position: absolute;
          right: 0;
          top: 0;
          .drop {
            position: relative;
            height: 70px;
            width: 326px;
            padding: 0 30px;
            line-height: 70px;
            font-size: 22px;
            color: #bbe5fd;
            cursor: pointer;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/common/bg_allocation02.png');
            i {
              position: absolute;
              right: 10px;
              top: 20px;
            }
            .drop-box {
              position: absolute;
              height: 400px;
              width: 320px;
              left: 6px;
              top: 70px;
              overflow-y: auto;
              background-color: rgba($color: #0f1120, $alpha: 0.8);
              box-shadow: #0f1120 0px 0px 10px;
              .drop-item {
                padding: 0 24px;
                width: 320px;
                color: #bbe5fd;
                font-size: 22px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                &:hover {
                  background-color: rgba($color: #333333, $alpha: 0.5);
                }
              }
            }
          }
        }
      }
      table {
        margin-top: 30px;
        border-collapse: collapse;
        table-layout: fixed;
        tr {
          background-color: rgba($color: #094d89, $alpha: 0.15);
          border-top: 2px solid transparent;
          td {
            height: 81px;
            color: #a1b8cd;
            font-size: 24px;
            text-align: center;
            &:first-child {
              color: #c9d4db;
              width: 430px;
            }
          }
          &:first-child {
            background: #05192b;
            td {
              color: #c9d4db;
            }
          }
          .error {
            color: #d94545;
          }
          .success {
            color: #4cdf5d;
          }
        }
      }
    }
    .active3-top {
      display: flex;
      justify-content: space-between;
      .active3-top-l {
        width: 904px;
        .active3-top-l-content-bottom {
          display: flex;
          flex-wrap: wrap;
          justify-content: space-between;
          margin-top: 8px;
          padding-left: 20px;
          .active3-top-l-item {
            width: 412px;
            height: 95px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            margin-top: 12px;
            padding-left: 126px;
            padding-top: 13px;
            .label {
              font-size: 18px;
              color: #b4ebff;
            }
            .value {
              font-size: 35px;
              font-weight: bold;
            }
            .unit {
              font-size: 21px;
            }
            &:nth-child(1),
            &:nth-child(2) {
              .value,
              .unit {
                color: #b4ebff;
                background: linear-gradient(180deg, rgba(#fff, 1) 0%, #4bbefa 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                text-shadow: 0 8px 4px rgba(#4bbefa, 0.25);
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
                text-shadow: 0 8px 4px rgba(#03d9b5, 0.25);
              }
            }
          }
        }
        .active3-top-l-content-statistics {
          padding-left: 20px;
          margin-top: 20px;
          .label {
            color: #15a3f2;
            font-size: 22px;
          }
          .value {
            display: flex;
            align-items: center;
            font-size: 55px;
            color: #fff;
            margin-top: 12px;
            .isNum {
              width: 60px;
              height: 80px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-image: url('~@/assets/img/photovoltaic/bg_inverter_digit.png');
              line-height: 80px;
              text-align: center;
              margin-left: 20px;
              &:first-child {
                margin-left: 0;
              }
            }
            .comma + .isNum {
              margin-left: 0;
            }
          }
        }
      }
      .active3-top-r {
        flex: 1;
        .chart-content {
          height: calc(413px - 50px);
        }
      }
    }
  }
  .active1 {
    position: relative;
    // .bg {
    //   //   position: absolute;
    //   //   height: 1137px;
    //   //   width: 1750px;
    //   //   background-image: url('~@/assets/img/jzsgf/bg_sp_os01.png');
    //   //   background-repeat: no-repeat;
    //   //   background-size: 100% 100%;
    //   //   top: -82px;
    //   //   left: 50%;
    //   //   margin-left: -875px;
    // }
    .top {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      .top-l {
        .top-l-content {
          margin-top: 20px;
          padding: 0 18px;
          .content-t {
            display: flex;
            width: 578px;
            justify-content: space-between;
            .content-t-item {
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
                  font-size: 26px;
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
          .content-b {
            margin-top: 26px;
            .content-b-item {
              height: 80px;
              width: 584px;
              line-height: 80px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-image: url('~@/assets/img/jzsgf/bg_sp_os08.png');
              padding-left: 86px;
              display: flex;
              justify-content: space-between;
              margin-top: 14px;
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
      }
      .top-c {
        flex: 1;
        padding: 70px 128px;
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        .top-c-item {
          width: 194px;
          height: 70px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-left: 20px;
          padding-left: 58px;
          padding-top: 10px;
          &:nth-child(4n-3) {
            margin-left: 0;
          }
          .label {
            color: #1ff2ff;
            font-size: 14px;
          }
          .value {
            color: #e8feff;
            font-size: 22px;
            margin-top: 4px;
          }
        }
      }
      .top-r {
        .title-text {
          margin-left: -20px;
        }
        .top-r-content {
          margin-top: 20px;
          width: 557px;
          margin-left: 43px;
          display: flex;
          justify-content: space-between;
          flex-wrap: wrap;
          .top-r-content-item {
            width: 265px;
            height: 132px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/jzsgf/bg_sp_os15.png');
            text-align: center;
            padding-top: 20px;
            .label {
              font-size: 22px;
              color: #00ddff;
            }
            .value {
              font-weight: bold;
              font-size: 32px;
              padding-top: 26px;
              background: linear-gradient(180deg, #fff, #68d9ea);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
        .title-text {
          margin-left: -20px;
        }
        .center-r-content {
          width: 557px;
          margin-left: 43px;
          display: flex;
          flex-wrap: wrap;
          margin-top: 22px;
          gap: 8px 13px;
          .content-r-item {
            height: 80px;
            width: 584px;
            line-height: 80px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/jzsgf/bg_sp_os08.png');
            padding-left: 86px;
            display: flex;
            justify-content: space-between;
            // margin-top: 14px;
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
    }
    .center {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      margin-top: 20px;
      .center-l {
        .center-l-content {
          display: flex;
          flex-wrap: wrap;
          // justify-content: space-between;
          padding: 0 18px;
          width: 708px;
          .center-l-content-item {
            width: 224px;
            display: flex;
            align-items: center;
            margin-top: 24px;
            .item-icon {
              width: 80px;
              height: 80px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              margin-right: 10px;
            }
            .values {
              display: flex;
              flex-direction: column;
              justify-content: space-around;
              .label {
                font-size: 18px;
                color: #fff;
                padding: 2px 12px;
                border-left: 3px solid #80c5f0;
                min-width: 126px;
                background: linear-gradient(to right, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0) 100%);
              }
              .value {
                font-size: 28px;
                color: #86d9ff;
                font-weight: bold;
                .unit {
                  color: #86d9ff;
                  font-size: 17px;
                  font-weight: 400;
                }
              }
            }
          }
        }
      }
      .center-c {
        display: flex;
        justify-content: center;
        padding: 22px 0;
        margin-top: 150px;
        background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
        border: 1px solid;
        border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
        border-image-slice: 1;
        .data-item {
          display: flex;
          align-items: center;
          margin-left: 60px;
          .icon {
            height: 94px;
            width: 94px;
            margin-right: 12px;
            img {
              height: 94px;
              width: 94px;
            }
          }
          .value {
            .data-title {
              color: #fff;
              font-size: 24px;
              background: linear-gradient(90deg, rgba(#286eab, 1), rgba(#286eab, 0));
              padding: 0 15px;
              border-left: 4px solid #80c5f0;
            }
            .data-value {
              color: #86d9ff;
              span {
                &:nth-child(1) {
                  font-size: 38px;
                  font-weight: bold;
                }
                &:nth-child(2) {
                  font-size: 18px;
                }
              }
            }
          }
        }
      }
      .center-r {
        .title-text {
          margin-left: -20px;
        }
        .center-r-content {
          width: 557px;
          margin-left: 43px;
          display: flex;
          flex-wrap: wrap;
          margin-top: 22px;
          gap: 8px 13px;
          .content-r-item {
            height: 80px;
            width: 584px;
            line-height: 80px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/jzsgf/bg_sp_os08.png');
            padding-left: 86px;
            display: flex;
            justify-content: space-between;
            // margin-top: 14px;
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
    }
    .bottom {
      margin-top: 18px;
      display: flex;
      .chart {
        flex: 1;
        height: 340px;
        .chart-content {
          height: 287px;
        }
      }
    }
  }
  .active2 {
    margin-top: 20px;
    .active2-top-statistic {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .statistic-item {
        height: 168px;
        width: 430px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding-left: 193px;
        padding-top: 40px;
        .label {
          color: #cde3f6;
          font-size: 20px;
        }
        .value {
          font-size: 40px;
          color: #f4fdff;
          font-family: douyv;
        }
      }
    }
    .active2-center {
      display: flex;
      margin-top: 40px;
      gap: 0 20px;
      & > div {
        flex: 1;
      }
      .chart-content {
        height: 460px;
      }
    }
    .active2-bottom {
      display: flex;
      margin-top: 40px;
      gap: 0 20px;
      & > div {
        flex: 1;
      }
      .select-title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        .query {
          display: flex;
          align-items: center;
          padding-right: 100px;
          .item {
            display: flex;
            align-items: center;
            font-size: 20px;
            color: #cde3f6;
            margin-left: 20px;
            .icon {
              height: 34px;
              width: 34px;
              background-size: cover;
              background-repeat: no-repeat;
              background-image: url('~@/assets/img/jzsgf/icon_select02.png');
            }
            .chartActive {
              background-image: url('~@/assets/img/jzsgf/icon_select01.png');
            }
          }
        }
      }
      .chart-content {
        height: 460px;
      }
    }
  }
}
</style>
