<template>
  <div class="cdxm">
    <Tabbar :tabbar="tabbarList" :active.sync="active"></Tabbar>
    <div class="active1" key="active1" v-if="active == 0">
      123456
      <div class="t">
        <div class="l">
          <div class="title">
            <div class="title-text">{{ stationData.label || '' }}</div>
          </div>
          <div class="stationData-content">
            <div class="stationData-item" v-for="(item, index) in stationData.childs || []" :key="index">
              <div class="icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.stationData[index % 2]})` }"></div>
              <div class="value">{{ item.value }}</div>
              <div class="label">{{ item.label }}</div>
            </div>
          </div>
          <!-- <div class="multipleData-content">
            <div class="multipleData-item" v-for="(item, index) in multipleData.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? ` /${item.unit} ` : '' }}</div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div> -->
          <div class="chart-item">
          <div class="title">
            <div class="title-text">{{ chart4Data.label || '' }}</div>
          </div>
          <div class="charts chart-pie" ref="chart14"></div>
          <div class="chart-item-footer">
            <div class="chart-item-footer-t" v-if="spearStatusData.childs">{{ spearStatusData.childs[2]?.label || '' }}{{ spearStatusData.childs[2]?.value }}{{ spearStatusData.childs[2]?.unit || '' }}</div>
            <div class="chart-item-footer-b">
              <div class="chart-item-footer-l" v-if="spearStatusData.childs">
                {{ spearStatusData.childs[0]?.value }}<span class="unit" v-if="spearStatusData.childs[0]?.unit">{{ spearStatusData.childs[0]?.unit || '' }}</span>
              </div>
              <div class="chart-item-footer-r" v-if="spearStatusData.childs">
                {{ spearStatusData.childs[1]?.value }}<span class="unit" v-if="spearStatusData.childs[1]?.unit">{{ spearStatusData.childs[1]?.unit || '' }}</span>
              </div>
            </div>
          </div>
        </div>
        </div>
        <div class="c">
          <div class="inUseData-item" :class="'inUseData-item' + index" v-for="(item, index) in inUseData.childs || []" :key="index" @click.stop="handleTip(item, index)">
            <div class="icon" :class="{ use: item.childs[0]?.value == '使用中', err: item.childs[0]?.value == '故障报警', idle: item.childs[0]?.value == '空闲中' }"></div>
            <div class="label">
              <span>{{ item.label }}</span>
            </div>
            <div class="inUseData-info-popover" v-if="popoverShow && chargeActive === index">
              <div class="status">
                <span>{{ item.childs[0].value }}</span>
                <span><i class="el-icon-close" @click.stop="popoverShow = false"></i></span>
              </div>
              <div class="v-a">
                <div class="a">{{ item.childs[1].label }} {{ item.childs[1].value }}{{ item.childs[1].unit || '' }}</div>
                <div class="v">{{ item.childs[2].label }} {{ item.childs[2].value }}{{ item.childs[2].unit || '' }}</div>
              </div>
              <div class="car"></div>
              <div class="row msg first" v-if="item.childs[3]">
                <div class="row-l">
                  <div class="row-icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.popoverIcon[1]})` }"></div>
                  <div class="row-label">{{ item.childs[3].label }}</div>
                </div>

                <div class="row-r">
                  <div class="row-value">{{ item.childs[3].value }}{{ item.childs[3].unit || '' }}</div>
                </div>
              </div>
              <div class="row msg" v-if="item.childs[4]">
                <div class="row-l">
                  <div class="row-icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.popoverIcon[2]})` }"></div>
                  <div class="row-label">{{ item.childs[4].label }}</div>
                </div>

                <div class="row-r">
                  <div class="row-value">{{ item.childs[4].value }}{{ item.childs[4].unit || '' }}</div>
                </div>
              </div>
              <div class="row msg" v-if="item.childs[5]">
                <div class="row-l">
                  <div class="row-icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.popoverIcon[3]})` }"></div>
                  <div class="row-label">{{ item.childs[5].label }}</div>
                </div>

                <div class="row-r">
                  <div class="row-value">{{ item.childs[5].value }}{{ item.childs[5].unit || '' }}</div>
                </div>
              </div>
            </div>
            <div class="inUseData-err-popover" v-if="item.childs.length && item.childs[0]?.value === '故障报警'">{{ item.childs[0].value }}</div>
          </div>
        </div>
        <div class="r">
          <div class="title">
            <div class="title-text">{{ devOpsData.label || '' }}</div>
          </div>
          <div class="devOpsData-content">
            <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? ` /${item.unit} ` : '' }}</div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div>
          <div class="title">
            <div class="title-text">{{ stationStatusData.label || '' }}</div>
          </div>
          <div class="stationStatusData-content">
            <div class="stationStatusData-item" v-for="(item, index) in stationStatusData.childs" :key="index">
              <div class="stationStatusData-header">
                <div class="label">{{ item.label }}</div>
                <div class="value">{{ item.value }}{{ item.unit || '' }}</div>
              </div>
              <div class="stationStatusData-item-bg">
                <div class="stationStatusData-item-bg-value" :style="{ width: (item.value / 100) * 100 + '%' }"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="b">
        <div class="chart-item">
          <div class="title">
            <div class="title-text">{{ chart1Data.label || '' }}</div>
          </div>
          <div class="charts" ref="chart11"></div>
        </div>
        <div class="chart-item">
          <div class="title">
            <div class="title-text">{{ chart2Data.label || '' }}</div>
          </div>
          <div class="charts" ref="chart12"></div>
        </div>
        <div class="chart-item">
          <div class="title">
            <div class="title-text">{{ chart3Data.label || '' }}</div>
          </div>
          <div class="charts" ref="chart13"></div>
        </div>
        
      </div>
    </div>
    <div class="active2" key="active2" v-if="active == 1">
      <div class="cdxm-top">
        <div class="cdxm-top-item" v-for="(item, index) in cdxmData?.top?.childs || []" :key="index">
          <div class="value">{{ item.value }}</div>
          <div class="label">
            <div class="l-icon"></div>
            <div class="name">{{ item.label }}{{ item.unit ? `/${item.unit}` : '' }}</div>
            <div class="r-icon"></div>
          </div>
        </div>
      </div>
      <div class="cdxm-center">
        <div class="cdxm-center-item" v-for="(item, index) in cdxmData?.center?.childs || []" :key="index">
          <div class="icon" :style="{ backgroundImage: `url(${projectIcons.cdxmDetail[index + 1]})` }"></div>
          <div class="values">
            <div class="label">{{ item.label }}</div>
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="cdxm-charts">
        <div class="cdxm-chart1">
          <div class="title">
            <div class="title-text">{{ '直流电流监测' }}</div>
          </div>
          <div class="chart-content" ref="chart1"></div>
        </div>
        <div class="cdxm-chart2">
          <div class="title">
            <div class="title-text">{{ '总有功功率趋势' }}</div>
          </div>
          <div class="chart-content" ref="chart2"></div>
        </div>
        <div class="cdxm-chart3">
          <div class="title">
            <div class="title-text">{{ '直流电压监测' }}</div>
          </div>
          <div class="chart-content" ref="chart3"></div>
        </div>
        <div class="cdxm-chart4">
          <div class="title">
            <div class="title-text">{{ '总无功功率趋势' }}</div>
          </div>
          <div class="chart-content" ref="chart4"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Tabbar from '../components/Tabbar'
import icons from '@/utils/icons.js'
import { cdxm_pilesMonitor, cdxm_chargeStationMonitor } from '@/api/cdxm'
import { formatSize } from '@/utils/echartsFontSize.js'
export default {
  name: 'cdxmdetail',
  components: {
    Tabbar,
  },
  data() {
    return {
      projectIcons: icons,
      tabbarList: [
        {
          label: '充电桩监测',
        },
        // { label: '逆变器监测' },
      ],
      active: 0,
      cdxmData: {
        top: {},
        center: {},
      },
      chart1: {},
      chart2: {},
      chart3: {},
      chart4: {},

      // 充电桩数据
      stationData: {},
      // 充电桩状态
      stationStatusData: {},
      //   饼图底部数据
      spearStatusData: {},
      multipleData: {},
      inUseData: {},
      devOpsData: {},

      chart1Data: {},
      chart2Data: {},
      chart3Data: {},
      chart4Data: {},
      chart1Dom: null,
      chart2Dom: null,
      chart3Dom: null,
      chart4Dom: null,
      chart4Options: {},
      pie3d: [],
      popoverShow: false,
      currentInUse: {},
      chargeActive: null,
    }
  },
  watch: {
    active: {
      handler() {
        if (this.active == 0) {
          this.initData()
        }
        if (this.active == 1) {
          this.getPilesMonitor()
        }
      },
      immediate: true,
    },
  },
  created() {},
  mounted() {
    document.addEventListener('click', () => {
      this.popoverShow = false
      this.chargeActive = null
    })
  },
  methods: {
    handleTip(obj, index) {
      if (obj.childs[0]?.value == '使用中') {
        this.currentInUse = obj
        this.chargeActive = index
        this.popoverShow = true
      }
    },
    initData() {
      cdxm_chargeStationMonitor().then((res) => {
        let data = res.data
        this.stationData = data.stationData || {}
        this.stationStatusData = data.stationStatusData || {}
        this.spearStatusData = data.spearStatusData || {}
        this.devOpsData = data.devOpsData || {}
        this.multipleData = data.multipleData || {}
        this.inUseData = data.inUseData || {}
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '0, 144, 255'
        this.chart2Data = data.chartList[1]
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '41, 245, 176'
        this.chart2Data.childs[1].style = '0'
        this.chart2Data.childs[1].color = '111, 133, 244'
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '81, 231, 226'
        // this.chart3Data.childs[1].style = '1'
        // this.chart3Data.childs[1].color = '12, 157, 253'
        this.chart4Data = data.chartList[3]
        this.$nextTick(() => {
          this.chart1Dom = this.$echarts.init(this.$refs.chart11, null, { renderer: 'svg' })
          this.chart2Dom = this.$echarts.init(this.$refs.chart12, null, { renderer: 'svg' })
          this.chart3Dom = this.$echarts.init(this.$refs.chart13, null, { renderer: 'svg' })
          this.chart4Dom = this.$echarts.init(this.$refs.chart14)
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
          needStart: true,
          needLegend: false,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        // this.chart1Dom.resize()
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        // this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          stackTotal: true,
          colorStops: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        // this.chart3Dom.resize()
        this.pie3d = this.chart4Data.childs[0].childs.map((item) => {
          return {
            name: item.label,
            value: Number(item.value),
          }
        })
        this.chart4Options = this.getPie3D(this.pie3d, 0)
        this.chart4Dom.setOption(this.chart4Options)
        // this.chart4Dom.resize()
      })
    },
    getPie3D(pieData, internalDiameterRatio) {
      let series = []
      let sumValue = 0
      let startValue = 0
      let endValue = 0
      let k = typeof internalDiameterRatio !== 'undefined' ? (1 - internalDiameterRatio) / (1 + internalDiameterRatio) : 1 / 3

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
      for (let i = 0; i < series.length; i++) {
        endValue = startValue + series[i].pieData.value

        series[i].pieData.startRatio = startValue / sumValue
        series[i].pieData.endRatio = endValue / sumValue
        series[i].parametricEquation = this.getParametricEquation(
          series[i].pieData.startRatio,
          series[i].pieData.endRatio,
          true,
          false,
          k,
          // 调整扇形高度
          i === 0 ? 10 : 10,
          i
        )

        startValue = endValue
      }

      series.push({
        name: 'pie2d',
        type: 'pie',
        label: {
          show: true,
          color: '#fff',
          fontSize: formatSize(14),
          overflow: 'none',
          formatter: function (val) {
            return `${val.name}：${val.value * 100}%`
          },
          overflow: 'breakAll',
        },
        labelLine: {
          normal: {
            length: formatSize(30),
            length2: formatSize(10),
            lineStyle: {
              color: '#e6e6e6',
              width: formatSize(1),
            },
          },
        },
        startAngle: 0, //起始角度，支持范围[0, 360]。
        clockwise: true, //饼图的扇区是否是顺时针排布。上述这两项配置主要是为了对齐3d的样式
        radius: ['20%', '70%'],
        center: ['50%', '50%'],
        data: this.pie3d,
        itemStyle: {
          color: 'rgba(0, 0, 0, 0)',
        },
      })

      // 准备待返回的配置项，把准备好的 legendData、series 传入。
      let obj = {
        //animation: false,
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
          boxHeight: formatSize(25),
          height: '100%',
          width: '100%',
          bottom: '50%',
          viewControl: {
            //3d效果可以放大、旋转等，请自己去查看官方配置
            alpha: 30,
            // 饼块开始得角度
            beta: -10,
            rotateSensitivity: 1,
            zoomSensitivity: 0,
            panSensitivity: 0,
            autoRotate: false,
            distance: 400,
          },
        },

        series: series,
      }
      return obj
    },
    getParametricEquation(startRatio, endRatio, isSelected, isHovered, k, h, i) {
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
      let offsetX = isSelected ? Math.cos(midRadian) * 0.3 : 0
      let offsetY = isSelected ? Math.sin(midRadian) * 0.1 : 0
      let offsetZ = i == 1 ? 2 : 0
      // 计算高亮效果的放大比例（未高亮，则比例为 1）
      let hoverRate = isHovered ? 1.05 : 1.4

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
            return offsetZ + Math.sin(u)
          }
          if (u > Math.PI * 2.5) {
            return offsetZ + Math.sin(u)
          }
          // 调整扇形高度
          return offsetZ + (Math.sin(v) > 0 ? 0.1 * h : -1)
        },
      }
    },
    getPilesMonitor() {
      cdxm_pilesMonitor({}).then((res) => {
        this.cdxmData = {
          top: res.data.topInfo,
          center: res.data.runningMonitor,
        }
        this.chart1 = res.data.chartList[0]
        this.chart2 = res.data.chartList[1]
        this.chart3 = res.data.chartList[2]
        this.chart4 = res.data.chartList[3]
        this.initCharts()
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.chart1.childs[0].style = '1'
        this.chart1.childs[0].color = '6, 94, 225'
        let chart1Data = {
          data: this.chart1,
          vm: this,
          needStart: true,
          smooth: false,
          needShadow: false,
          interval: 0,
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

        this.chart3.childs[0].style = '1'
        this.chart3.childs[0].color = '30, 187, 189'
        let chart3Data = {
          data: this.chart3,
          vm: this,
          needStart: true,
          smooth: false,
          needShadow: false,
          interval: 0,
          needLegend: false,
        }
        let chart3 = this.$echarts.init(this.$refs.chart3)
        chart3.setOption(this.$initChart(chart3Data))

        this.chart4.childs[0].style = '0'
        this.chart4.childs[0].color = '29, 210, 255'
        let chart4Data = {
          data: this.chart4,
          needLegend: false,
        }
        let chart4 = this.$echarts.init(this.$refs.chart4)
        chart4.setOption(this.$initChart(chart4Data))
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.cdxm {
  height: 100%;
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
  .active1 {
    margin-top: 20px;
    .t {
      display: flex;
      .l {
        width: 590px;
        .stationData-content {
          padding-left: 7px;
          display: flex;
          justify-content: space-between;
          .stationData-item {
            text-align: center;
            .icon {
              width: 148px;
              height: 144px;
              margin-top: 20px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
            }
            .value {
              font-size: 50px;
              font-weight: bold;
              margin-top: -6px;
            }
            .label {
              font-size: 22px;
              color: #fff;
            }
            &:nth-child(2n) {
              .value {
                background: linear-gradient(0deg, #3bcd5a 0%, #fff);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                text-shadow: 0px 6px 0px rgba(43, 159, 214, 0.2);
              }
            }
            &:nth-child(2n - 1) {
              .value {
                background: linear-gradient(0deg, #49c5ff 0%, #fff);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                text-shadow: 0px 6px 0px rgba(43, 159, 214, 0.2);
              }
            }
          }
        }
        .multipleData-content {
          margin-top: 26px;
          margin-bottom: 20px;
          display: flex;
          flex-wrap: wrap;
          padding-left: 7px;
          .multipleData-item {
            width: 583px;
            height: 83px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cdxm/bg_cpmm01.png');
            margin-top: 12px;
            &:first-child {
              margin-top: 0;
            }
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-left: 86px;
            .label {
              font-size: 21px;
              font-weight: bold;
              color: #eef3ff;
              width: 170px;
            }
            .value {
              width: 255px;
              text-align: center;
              span {
                display: inline-block;
                width: 255px;
                font-size: 36px;
                font-weight: bold;
                font-style: italic;
                background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
              }
            }
          }
        }
      }
      .c {
        flex: 1;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/cdxm/bg_cpm02.png');
        background-size: 1242px 462px;
        background-position: center 138px;
        position: relative;
        .inUseData-item {
          position: absolute;
          cursor: pointer;
          .icon {
            height: 164px;
            width: 228px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .use {
            background-image: url('~@/assets/img/cdxm/icon_cpm04.png');
          }
          .err {
            background-image: url('~@/assets/img/cdxm/icon_cpm10.png');
          }
          .idle {
            background-image: url('~@/assets/img/cdxm/icon_cpm05.png');
          }
          .label {
            text-align: center;
            margin-top: -18px;
            background-image: url('~@/assets/img/cdxm/bg_0000d.png');
            background-size: 152px 24px;
            background-repeat: no-repeat;
            background-position: center 24px;
            padding-bottom: 24px;

            span {
              font-size: 24px;
              background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
              font-style: italic;
              color: #eef3ff;
              font-weight: 700;
            }
          }
          .inUseData-err-popover {
            width: 236px;
            height: 86px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cdxm/bg_cpm05.png');
            margin-top: -250px;
            margin-left: -130px;
            padding-left: 63px;
            line-height: 86px;
            color: #eff0f1;
            font-size: 20px;
          }
          .inUseData-info-popover {
            position: absolute;
            width: 285px;
            height: 376px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cdxm/bg_cpm04.png');
            padding: 28px 10px 0 32px;
            .status {
              display: flex;
              justify-content: space-between;
              span {
                &:first-child {
                  color: #daeef9;
                  font-size: 24px;
                  font-weight: bold;
                }
                i {
                  font-size: 24px;
                  color: #fff;
                }
              }
            }
            .v-a {
              display: flex;
              justify-content: space-between;
              color: #daeef9;
              font-size: 18px;
              padding-right: 20px;
              margin-top: 10px;
            }
            .car {
              width: 204px;
              height: 122px;
              margin: 14px 0 0;
              border-top: 1px solid #146bff;
              background-repeat: no-repeat;
              background-size: 170px 95px;
              background-position: center center;
              background-image: url('~@/assets/img/cdxm/bg_cpm03.png');
            }
            .row {
              width: 243px;
              display: flex;
              justify-content: space-between;
              padding: 14px 0 14px;
              border-bottom: 1px solid #e8e8e8;

              &:last-child {
                border-bottom: none;
              }
              .row-l {
                display: flex;
                .row-icon {
                  width: 20px;
                  height: 20px;
                  background-repeat: no-repeat;
                  background-size: 100% 100%;
                  margin-right: 8px;
                }
                .row-label {
                  font-size: 18px;
                  color: #e8f2f6;
                }
              }
              .row-r {
                .row-value {
                  color: #2dff68;
                  font-size: 18px;
                }
              }
            }
            .first {
              padding-top: 0;
            }
          }
        }
        .inUseData-item0 {
          top: 120px;
          left: 150px;
          .inUseData-info-popover {
            top: -90px;
            left: -240px;
          }
        }
        .inUseData-item1 {
          top: 130px;
          left: 780px;
          .inUseData-info-popover {
            top: -90px;
            left: -240px;
          }
        }
        .inUseData-item2 {
          top: 520px;
          left: 110px;
          .inUseData-info-popover {
            top: -70px;
            left: 210px;
          }
        }
        .inUseData-item3 {
          top: 520px;
          left: 780px;
          .inUseData-info-popover {
            top: -90px;
            left: -240px;
          }
        }
        .inUseData-item4 {
          top: 540px;
          left: 330px;
          .inUseData-info-popover {
            top: -90px;
            left: -240px;
          }
        }
        .inUseData-item5 {
          top: 540px;
          left: 580px;
          .inUseData-info-popover {
            top: -90px;
            left: -240px;
          }
        }
      }
      .r {
        width: 590px;
        .devOpsData-content {
          margin-top: 10px;
          margin-bottom: 20px;
          display: flex;
          flex-wrap: wrap;
          padding-left: 7px;
          .devOpsData-item {
            width: 583px;
            height: 83px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cdxm/bg_cpmm01.png');
            margin-top: 12px;
            &:first-child {
              margin-top: 0;
            }
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-left: 86px;
            .label {
              font-size: 21px;
              font-weight: bold;
              color: #eef3ff;
              width: 170px;
            }
            .value {
              width: 255px;
              text-align: center;
              span {
                display: inline-block;
                width: 255px;
                font-size: 36px;
                font-weight: bold;
                font-style: italic;
                background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
              }
            }
          }
        }
        .stationStatusData-content {
          margin-top: 37px;
          margin-bottom: 40px;
          padding-left: 20px;
          .stationStatusData-item {
            margin-top: 4px;
            .stationStatusData-header {
              display: flex;
              align-items: center;
              justify-content: space-between;
              font-size: 20px;
              color: #ffffff;
              .value {
                // opacity: 0.6;
                color: #04a185;
              }
            }
            .stationStatusData-item-bg {
              height: 43px;
              margin-top: 10px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              background-image: url('~@/assets/img/cdxm/bg_charging_aa.png');
              .stationStatusData-item-bg-value {
                height: 100%;
                background: linear-gradient(90deg, rgba(#1f4b7a, 0.5) 0%, rgba(#065ee1, 0.5) 100%);
              }
            }
          }
        }
      }
    }
    .b {
      display: flex;
      
    }.chart-item {
        flex: 1;
        .charts {
          height: 428px;
        }
        .chart-pie {
          height: 276px;
        }
        .chart-item-footer {
          width: 509px;
          height: 100px;
          margin: 0 auto;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/cdxm/bg_cpm08.png');
          overflow: hidden;
          position: relative;
          .chart-item-footer-t {
            font-size: 13px;
            background: linear-gradient(180deg, #ddf9ff 60.6201171875%, #7bfaff 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            font-weight: bold;
            margin-top: 6px;
            margin-left: 7px;
          }
          .chart-item-footer-l {
            position: absolute;
            font-size: 36px;
            left: 89px;
            font-weight: bold;
            background: linear-gradient(180deg, #ffffff 34.6923828125%, #bce7ff 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
          .chart-item-footer-r {
            position: absolute;
            font-size: 36px;
            left: 349px;
            font-weight: bold;
            color: #fff;
          }
          .unit {
            font-size: 14px;
          }
        }
      }
  }
  .active2 {
    margin-top: 20px;
    .cdxm-top {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .cdxm-top-item {
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
    .cdxm-center {
      width: 100vw;
      margin-left: -123px;
      margin-top: 14px;
      height: 142px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/cdxm/bg_charging_va.png');
      padding: 0 123px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .cdxm-center-item {
        display: flex;
        align-items: flex-start;
        .icon {
          width: 94px;
          height: 95px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-right: 12px;
        }
        .values {
          padding-top: 6px;
          .label {
            border-left: 4px solid rgba($color: #80c5f0, $alpha: 0.8);
            min-width: 150px;
            padding: 4px 10px;
            background: linear-gradient(to right, #286eab 0%, rgba(#286eab, 0));
            font-size: 24px;
            color: #fff;
          }
          .value {
            font-size: 37px;
            color: #86d9ff;
            font-weight: bold;
            margin-top: 10px;
            .unit {
              font-size: 24px;
              font-weight: 400;
            }
          }
        }
      }
    }
    .cdxm-charts {
      margin-top: 20px;
      display: flex;
      flex-wrap: wrap;

      .cdxm-chart1,
      .cdxm-chart3 {
        width: 760px;
      }
      .cdxm-chart2,
      .cdxm-chart4 {
        width: calc(100% - 860px);
      }
      .cdxm-chart3,
      .cdxm-chart4 {
        margin-top: 20px;
      }
      .chart-content {
        height: 420px;
      }
    }
  }
}
</style>
