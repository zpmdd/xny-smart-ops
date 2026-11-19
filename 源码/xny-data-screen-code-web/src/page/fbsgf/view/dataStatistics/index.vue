<template>
  <div class="fbsgf-dataStatistics view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="topDataStatistics-content">
        <div class="topDataStatistics-item" v-for="(item, index) in topDataStatistics.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.fbsgf.topDataStatistics[index]})` }">
          <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
          <div class="value">{{ item.value }}</div>
        </div>
      </div>
      <div class="chart-content">
        <div class="chart-item">
          <TextTittle :label="chart1Data?.label || ''"></TextTittle>
          <div class="chart1 charts" ref="chart1"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart2Data?.label || ''"></TextTittle>
          <div class="chart2 charts" ref="chart2"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart3Data?.label || ''"></TextTittle>
          <div class="chart3 charts" ref="chart3"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart4Data?.label || ''"></TextTittle>
          <div class="chart4 charts" ref="chart4"></div>
        </div>
        <div class="chart-item">
          <div class="select">
            <TextTittle :label="chart5Data?.label || ''"></TextTittle>
            <div class="query">
              <div class="item" v-for="(item, index) in selectList" :key="index" @click="handleCheck(item)">
                <div class="icon" :class="{ chartActive: item.value === chartActive }"></div>
                <span>{{ item.label }}</span>
              </div>
            </div>
          </div>
          <div class="chart5 charts" ref="chart5"></div>
        </div>
        <div class="chart-item">
          <TextTittle :label="chart6Data?.label || ''"></TextTittle>
          <div class="chart6 charts" ref="chart6"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { dataStatistics } from '@/api/fbsgf.js'
export default {
  data() {
    return {
      projectIcons: icons,

      topDataStatistics: {},

      chart1Dom: null,
      chart2Dom: null,
      chart3Dom: null,
      chart4Dom: null,
      chart5Dom: null,
      chart6Dom: null,
      chart1Data: {},
      chart2Data: {},
      chart3Data: {},
      chart4Data: {},
      chart5Data: {},
      chart6Data: {},
      optionData: [],

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
    }
  },
  watch: {
    chartActive() {
      dataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.chart5Data = data.chartList[4]
        this.chart5Data.childs[0].style = '0'
        this.chart5Data.childs[0].color = '126, 184, 255'
        this.chart5Data.childs[1].style = '0'
        this.chart5Data.childs[1].color = '235, 179, 101'
        this.$nextTick(() => {
          this.initChart()
        })
      })
    },
  },
  created() {
    this.initData()
  },
  mounted() {
    this.$bus.$on('onresize', () => {
      this.initChart()
    })
    this.$bus.$on('refreshData', () => {
      this.initData('1')
    })
  },
  methods: {
    handleCheck(obj) {
      this.chartActive = obj.value
    },
    initData(needInitChart) {
      dataStatistics({ type: this.chartActive }).then((res) => {
        let data = res.data
        this.topDataStatistics = data.topDataStatistics || {}
        this.chart1Data = data.chartList[0]
        this.chart2Data = data.chartList[1]
        this.chart2Data.childs[0].style = '0'
        this.chart2Data.childs[0].color = '41, 245, 175'
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '1'
        this.chart3Data.childs[0].color = '126, 184, 255'
        this.chart3Data.childs[1].style = '0'
        this.chart3Data.childs[1].color = '0, 255, 255'
        this.chart4Data = data.chartList[3]
        this.chart4Data.childs[0].style = '0'
        this.chart4Data.childs[0].color = '29, 210, 255'
        this.chart5Data = data.chartList[4]
        this.chart5Data.childs[0].style = '0'
        this.chart5Data.childs[0].color = '126, 184, 255'
        this.chart5Data.childs[1].style = '0'
        this.chart5Data.childs[1].color = '235, 179, 101'
        this.chart6Data = data.chartList[5]
        this.chart6Data.childs[0].style = '1'
        this.chart6Data.childs[0].color = '206, 173, 103'
        this.optionData = data.chartList[0].childs[0].childs.map((item) => {
          let i = {
            name: item.label,
            value: Number(item.value),
          }
          return i
        })
        this.$nextTick(() => {
          if (!needInitChart) {
            this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'canvas' })
            this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
            this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
            this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
            this.chart5Dom = this.$echarts.init(this.$refs.chart5, null, { renderer: 'svg' })
            this.chart6Dom = this.$echarts.init(this.$refs.chart6, null, { renderer: 'svg' })
          }
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart2Options = {
          data: this.chart2Data,
          needLegend: false,
          needStart: true,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        this.chart2Dom.resize()
        let chart3Options = {
          data: this.chart3Data,
          needStart: true,
          needShadow: false,
          smooth: false,
          vm: this,
        }
        this.chart3Dom.setOption(this.$initChart(chart3Options))
        this.chart3Dom.resize()
        let chart4Options = {
          data: this.chart4Data,
          needLegend: false,
          needStart: true,
        }
        this.chart4Dom.setOption(this.$initChart(chart4Options))
        this.chart4Dom.resize()
        let chart5Options = {
          data: this.chart5Data,
          smooth: false,
          needStart: true,
        }
        this.chart5Dom.setOption(this.$initChart(chart5Options))
        this.chart5Dom.resize()
        let chart6Options = {
          data: this.chart6Data,
          needStart: true,
          needShadow: false,
          needLegend: false,
          smooth: false,
          vm: this,
        }
        this.chart6Dom.setOption(this.$initChart(chart6Options))
        this.chart6Dom.resize()

        this.init()
      })
    },
    init() {
      //构建3d饼状图
      this.option = this.getPie3D(this.optionData, 0.7) //空心圆的大小
      this.chart1Dom.setOption(this.option)
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
      this.chart1Dom.resize()
      this.chart1Dom.setOption(this.option)
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
          left: -this.$formatSize(100),
          viewControl: {
            //3d效果可以放大、旋转等，请自己去查看官方配置
            alpha: 20, //角度
            distance: 200, //调整视角到主体的距离，类似调整zoom
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
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.fbsgf-dataStatistics {
  .view-container {
    .topDataStatistics-content {
      display: flex;
      justify-content: space-between;
      .topDataStatistics-item {
        width: 346px;
        height: 136px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding-left: 154px;
        padding-top: 35px;
        .label {
          color: #cde3f6;
          font-size: 16px;
        }
        .value {
          color: #f4fdff;
          font-size: 32px;
          font-family: douyv;
          word-break: break-all;
        }
      }
    }
    .chart-content {
      display: flex;
      flex-wrap: wrap;
      gap: 10px 15px;
      margin-top: 30px;
      .chart-item {
        height: 340px;
        width: 610px;
        .charts {
          height: 292px;
        }
        .select {
          position: relative;
          .query {
            position: absolute;
            right: 0;
            top: 6px;
            display: flex;
            align-items: center;
            // padding-right: 100px;
            .item {
              display: flex;
              align-items: center;
              cursor: pointer;
              font-size: 18px;
              color: #cde3f6;
              margin-left: 10px;
              .icon {
                height: 34px;
                width: 34px;
                background-size: cover;
                background-repeat: no-repeat;
                background-image: url('~@/assets/img/common/icon_select02.png');
              }
              .chartActive {
                background-image: url('~@/assets/img/common/icon_select01.png');
              }
            }
          }
        }
      }
    }
  }
}
</style>
