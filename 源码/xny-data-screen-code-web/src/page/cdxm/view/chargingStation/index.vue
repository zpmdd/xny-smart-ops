<template>
  <div class="cdxm-chargingStation view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="t">
        <div class="l">
          <TextTittle :label="stationData.label || ''"></TextTittle>
          <div class="stationData-content">
            <div class="stationData-item" v-for="(item, index) in stationData.childs || []" :key="index">
              <div class="icon" :style="{ backgroundImage: `url(${projectIcons.cdxm.stationData[index % 2]})` }"></div>
              <div class="value">{{ item.value }}</div>
              <div class="label">{{ item.label }}</div>
            </div>
          </div>
          <!-- <div class="multipleData-content">
            <div class="multipleData-item" v-for="(item, index) in multipleData.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div> -->
          <div class="chart-item">
            <TextTittle :label="chart4Data.label || ''"></TextTittle>
            <div class="charts chart-pie" ref="chart4"></div>
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
            <div class="label">{{ item.label }}</div>
            <div class="inUseData-info-popover" v-if="popoverShow && active === index">
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
          <TextTittle :label="devOpsData.label || ''"></TextTittle>
          <div class="devOpsData-content">
            <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
              <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
              <div class="value">
                <span> {{ item.value }}</span>
              </div>
            </div>
          </div>
          <TextTittle :label="stationStatusData.label || ''"></TextTittle>
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
          <TextTittle :label="chart1Data.label || ''"></TextTittle>
          <div class="charts" ref="chart1"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart2Data.label || ''"></TextTittle>
          <div class="charts" ref="chart2"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart3Data.label || ''"></TextTittle>
          <div class="charts" ref="chart3"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { chargeStationMonitor } from '@/api/cdxm'
import { formatSize } from '@/utils/echartsFontSize.js'
export default {
  data() {
    return {
      projectIcons: icons,

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
      active: null,
      currentInUse: {},
    }
  },
  created() {
    this.initData()
  },
  mounted() {
    this.$bus.$on('onresize', () => {
      this.initChart()
    })
    document.addEventListener('click', () => {
      this.popoverShow = false
      this.active = null
    })
    this.$bus.$on('refreshData', () => {
      this.initData('1')
    })
  },
  methods: {
    handleTip(obj, index) {
      if (obj.childs[0]?.value == '使用中') {
        this.currentInUse = obj
        this.active = index
        this.popoverShow = true
      }
    },
    initData(needInitChart) {
      chargeStationMonitor().then((res) => {
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
          if (!needInitChart) {
            this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
            this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
            this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
            this.chart4Dom = this.$echarts.init(this.$refs.chart4)
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
          needLegend: false,
        }
        console.log(this.$initChart(chart1Options), 'this.$initChart(chart1Options)')
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          stackTotal: true,
          colorStops: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        this.chart3Dom.resize()
        this.pie3d = this.chart4Data.childs[0].childs.map((item) => {
          return {
            name: item.label,
            value: Number(item.value),
          }
        })
        this.chart4Options = this.getPie3D(this.pie3d, 0)
        this.chart4Dom.setOption(this.chart4Options)
        this.chart4Dom.resize()
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
          formatter: function (val) {
            return `${val.name}：${val.value * 100}%`
          },
          overflow: 'breakAll',
        },
        labelLine: {
          normal: {
            length: formatSize(30),
            length2: formatSize(40),
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
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.cdxm-chargingStation {
  .t {
    display: flex;
    .l {
      width: 410px;
      .stationData-content {
        padding-left: 26px;
        display: flex;
        justify-content: space-between;
        .stationData-item {
          text-align: center;
          .icon {
            width: 125px;
            height: 119px;
            margin-top: -7px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .value {
            font-size: 32px;
            font-weight: bold;
            margin-top: -6px;
          }
          .label {
            font-size: 14px;
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
        margin-top: 10px;
        margin-bottom: 20px;
        display: flex;
        flex-wrap: wrap;
        padding-left: 26px;
        .multipleData-item {
          width: 420px;
          height: 65px;
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
            width: 130px;
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
    .c {
      flex: 1;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/cdxm/bg_cpm02.png');
      background-size: 945px 352px;
      background-position: center center;
      position: relative;
      .inUseData-item {
        position: absolute;
        cursor: pointer;
        .icon {
          height: 125px;
          width: 174px;
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
          font-size: 18px;
          margin-top: -13px;
          background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          font-style: italic;
          color: #eef3ff;
          font-weight: 700;
        }
        .inUseData-err-popover {
          width: 180px;
          height: 66px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/cdxm/bg_cpm05.png');
          margin-top: -180px;
          margin-left: -100px;
          padding-left: 48px;
          line-height: 66px;
          color: #eff0f1;
          font-size: 15px;
        }
        .inUseData-info-popover {
          position: absolute;
          width: 213px;
          height: 281px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/cdxm/bg_cpm04.png');
          padding: 16px 8px 0 24px;
          .status {
            display: flex;
            justify-content: space-between;
            span {
              &:first-child {
                color: #daeef9;
                font-size: 18px;
                font-weight: bold;
              }
              i {
                font-size: 16px;
                color: #fff;
              }
            }
          }
          .v-a {
            display: flex;
            justify-content: space-between;
            color: #daeef9;
            font-size: 14px;
            padding-right: 20px;
            margin-top: 10px;
          }
          .car {
            width: 152px;
            height: 90px;
            margin: 10px 0 0;
            border-top: 1px solid #146bff;
            background-repeat: no-repeat;
            background-size: 127px 71px;
            background-position: center center;
            background-image: url('~@/assets/img/cdxm/bg_cpm03.png');
          }
          .row {
            width: 181px;
            display: flex;
            justify-content: space-between;
            padding: 10px 0 10px;
            border-bottom: 1px solid #e8e8e8;

            &:last-child {
              border-bottom: none;
            }
            .row-l {
              display: flex;
              .row-icon {
                width: 16px;
                height: 16px;
                background-repeat: no-repeat;
                background-size: 100% 100%;
                margin-right: 10px;
              }
              .row-label {
                font-size: 14px;
                color: #e8f2f6;
              }
            }
            .row-r {
              .row-value {
                color: #2dff68;
                font-size: 14px;
              }
            }
          }
          .first {
            padding-top: 0;
          }
        }
      }
      .inUseData-item0 {
        top: 100px;
        left: 170px;
        .inUseData-info-popover {
          top: -90px;
          left: -180px;
        }
      }
      .inUseData-item1 {
        top: 110px;
        left: 680px;
        .inUseData-info-popover {
          top: -90px;
          left: -180px;
        }
      }
      .inUseData-item2 {
        top: 320px;
        left: 90px;
        .inUseData-info-popover {
          top: -70px;
          left: 160px;
        }
      }
      .inUseData-item3 {
        top: 320px;
        left: 720px;
        .inUseData-info-popover {
          top: -90px;
          left: -180px;
        }
      }
      .inUseData-item4 {
        top: 380px;
        left: 300px;
        .inUseData-info-popover {
          top: -90px;
          left: -180px;
        }
      }
      .inUseData-item5 {
        top: 380px;
        left: 540px;
        .inUseData-info-popover {
          top: -90px;
          left: -180px;
        }
      }
    }
    .r {
      width: 465px;
      .devOpsData-content {
        margin-top: 10px;
        margin-bottom: 20px;
        display: flex;
        flex-wrap: wrap;
        padding-left: 30px;
        .devOpsData-item {
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
      .stationStatusData-content {
        margin-top: 20px;
        margin-bottom: 40px;
        padding-left: 20px;
        .stationStatusData-item {
          margin-top: 4px;
          .stationStatusData-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 14px;
            color: #ffffff;
            .value {
              opacity: 0.6;
            }
          }
          .stationStatusData-item-bg {
            height: 20px;
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
  }
  .chart-item {
    flex: 1;
    .charts {
      height: 300px;
    }
    .chart-pie {
      height: 195px;
    }
    .chart-item-footer {
      width: 390px;
      height: 77px;
      margin: 0 auto;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/cdxm/bg_cpm08.png');
      overflow: hidden;
      position: relative;
      .chart-item-footer-t {
        font-size: 10px;
        background: linear-gradient(180deg, #ddf9ff 60.6201171875%, #7bfaff 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-weight: bold;
        margin-top: 6px;
        margin-left: 7px;
      }
      .chart-item-footer-l {
        position: absolute;
        font-size: 28px;
        left: 68px;
        font-weight: bold;
        background: linear-gradient(180deg, #ffffff 34.6923828125%, #bce7ff 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
      .chart-item-footer-r {
        position: absolute;
        font-size: 28px;
        left: 268px;
        font-weight: bold;
        color: #fff;
      }
      .unit {
        font-size: 9px;
      }
    }
  }
}
</style>
