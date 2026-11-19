import { formatSize } from '@/utils/echartsFontSize.js'
/*
 * echarts 初始化
 * @param {Object} data 数据
 * @param {Boolean} needLegend 是否需要图例
 * @param {String} direction 选项卡方向
 * @param {Boolean} needStart 是否需要开始
 * @param {Boolean} needShadow 是否需要阴影
 * @param {Boolean} smooth 是否平滑
 * @param {Object} vm Vue实例
 * @param {Boolean} colorStops 是否需要渐变色
 * @param {Boolean} stackTotal 是否需要堆叠合计
 * @param {String} interval 选项卡间隔
 * @param {Boolean} chartFull 是否全屏
 */
export const initChart = ({
  data = {},
  needPieLable = true,
  needLegend = true,
  direction = '1',
  needStart = false,
  needShadow = true,
  smooth = true,
  vm,
  colorStops = true,
  stackTotal = false,
  interval = 'auto',
  chartFull = false,
  colorList = [],
}) => {
  if (data.childs.find((item) => item.style == 2)) {
    let pieOptions = {
      grid: {
        containLabel: true,
      },
      legend: {
        show: needLegend,
        y: 'center',
        right: formatSize(10),
        orient: 'vertical',
        textStyle: {
          fontSize: formatSize(18),
          color: '#B5C5D4',
          fontFamily: 'PingFang SC, wryh',
        },
        itemWidth: formatSize(14),
        itemHeight: formatSize(14),
      },
      series: [
        {
          type: 'pie',
          radius: chartFull ? ['60%', '90%'] : ['40%', '60%'],
          right: chartFull ? '0' : '30%',
          silent:true,
          label: {
            show: true,
            fontSize: formatSize(14),
            overflow: 'break',
            formatter: '{b}: {d}%',
          },
          labelLine: {
            showAbove: false,
            length: formatSize(0),
            length2: formatSize(30),
            lineStyle: {
              width: formatSize(1),
            },
          },
        },
      ],
    }
    if (!needPieLable) {
      pieOptions.series[0].label.position = 'center'
      pieOptions.series[0].label.width = formatSize(100)
      pieOptions.series[0].label.fontSize = formatSize(24)
      pieOptions.series[0].label.formatter = () => {
        return data.label
      }
    }
    pieOptions.series[0].data = data.childs[0].childs.map((item, k) => {
      return colorList.length
        ? {
            value: Number(item.value),
            name: item.label,
            itemStyle: {
              color: colorList[k],
            },
          }
        : {
            value: Number(item.value),
            name: item.label,
          }
    })
    return pieOptions
  }

  let options = {
    tooltip: {
      show: true,
      trigger: 'axis',
      textStyle: {
        fontSize: formatSize(24),
        fontFamily: 'PingFang SC, wryh',
      },
    },
    legend: {
      show: needLegend,
      top: formatSize(0),
      right: formatSize(80),
      textStyle: {
        fontSize: formatSize(18),
        color: '#B5C5D4',
        fontFamily: 'PingFang SC, wryh',
      },
      itemWidth: formatSize(25),
      itemHeight: formatSize(14),
    },
    grid: {
      // top: 20,
      left: formatSize(14),
      right: formatSize(14),
      bottom: formatSize(24),
      containLabel: true,
    },
    xAxis: {
      type: direction === '1' ? 'category' : 'value',
      axisLabel: {
        fontSize: formatSize(16),
        color: '#B5C5D4',
        fontFamily: 'PingFang SC, wryh',
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
      },
    },
    yAxis: data.childs.map((i, _index) => {
      return {
        type: direction === '1' ? 'value' : 'category',
        name: i.unit || '',
        nameTextStyle: {
          fontSize: formatSize(16),
          fontFamily: 'PingFang SC, wryh',
        },
        alignTicks: _index === 0,
        splitNumber: 2,
        axisLabel: {
          fontSize: formatSize(16),
          color: '#B5C5D4',
          fontFamily: 'PingFang SC, wryh',
        },
        splitLine: {
          lineStyle: {
            type: 'solid', //虚线
            color: '#F4F5F9',
          },
          show: true, //隐藏
        },
      }
    }),

    series: [],
  }
  if (direction === '1') {
    options.xAxis.data = data.childs[0].childs.map((item) => item.label)
    options.xAxis.boundaryGap = needStart
    options.xAxis.axisLabel.interval = interval
  }
  if (direction === '2') {
    options.yAxis.data = data.childs[0].childs.map((item) => item.label)
    options.yAxis.forEach((item) => {
      item.boundaryGap = needStart
      item.axisLabel.interval = interval
    })
  }

  options.series = data.childs.map((item, index) => {
    let i = {
      name: item.label,
      color: `rgb(${item.color})`,
      yAxisIndex: index,
      type: item.style === '0' ? 'line' : item.style === '1' ? 'bar' : item.style === '2' ? 'pie' : '',
      data: item.childs.map((k) => {
        return k.value
      }),
    }
    if (item.style === '1') {
      // i.barWidth = formatSize(26)
      if (stackTotal) {
        i.stack = 'total'
      }
      if (colorStops) {
        i.itemStyle = {
          normal: {
            color: new vm.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                // 四个数字分别对应 数组中颜色的开始位置，分别为 右，下，左，上。例如（1,0,0,0 ）代表从右边开始渐
                // 变。offset取值为0~1，0代表开始时的颜色，1代表结束时的颜色，柱子表现为这两种颜色的渐变。
                offset: 0,
                color: `rgba(${item.color}, 1)`,
              },
              {
                offset: 1,
                color: `rgba(${item.color}, 0)`,
              },
            ]),
          },
        }
      }
    }
    if (item.style === '0') {
      i.smooth = smooth
      i.symbol = 'none'
      i.lineStyle = Object.assign(
        {},
        {
          width: formatSize(3),
        }
      )
      if (needShadow) {
        i.areaStyle = {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: `rgba(${item.color}, 0.2)`, // 100% 处的颜色
                opacity: 0.1,
              },
              {
                offset: 1,
                color: `rgba(${item.color}, 0)`, //   0% 处的颜色
              },
            ],
            global: false, // 缺省为 false
          },
        }
      }
    }
    return i
  })

  return options
}
