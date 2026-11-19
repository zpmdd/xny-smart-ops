<template>
  <div class="photovoltaic project-data-page">
    <Tabbar :tabbar="tabbarList" :active.sync="active"></Tabbar>
    <div class="active1" v-if="active == 0">
      <div class="tocdxm" @click="handlePath(1)" v-if="roles.includes('10400')">充电项目</div>
      <div class="tocnxm" @click="handlePath(2)" v-if="roles.includes('10300')">储能项目</div>
      <div class="power" :class="'power' + index" v-for="(item, index) in pageData.deviceList || []" :key="index">
        <div class="power-icon"></div>
        <div class="deviceName">{{ pageData.deviceList.find((i) => i.place == index + 1)?.deviceName || '' }}</div>
        <div class="values">
          <div v-for="(item1, index1) in item.otherData?.childs || []" :key="index1">
            <span class="label">{{ item1.label }}</span>
            <span class="value">{{ item1.value }}</span>
          </div>
          <div>
            <span class="label" v-html="item.socialBenefit?.childs[0]?.label || ''"></span>
            <span class="value">{{ item.socialBenefit?.childs[0]?.value }}</span>
          </div>
        </div>
      </div>
      <div class="popup-control" @click="handlePopup" v-if="active == '0'"></div>
      <div class="country-data">
        <div class="fbsgf-data-content">
          <div class="data-item" v-for="(item, index) of pageData.socialBenefitVO" :key="index">
            <div class="icon"><img :src="projectIcons.fbsgfSocialBenefitVO[index + 1]" alt="" /></div>
            <div class="value">
              <div class="data-title" v-html="item.label"></div>
              <div class="data-value">
                <span>{{ item.value }}</span>
                <span>{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="fbsgf-data-content">
          <div class="data-item" v-for="(item, index) of pageData.devOpsData" :key="index">
            <div class="icon"><img :src="projectIcons.fbsgfSocialBenefitVO[index + 3]" alt="" /></div>
            <div class="value">
              <div class="data-title" v-html="item.label"></div>
              <div class="data-value">
                <span>{{ item.value }}</span>
                <span>{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="popup" v-if="drawer">
        <div class="close"><i class="el-icon-close" @click="drawer = false"></i></div>
        <div class="popup-item">
          <div class="title">
            <div class="title-text">{{ pageData?.photovoltaicDataL.label || '' }}</div>
          </div>
          <div class="popup-item-content">
            <div class="popup-item-content-tl">
              <div class="tl-item" v-for="(item, index) in pageData?.photovoltaicDataL.childs || []" :key="index">
                <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
            <div class="popup-item-content-tr">
              <div class="tr-item" v-for="(item, index) in pageData?.photovoltaicDataR.childs || []" :key="index">
                <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
                <div class="value">{{ item.value }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="popup-item">
          <div class="title">
            <div class="title-text">{{ pageData?.environment.label || '' }}</div>
          </div>
          <div class="environment">
            <div class="environment-item" v-for="(item, index) in pageData?.environment.childs" :key="index">
              <div class="icon" :style="{ backgroundImage: 'url(' + projectIcons.environment[index + 2] + ')' }"></div>
              <div class="value">
                <div class="title">{{ item.label }}</div>
                <div class="val">
                  {{ item.value }}
                  <span class="unit" v-if="item.unit"> {{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="popup-item">
          <div class="active1chartContent">
            <div v-for="(item, index) in pageData.chartList" :key="index" v-show="index <= 1">
              <div class="title">
                <div class="title-text" :class="{ noBG: !item.label }">{{ item.label || '' }}</div>
              </div>
              <div class="active1chart" :class="'active1chart' + (index + 1)"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="active2 data-content" v-if="active == 1">
      <div class="architecture">
        <div class="horizontal"></div>
        <div class="perpendicular" v-for="item in 10" :key="item"></div>
      </div>
      <div class="architecture-data">
        <div class="architecture-data-item">
          <div v-for="(item, index) in active2Data?.inverter?.list || []" :key="index">
            <div v-for="(i, _index) in item" :key="_index">{{ i.value }}</div>
          </div>
        </div>
        <div class="architecture-status">
          <div class="label">储能蓄电池</div>
          <div class="value">
            <div><div></div></div>
            {{ active2Data.storageStatus || '' }}
          </div>
        </div>
      </div>
      <div class="active2-bottom">
        <div class="active2-bottom-l">
          <div class="title">
            <div class="title-text">{{ architecture?.architectureL?.label || '' }}</div>
          </div>
          <div class="values">
            <div class="values-item" v-for="(item, index) in architecture?.architectureL?.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="active2-bottom-r">
          <div class="title">
            <div class="title-text">{{ architecture?.architectureR?.label || '' }}</div>
          </div>
          <div class="values">
            <div class="values-item" v-for="(item, index) in architecture?.architectureR?.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="active3 data-content" v-if="active == 2">
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
      <div class="active3-bottom">
        <div class="title">
          <div class="title-text">{{ '逆变器实时数据' }}</div>
        </div>
        <table>
          <tr v-for="(item, index) in active3Data?.inverterCurrentData?.list || []" :key="index">
            <td v-for="(item1, index1) in item" :key="index1" :class="{ error: item1.status == 1, success: item1.status == 0 }">{{ item1.value }}</td>
          </tr>
        </table>
      </div>
    </div>
    <div class="active4 data-content" v-if="active == 3">
      <div class="active4-top-statistic">
        <div class="statistic-item" v-for="(item, index) in active4Data?.dataStatistics?.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.inverterDataStatistics[index + 1]})` }">
          <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
          <div class="value">{{ item.value }}</div>
        </div>
      </div>
      <div class="active4-chart1">
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[0]?.label || '' }}</div>
          </div>
          <div class="cityGreenLand-charts charts-content" id="cityGreenLand-charts"></div>
        </div>
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[1]?.label || '' }}</div>
          </div>
          <div class="charts-content" ref="active4chart2"></div>
        </div>
      </div>
      <div class="active4-chart2">
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[2]?.label || '' }}</div>
          </div>
          <div class="charts-content" ref="active4chart3"></div>
        </div>
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[3]?.label || '' }}</div>
          </div>
          <div class="charts-content" ref="active4chart4"></div>
        </div>
      </div>
      <div class="active4-chart3">
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[4]?.label || '' }}</div>
            <div class="query">
              <div class="item" v-for="(item, index) in selectList" :key="index" @click="handleCheck(item)">
                <div class="icon" :class="{ chartActive: item.value === chartActive }"></div>
                <span>{{ item.label }}</span>
              </div>
            </div>
          </div>
          <div class="charts-content" ref="active4chart5"></div>
        </div>
        <div>
          <div class="title">
            <div class="title-text">{{ active4Data?.chartList[5]?.label || '' }}</div>
          </div>
          <div class="charts-content" ref="active4chart6"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Header from '@/components/Header'
import Tabbar from '../components/Tabbar'
import icons from '@/utils/icons.js'
import { pvGlobalOverview, systemArchitecture, inverterMonitoring, dataStatistics } from '@/api/fbsgf.js'
import { getPermsTree } from '@/api/user'
import { mapState } from 'vuex'
export default {
  name: 'fbsgf',
  data() {
    return {
      tabbarList: [
        {
          label: '全局总览',
        },
        { label: '系统架构图' },
        { label: '逆变器监测' },
        // { label: '数据统计' },
      ],
      active: 0,
      drawer: false,

      projectIcons: icons,
      pageData: {
        socialBenefitVO: {},
        photovoltaicDataL: {},
        devOpsData: {},
      },
      architecture: {
        architectureL: {},
        architectureR: {},
      },
      active2Data: {},
      active4Data: {
        dataStatistics: {},
      },
      optionData: [],
      times: null,
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
      chartActive: 1,
      roles: [],
    }
  },
  watch: {
    active(val) {
      this.$store.commit('status/fbsgfBG', this.active == 0)
      if (val == 2) {
        this.$nextTick(() => {
          this.active3Data.chartList[0].childs[0].style = '0'
          this.active3Data.chartList[0].childs[0].color = '50, 195, 104'
          let chart1Data = {
            data: this.active3Data.chartList[0],
          }
          let chart1 = this.$echarts.init(this.$refs.active3Chart)
          chart1.setOption(this.$initChart(chart1Data))
        })
      }
      if (val == 3) {
        this.$nextTick(() => {
          this.active4Data.chartList[1].childs[0].style = '0'
          this.active4Data.chartList[1].childs[0].color = '50, 195, 104'
          let chart2Data = {
            data: this.active4Data.chartList[1],
            needLegend: false,
          }

          this.active4Data.chartList[2].childs[0].style = '1'
          this.active4Data.chartList[2].childs[0].color = '50, 195, 104'
          this.active4Data.chartList[2].childs[1].style = '0'
          this.active4Data.chartList[2].childs[1].color = '0, 255, 255'
          let chart3Data = {
            data: this.active4Data.chartList[2],
            needStart: true,
            needShadow: false,
            smooth: false,
            vm: this,
          }

          this.active4Data.chartList[3].childs[0].style = '0'
          this.active4Data.chartList[3].childs[0].color = '29, 210, 255'
          let chart4Data = {
            data: this.active4Data.chartList[3],
            needLegend: false,
          }

          this.active4Data.chartList[4].childs[0].style = '0'
          this.active4Data.chartList[4].childs[0].color = '121, 161, 254'
          this.active4Data.chartList[4].childs[1].style = '0'
          this.active4Data.chartList[4].childs[1].color = '239, 182, 101'
          let chart5Data = {
            data: this.active4Data.chartList[4],
            smooth: false,
            needStart: true,
          }

          this.active4Data.chartList[5].childs[0].style = '1'
          this.active4Data.chartList[5].childs[0].color = '239, 182, 101'
          let chart6Data = {
            data: this.active4Data.chartList[5],
            needLegend: false,
            needStart: true,
            vm: this,
          }

          let chart2 = this.$echarts.init(this.$refs.active4chart2)
          chart2.setOption(this.$initChart(chart2Data))
          let chart3 = this.$echarts.init(this.$refs.active4chart3)
          chart3.setOption(this.$initChart(chart3Data))
          let chart4 = this.$echarts.init(this.$refs.active4chart4)
          chart4.setOption(this.$initChart(chart4Data))
          let chart5 = this.$echarts.init(this.$refs.active4chart5)
          chart5.setOption(this.$initChart(chart5Data))
          let chart6 = this.$echarts.init(this.$refs.active4chart6)
          chart6.setOption(this.$initChart(chart6Data))
          this.init()
        })
      }
    },
    drawer() {
      if (this.drawer) {
        this.$nextTick(() => {
          this.pageData.chartList[0].childs[0].style = '0'
          this.pageData.chartList[0].childs[0].color = '50, 195, 104'
          let chart1Data = {
            data: this.pageData.chartList[0],
          }

          this.pageData.chartList[1].childs[0].style = '0'
          this.pageData.chartList[1].childs[0].color = '29, 210, 255'
          let chart2Data = {
            data: this.pageData.chartList[1],
            vm: this,
          }

          this.pageData.chartList[1].childs[0].style = '1'
          this.pageData.chartList[1].childs[0].color = '126, 184, 255'
          // let chart3Data = {
          //   data: this.pageData.chartList[1],
          //   needStart: false,
          //   vm: this,
          // }

          // this.pageData.chartList[3].childs[0].style = '0'
          // this.pageData.chartList[3].childs[0].color = '50, 195, 104'
          // let chart4Data = {
          //   data: this.pageData.chartList[3],
          //   smooth: false,
          // }

          // this.pageData.chartList[4].childs[0].style = '1'
          // this.pageData.chartList[4].childs[0].color = '37, 161, 115'
          // this.pageData.chartList[4].childs[1].style = '1'
          // this.pageData.chartList[4].childs[1].color = '0, 174, 255'
          // let chart5Data = {
          //   data: this.pageData.chartList[4],
          //   needStart: true,
          //   needShadow: false,
          //   smooth: false,
          //   vm: this,
          // }

          let chart1 = this.$echarts.init(document.querySelector('.active1chart1'))
          chart1.setOption(this.$initChart(chart1Data))
          let chart2 = this.$echarts.init(document.querySelector('.active1chart2'))
          chart2.setOption(this.$initChart(chart2Data))
          // let chart3 = this.$echarts.init(document.querySelector('.active1chart3'))
          // chart3.setOption(this.$initChart(chart3Data))
          // let chart4 = this.$echarts.init(document.querySelector('.active1chart4'))
          // chart4.setOption(this.$initChart(chart4Data))
          // let chart5 = this.$echarts.init(document.querySelector('.active1chart5'))
          // chart5.setOption(this.$initChart(chart5Data))
        })
      }
    },
    chartActive() {
      dataStatistics({ type: this.chartActive }).then((res) => {
        if (res) {
          let backData = res.data
          this.active4Data = {
            chartList: backData.chartList,
            dataStatistics: backData.topDataStatistics,
          }
          this.optionData = backData.chartList[0].childs[0].childs.map((item) => {
            let i = {
              name: item.label,
              value: Number(item.value),
            }
            return i
          })
          this.$nextTick(() => {
            this.active4Data.chartList[4].childs[0].style = '0'
            this.active4Data.chartList[4].childs[0].color = '121, 161, 254'
            this.active4Data.chartList[4].childs[1].style = '0'
            this.active4Data.chartList[4].childs[1].color = '239, 182, 101'
            let chart5Data = {
              data: this.active4Data.chartList[4],
              smooth: false,
              needStart: true,
            }
            let chart5 = this.$echarts.init(this.$refs.active4chart5)
            chart5.setOption(this.$initChart(chart5Data))
          })
        }
      })
    },
  },
  computed: {
    ...mapState({
      loginId: (state) => state.user.loginId,
    }),
    formatNumber() {
      return (num) => {
        return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      }
    },
    inverterStatus() {
      return (obj, val) => {
        if (obj.values[0] == '运行状态') {
          if (isNaN(val)) {
            return val
          } else {
            let arr = ['运行异常', '运行正常']
            return arr[val]
          }
        } else {
          return val
        }
      }
    },
  },
  async created() {
    await this.getPermsTree()
    this.type = this.$route.query.type
    this.$store.commit('status/fbsgfBG', true)
    this.initData()
  },
  mounted() {
    if (this.times) {
      return
    }
    this.times = setInterval(() => {
      inverterMonitoring({}).then((res) => {
        if (res) {
          let backData = res.data
          this.active3Data.inverterCurrentData = {
            list: backData.realTimeData,
          }
        }
      })
    }, 30000)
  },
  methods: {
    getPermsTree() {
      return new Promise((resolve, reject) => {
        let params = {
          userId: this.loginId,
          ifExist: true,
        }
        getPermsTree(params)
          .then((res) => {
            const { data } = res
            let arr = []
            formatArr(data)
            function formatArr(list) {
              list.forEach((item) => {
                arr.push(item.id)
                if (item.children && item.children.length) {
                  formatArr(item.children)
                }
              })
            }
            this.roles = arr
            resolve(arr)
          })
          .catch((error) => {
            reject(error)
          })
      })
    },
    handleCheck(obj) {
      this.chartActive = obj.value
    },
    handlePath(val) {
      if (val == 1) {
        this.$router.push({ path: '/detail/cdxm' })
      }
      if (val == 2) {
        this.$router.push({ path: '/detail/cnxm' })
      }
    },
    init() {
      //构建3d饼状图
      let myChart = this.$echarts.init(document.getElementById('cityGreenLand-charts'))
      // 传入数据生成 option
      this.option = this.getPie3D(this.optionData, 0.7) //空心圆的大小
      myChart.setOption(this.option)
      //是否需要label指引线，如果要就添加一个透明的2d饼状图并调整角度使得labelLine和3d的饼状图对齐，并再次setOption
      this.option.series.push({
        name: 'pie2d',
        type: 'pie',
        labelLine: {
          show: false,
        },
        startAngle: -20, //起始角度，支持范围[0, 360]。
        clockwise: false, //饼图的扇区是否是顺时针排布。上述这两项配置主要是为了对齐3d的样式
        radius: ['20%', '50%'],
        center: ['0%', '50%'],
        data: this.optionData,
        itemStyle: {
          opacity: 0,
        },
      })
      myChart.setOption(this.option)
    },
    initData() {
      // 后台请求数据 - 全局总览
      pvGlobalOverview({}).then((res) => {
        if (res) {
          let backData = res.data
          this.pageData = {
            chartList: backData.chartList,
            deviceList: backData.deviceList,
            socialBenefitVO: backData.socialBenefit.childs, // 社会效益
            photovoltaicDataL: backData.dataView, // 数据一览
            photovoltaicDataR: backData.otherStatistics, // 累计数据
            environment: backData.envDataToday, // 今日环境数据
            devOpsData: backData.devOpsData.childs,
          }
        }
      })

      // 后台请求数据 - 系统架构
      systemArchitecture({}).then((res) => {
        if (res) {
          let backData = res.data
          this.architecture = {
            architectureL: backData.inverterDeviceData,
            architectureR: backData.storageDeviceData,
          }
          this.active2Data.inverter = {
            list: backData.realTimeData,
          }
          this.active2Data.storageStatus = backData.storageStatus
        }
      })

      // 后台请求数据 - 逆变器监测
      inverterMonitoring({}).then((res) => {
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
        }
      })

      // 后台请求数据 - 获取数据统计
      dataStatistics({ type: this.chartActive }).then((res) => {
        if (res) {
          let backData = res.data
          this.active4Data = {
            chartList: backData.chartList,
            dataStatistics: backData.topDataStatistics,
          }
          this.optionData = backData.chartList[0].childs[0].childs.map((item) => {
            let i = {
              name: item.label,
              value: Number(item.value),
            }
            return i
          })
        }
      })
    },
    getPie3D(pieData, internalDiameterRatio) {
      //internalDiameterRatio:透明的空心占比
      let that = this
      let series = []
      let sumValue = 0
      let startValue = 0
      let endValue = 0
      let legendData = []
      let legendBfb = []
      let k = 1 - internalDiameterRatio
      pieData.sort((a, b) => {
        return b.value - a.value
      })
      // 为每一个饼图数据，生成一个 series-surface 配置
      for (let i = 0; i < pieData.length; i++) {
        sumValue += pieData[i].value
        let seriesItem = {
          name: typeof pieData[i].name === 'undefined' ? `series${i}` : pieData[i].name,
          type: 'surface',
          parametric: true,
          wireframe: {
            show: false,
          },
          pieData: pieData[i],
          pieStatus: {
            selected: false,
            hovered: false,
            k: k,
          },
          center: ['10%', '50%'],
        }

        if (typeof pieData[i].itemStyle != 'undefined') {
          let itemStyle = {}
          typeof pieData[i].itemStyle.color != 'undefined' ? (itemStyle.color = pieData[i].itemStyle.color) : null
          typeof pieData[i].itemStyle.opacity != 'undefined' ? (itemStyle.opacity = pieData[i].itemStyle.opacity) : null
          seriesItem.itemStyle = itemStyle
        }
        series.push(seriesItem)
      }

      // 使用上一次遍历时，计算出的数据和 sumValue，调用 getParametricEquation 函数，
      // 向每个 series-surface 传入不同的参数方程 series-surface.parametricEquation，也就是实现每一个扇形。
      legendData = []
      legendBfb = []
      for (let i = 0; i < series.length; i++) {
        endValue = startValue + series[i].pieData.value
        series[i].pieData.startRatio = startValue / sumValue
        series[i].pieData.endRatio = endValue / sumValue
        series[i].parametricEquation = this.getParametricEquation(series[i].pieData.startRatio, series[i].pieData.endRatio, false, false, k, series[i].pieData.value)
        startValue = endValue
        let bfb = that.fomatFloat(series[i].pieData.value / sumValue, 4)
        legendData.push({
          name: series[i].name,
          value: bfb,
        })
        legendBfb.push({
          name: series[i].name,
          value: bfb,
        })
      }
      let boxHeight = this.getHeight3D(series, 26) //通过传参设定3d饼/环的高度，26代表26px
      // 准备待返回的配置项，把准备好的 legendData、series 传入。
      let option = {
        legend: {
          data: legendData,
          orient: 'horizontal',
          right: this.$formatSize(40),
          top: this.$formatSize(10),
          itemGap: this.$formatSize(10),
          textStyle: {
            color: '#A1E2FF',
            fontSize: this.$formatSize(18),
            fontFamily: 'wryh',
          },
          // type: 'scroll',
          itemWidth: this.$formatSize(25),
          itemHeight: this.$formatSize(14),
          orient: 'vertical',
          show: true,
          formatter: function (param) {
            let item = legendBfb.filter((item) => item.name == param)[0]
            let bfs = that.fomatFloat(item.value * 100, 2) + '%'
            return `${item.name}  ${bfs}`
          },
        },
        labelLine: {
          show: true,
          lineStyle: {
            color: '#7BC0CB',
          },
        },
        label: {
          show: true,
          position: 'outside',
          rich: {
            b: {
              color: '#7BC0CB',
              fontSize: this.$formatSize(12),
              lineHeight: this.$formatSize(20),
            },
            c: {
              fontSize: this.$formatSize(16),
            },
          },
          formatter: '{b|{b} \n}{c|{c}}{b|  亩}',
        },
        tooltip: {
          fontSize: this.$formatSize(16),
          fontFamily: 'wryh',
          formatter: (params) => {
            if (params.seriesName !== 'mouseoutSeries' && params.seriesName !== 'pie2d') {
              let bfb = ((option.series[params.seriesIndex].pieData.endRatio - option.series[params.seriesIndex].pieData.startRatio) * 100).toFixed(2)
              return `${params.seriesName}<br/>` + `<span style="display:inline-block;margin-right:5px;border-radius:10px;width:10px;height:10px;background-color:${params.color};"></span>` + `${bfb}%`
            }
          },
        },
        xAxis3D: {
          min: -1,
          max: 1,
        },
        yAxis3D: {
          min: -1,
          max: 1,
        },
        zAxis3D: {
          min: -1,
          max: 1,
        },
        grid3D: {
          show: false,
          boxHeight: boxHeight, //圆环的高度
          left: -this.$formatSize(200),
          viewControl: {
            //3d效果可以放大、旋转等，请自己去查看官方配置
            alpha: 20, //角度
            distance: 140, //调整视角到主体的距离，类似调整zoom
            rotateSensitivity: 0, //设置为0无法旋转
            zoomSensitivity: 0, //设置为0无法缩放
            panSensitivity: 0, //设置为0无法平移
            autoRotate: true, //自动旋转
          },
        },
        series: series,
      }
      return option
    },

    //获取3d丙图的最高扇区的高度
    getHeight3D(series, height) {
      series.sort((a, b) => {
        return b.pieData.value - a.pieData.value
      })
      return (height * 25) / series[0].pieData.value
    },

    // 生成扇形的曲面参数方程，用于 series-surface.parametricEquation
    getParametricEquation(startRatio, endRatio, isSelected, isHovered, k, h) {
      // 计算
      let midRatio = (startRatio + endRatio) / 2
      let startRadian = startRatio * Math.PI * 2
      let endRadian = endRatio * Math.PI * 2
      let midRadian = midRatio * Math.PI * 2
      // 如果只有一个扇形，则不实现选中效果。
      if (startRatio === 0 && endRatio === 1) {
        isSelected = false
      }
      // 通过扇形内径/外径的值，换算出辅助参数 k（默认值 1/3）
      k = typeof k !== 'undefined' ? k : 1 / 3
      // 计算选中效果分别在 x 轴、y 轴方向上的位移（未选中，则位移均为 0）
      let offsetX = isSelected ? Math.cos(midRadian) * 0.1 : 0
      let offsetY = isSelected ? Math.sin(midRadian) * 0.1 : 0
      // 计算高亮效果的放大比例（未高亮，则比例为 1）
      let hoverRate = isHovered ? 1.05 : 1
      // 返回曲面参数方程
      return {
        u: {
          min: -Math.PI,
          max: Math.PI * 3,
          step: Math.PI / 32,
        },
        v: {
          min: 0,
          max: Math.PI * 2,
          step: Math.PI / 20,
        },
        x: function (u, v) {
          if (u < startRadian) {
            return offsetX + Math.cos(startRadian) * (1 + Math.cos(v) * k) * hoverRate
          }
          if (u > endRadian) {
            return offsetX + Math.cos(endRadian) * (1 + Math.cos(v) * k) * hoverRate
          }
          return offsetX + Math.cos(u) * (1 + Math.cos(v) * k) * hoverRate
        },
        y: function (u, v) {
          if (u < startRadian) {
            return offsetY + Math.sin(startRadian) * (1 + Math.cos(v) * k) * hoverRate
          }
          if (u > endRadian) {
            return offsetY + Math.sin(endRadian) * (1 + Math.cos(v) * k) * hoverRate
          }
          return offsetY + Math.sin(u) * (1 + Math.cos(v) * k) * hoverRate
        },
        z: function (u, v) {
          if (u < -Math.PI * 0.5) {
            return Math.sin(u)
          }
          if (u > Math.PI * 2.5) {
            return Math.sin(u) * h * 0.1
          }
          return Math.sin(v) > 0 ? 1 * h * 0.1 : -1
        },
      }
    },

    fomatFloat(num, n) {
      var f = parseFloat(num)
      if (isNaN(f)) {
        return false
      }
      f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n) // n 幂
      var s = f.toString()
      var rs = s.indexOf('.')
      //判定如果是整数，增加小数点再补0
      if (rs < 0) {
        rs = s.length
        s += '.'
      }
      while (s.length <= rs + n) {
        s += '0'
      }
      return s
    },

    handlePopup() {
      // this.popupShow = true
      this.drawer = true
    },
  },
  components: {
    Header,
    Tabbar,
  },
  beforeDestroy() {
    clearInterval(this.times)
    this.$store.commit('status/fbsgfBG', false)
  },
}
</script>
<style lang="scss" scoped>
@keyframes horizontal {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(1960px);
  }
}
@keyframes perpendicular {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-100px);
  }
}
@keyframes perpendicular1 {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-80px);
  }
}
.photovoltaic {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
  .title-text {
    color: #fff;
    font-size: 24px;
    line-height: 50px;
    height: 50px;
    width: 600px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/data/bg_title.png');
    font-family: douyv;
    padding-left: 122px;
  }
  .active1 {
    // position:relative
    .tocdxm,
    .tocnxm {
      position: absolute;
      z-index: 100;
      height: 206px;
      width: 137px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      color: #fff;
      font-size: 18px;
      text-align: center;
      font-weight: bold;
      padding-top: 80px;
    }
    .tocdxm {
      background-image: url('~@/assets/img/fbsgf/icon_cicic.png');
      left: 1448px;
      top: 334px;
    }
    .tocnxm {
      background-image: url('~@/assets/img/fbsgf/icon_cicic2.png');
      left: 1710px;
      top: 450px;
    }
    .power {
      position: absolute;
      min-height: 110px;
      width: 272px;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/fbsgf/ipadicon_power0022.png');
      display: flex;
      // align-items: flex-start;
      color: #eff0f1;
      padding: 15px;
      font-size: 18px;
      &::after {
        content: '';
        position: absolute;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        width: 93px;
        height: 71px;
        background-image: url('~@/assets/img/fbsgf/ipadicon_power0024.png');
        right: -70px;
        top: 35px;
      }
      .power-icon {
        width: 44px;
        height: 67px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        position: absolute;
        top: 20px;
        left: 35px;
        background-image: url('~@/assets/img/fbsgf/inverter.png');
      }
      .deviceName {
        padding-top: 80px;
        width: 85px;
        text-align: center;
      }
      .values {
        // height: 100%;
        margin-left: 10px;
        width: 150px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        word-break: break-all;
      }
    }
    .power0 {
      top: 160px;
      left: 500px;
    }
    .power1 {
      top: 80px;
      left: 1160px;
      &::after {
        width: 142px;
        height: 70px;
        background-image: url('~@/assets/img/fbsgf/ipadicon_power0023.png');
        right: 253px;
        top: 44px;
      }
      justify-content: flex-end;
    }
    .power2 {
      top: 720px;
      left: 1240px;
      justify-content: flex-end;
      &::after {
        width: 142px;
        height: 70px;
        background-image: url('~@/assets/img/fbsgf/ipadicon_power0023.png');
        right: 253px;
        top: 44px;
      }
    }
    .power3 {
      top: 580px;
      left: 1040px;
      justify-content: flex-end;
      &::after {
        width: 142px;
        height: 70px;
        background-image: url('~@/assets/img/fbsgf/ipadicon_power0023.png');
        right: 253px;
        top: 44px;
      }
    }
    .power4 {
      top: 830px;
      left: 720px;
    }
    .power5 {
      top: 700px;
      left: 580px;
    }
    .power6 {
      top: 570px;
      left: 380px;
    }
    .power7 {
      top: 330px;
      left: 1800px;
      justify-content: flex-end;
      &::after {
        width: 142px;
        height: 70px;
        background-image: url('~@/assets/img/fbsgf/ipadicon_power0023.png');
        right: 253px;
        top: 44px;
      }
    }
  }
  .active4 {
    margin-top: 20px;
    .active4-top-statistic {
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
    .active4-chart1,
    .active4-chart2,
    .active4-chart3 {
      display: flex;
      .title {
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
      & > div {
        flex: 1;
      }
      .charts-content {
        height: 318px;
      }
    }
    .active4-chart1 {
      margin-top: 40px;
    }
  }
  .active3 {
    margin-top: 20px;
    .active3-bottom {
      margin-top: 40px;
      table {
        margin-top: 30px;
        border-collapse: collapse;
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
  .active2 {
    .active2-bottom {
      margin-top: 20px;
      display: flex;
      // padding: 0 46px;
      .active2-bottom-l {
        width: 687px;
        .values {
          display: flex;
          justify-content: space-between;
          width: 554px;
          flex-wrap: wrap;
          margin-top: 20px;
          .values-item {
            width: 254px;
            height: 120px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/country/popup/icon_pupup01.png');
            margin-top: 28px;
            text-align: center;
            padding-top: 22px;
            &:nth-child(1),
            &:nth-child(2) {
              margin-top: 0;
            }
            .label {
              color: #00ddff;
              font-size: 22px;
            }
            .value {
              font-size: 32px;
              font-weight: bold;
              color: #eef3ff;
              margin-top: 10px;
              background: linear-gradient(180deg, #fff 0%, #68d9ea 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
      }
      .active2-bottom-r {
        flex: 1;
        .values {
          display: flex;
          justify-content: space-between;
          flex-wrap: wrap;
          margin-top: 20px;
          .values-item {
            width: 486px;
            padding-right: 40px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/photovoltaic/bg_gf_data.png');
            margin-top: 10px;
            line-height: 80px;
            padding-left: 87px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .label {
              color: #eef3ff;
              font-size: 21px;
            }
            .value {
              min-width: 160px;
              text-align: center;
              font-size: 36px;
              color: #eef3ff;
              font-weight: bold;
              // color: linear-gradient(0deg, #8FB9F7 0%, rgba(255,255,255,0.87) 100%);
              background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
      }
    }
    .architecture-data {
      color: #00ffff;
      display: flex;
      .architecture-status {
        text-align: center;
        width: 200px;
        .label {
          font-size: 24px;
        }
        .value {
          color: #ffe0df;
          font-size: 22px;
          display: flex;
          align-items: center;
          margin-top: 10px;
          margin-left: 28px;
          justify-content: center;
          & > div {
            width: 21px;
            height: 21px;
            margin-left: -20px;
            background: rgba(255, 0, 0, 0.33);
            border-radius: 50%;
            margin-right: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            & > div {
              width: 9px;
              height: 9px;
              background-color: #ff0000;
              border-radius: 50%;
            }
          }
        }
      }
      .architecture-data-item {
        margin-left: 30px;
        width: 1950px;
        & > div {
          background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
          border-bottom: 1px solid;
          border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
          border-image-slice: 1;
          font-size: 22px;
          display: flex;
          width: 1916px;
          min-height: 60px;
          align-items: center;
          & > div {
            text-align: center;
            min-width: 204px;
            width: 204px;
            min-height: 60px;
            padding: 16px 6px;
            // word-wrap: break-word; /* 在单词内部进行断开 */
            // overflow-wrap: break-word; /* 支持更多语言的断开 */
            &:nth-child(1) {
              min-width: 60px;
              width: 60px;
            }
            &:nth-child(6) {
              min-width: 400px;
              width: 400px;
              padding-right: 186px;
            }
          }
          &:nth-child(1) {
            height: 72px;
            font-size: 24px;
            & > div {
              padding-top: 0;
            }
          }
        }
      }
    }
    .architecture {
      width: 2080px;
      height: 682px;
      margin: 0 auto;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/photovoltaic/architecture.png');
      background-repeat: no-repeat;
      position: relative;
      .horizontal {
        height: 6px;
        width: 36px;
        position: absolute;
        background-color: #ffda0f;
        top: 233px;
        left: 0;
        animation: horizontal 6s linear infinite;
      }
      .perpendicular {
        width: 6px;
        height: 36px;
        position: absolute;
        background-color: #ffda0f;
        top: 476px;
        animation: perpendicular 2s linear infinite;
        &:nth-child(2) {
          left: 71px;
        }
        &:nth-child(3) {
          left: 272px;
        }
        &:nth-child(4) {
          left: 473px;
        }
        &:nth-child(5) {
          left: 673px;
        }
        &:nth-child(6) {
          left: 874px;
        }
        &:nth-child(7) {
          left: 1258px;
        }
        &:nth-child(8) {
          left: 1458px;
        }
        &:nth-child(9) {
          left: 1659px;
        }
        &:nth-child(10) {
          left: 1769px;
          top: 340px;
        }
        &:nth-child(11) {
          left: 1806px;
          top: 320px;
          animation: perpendicular1 2s linear infinite;
        }
      }
    }
  }
  .popup-control {
    width: 270px;
    height: 280px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/country/icon_left_slide.png');
    background-repeat: no-repeat;
    position: absolute;
    right: 0;
    top: 50%;
    margin-top: -140px;
  }
  .popup {
    position: absolute;
    z-index: 99999;
    width: 2328px;
    height: 1358px;
    left: 50%;
    margin-left: -1164px;
    bottom: 50px;
    background-image: url('~@/assets/img/photovoltaic/photovoltaic_bg_bottom.png');
    background-repeat: no-repeat;
    background-size: 100% 100%;
    padding: 54px 60px 0 120px;
    .close {
      position: absolute;
      font-size: 50px;
      right: 37px;
      top: 45px;
      color: #fff;
    }
    .popup-item {
      margin-top: 20px;
      &:first-child {
        margin-top: 0;
      }
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
      .active1chartContent {
        height: 700px;
        display: flex;
        flex-wrap: wrap;
        .noBG {
          background-image: none;
        }
        & > div {
          width: 716px;
          &:last-child {
            width: 1432px;
          }
          .active1chart {
            height: 297px;
            width: 100%;
          }
        }
      }
      .popup-item-content {
        display: flex;
        .popup-item-content-tl {
          width: 1372px;
          display: flex;
          flex-wrap: wrap;
          justify-content: space-between;
          .tl-item {
            width: 643px;
            height: 80px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/photovoltaic/bg_gf_data.png');
            margin-top: 10px;
            line-height: 80px;
            padding-left: 87px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            .label {
              color: #eef3ff;
              font-size: 21px;
            }
            .value {
              width: 310px;
              text-align: center;
              font-size: 36px;
              color: #eef3ff;
              font-weight: bold;
              // color: linear-gradient(0deg, #8FB9F7 0%, rgba(255,255,255,0.87) 100%);
              background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
        .popup-item-content-tr {
          margin-left: 104px;
          width: 554px;
          display: flex;
          justify-content: space-between;
          flex-wrap: wrap;
          .tr-item {
            width: 254px;
            height: 120px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/country/popup/icon_pupup01.png');
            margin-top: 28px;
            text-align: center;
            padding-top: 22px;
            &:nth-child(1),
            &:nth-child(2) {
              margin-top: 0;
            }
            .label {
              color: #00ddff;
              font-size: 22px;
            }
            .value {
              font-size: 32px;
              font-weight: bold;
              color: #eef3ff;
              margin-top: 10px;
              background: linear-gradient(180deg, #fff 0%, #68d9ea 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
      }
      .environment {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 26px 10px 20px 10px;
        .environment-item {
          display: flex;
          align-items: center;
          .icon {
            height: 94px;
            width: 94px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .value {
            margin-left: 16px;
            .title {
              background: linear-gradient(90deg, rgba(#286eab, 1) 0%, rgba(#286eab, 0) 100%);
              padding: 0 15px;
              border-left: 4px solid #80c5f0;
              color: #ffffff;
              font-size: 24px;
              height: 34px;
              line-height: 34px;
              min-width: 152px;
            }
            .val {
              font-size: 38px;
              color: #86d9ff;
              font-weight: bold;
              .unit {
                font-size: 20px;
              }
            }
          }
        }
      }
    }
  }
  .country-data {
    position: absolute;
    width: 100%;
    bottom: 120px;
    left: 0;
    display: flex;
    justify-content: center;
    .fbsgf-data-content {
      display: flex;
      justify-content: center;
      padding: 30px 0;
      background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
      border: 2px solid;
      border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
      border-image-slice: 1;
      &:first-child {
        width: 642px;
      }
      &:last-child {
        width: 1298px;
      }
      ul {
        width: 100%;
        display: flex;
        justify-content: center;
        li {
          width: 386px;
          display: flex;
          align-items: center;
          .icon {
            height: 94px;
            width: 94px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            margin-right: 8px;
          }
          .bias {
            transform: rotate(10deg);
            color: #233847;
            height: 78px;
            width: 0;
            border-right: 2px solid #233847;
            margin-right: 20px;
          }
          .data {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            .value {
              font-size: 40px;
              color: #fff;
              font-family: douyv;
            }
            .label {
              color: #98c3f4;
              font-size: 20px;
            }
          }
        }
      }
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
  }
}
</style>
