import { formatSize } from "@/utils/echartsFontSize.js";

import * as echarts from "echarts";
/*
 * echarts 初始化
 * @param {Object} data 数据
 * @param {Boolean} needLegend 是否需要图例
 * @param {String} direction 选项卡方向
 * @param {Boolean} needStart 是否需要开始
 * @param {Boolean} needShadow 是否需要阴影
 * @param {Boolean} smooth 是否平滑
 * @param {Boolean} colorStops 是否需要渐变色
 * @param {Boolean} stackTotal 是否需要堆叠合计
 * @param {String} interval 选项卡间隔
 * @param {Boolean} chartFull 是否全屏
 */
export const initChart = ({
  data = {},
  needPieLable = true,
  needLegend = true,
  direction = "1",
  needStart = false,
  needShadow = true,
  smooth = true,
  colorStops = true,
  stackTotal = false,
  interval = "auto",
  chartFull = false,
  singleYAxis = false,
  colorList = [],
}) => {
  if (data.style == 2) {
    let pieOptions = {
      grid: {
        containLabel: true,
      },
      legend: {
        type: "scroll",
        show: needLegend,
        bottom: formatSize(10),
        textStyle: {
          fontSize: formatSize(14),
          color: "#B5C5D4",
          fontFamily: "PingFang SC, wryh",
        },
        itemWidth: formatSize(12),
        itemHeight: formatSize(12),
      },
      series: [
        {
          type: "pie",
          avoidLabelOverlap: true,
          radius: chartFull ? ["60%", "90%"] : ["40%", "65%"],
          // right: chartFull ? "0" : "30%",
          // silent: true,
          label: {
            show: false,
            fontSize: formatSize(14),
            formatter: "{b}: {d}%",
          },
          emphasis: {
            label: {
              show: true,
              fontSize: formatSize(14),
              overflow: "break",
              // fontWeight: 'bold'
            },
          },
          labelLine: {
            showAbove: false,
            // length: formatSize(0),
            // length2: formatSize(30),
            lineStyle: {
              width: formatSize(1),
            },
          },
        },
      ],
    };
    if (!needPieLable) {
      pieOptions.series[0].label.position = "center";
      pieOptions.series[0].label.width = formatSize(100);
      pieOptions.series[0].label.fontSize = formatSize(24);
      // pieOptions.series[0].label.formatter = () => {
      //   return data.label;
      // };
    }
    pieOptions.series[0].data = data.childs.map((item, k) => {
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
          };
    });
    return pieOptions;
  }
  let options = {
    tooltip: {
      show: true,
      trigger: "axis",
      textStyle: {
        fontSize: formatSize(14),
        fontFamily: "PingFang SC, wryh",
      },
    },
    legend: {
      show: needLegend,
      top: formatSize(0),
      right: !singleYAxis ? formatSize(70) : formatSize(20),
      textStyle: {
        fontSize: formatSize(12),
        color: "#B5C5D4",
        fontFamily: "PingFang SC, wryh",
      },
      itemWidth: formatSize(10),
      itemHeight: formatSize(10),
    },
    grid: {
      top: formatSize(40),
      left: formatSize(24),
      right: formatSize(24),
      bottom: formatSize(0),
      containLabel: true,
    },
    xAxis: {
      type: direction === "1" ? "category" : "value",
      axisLabel: {
        fontSize: formatSize(12),
        color: "#B5C5D4",
        fontFamily: "PingFang SC, wryh",
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
      },
    },
    yAxis: {
      type: "value",
      name: data.unit || "",
      nameTextStyle: {
        fontSize: formatSize(12),
        fontFamily: "PingFang SC, wryh",
      },
      axisLabel: {
        fontSize: formatSize(12),
        color: "#B5C5D4",
        fontFamily: "PingFang SC, wryh",
      },
      splitLine: {
        lineStyle: {
          width: formatSize(1),
          type: "dashed", //虚线
          color: "#B2C2D3",
        },
        show: true, //隐藏
      },
    },

    series: [],
  };
  if (direction === "1") {
    options.xAxis.data = data.childs.map((item) => {
      return {
        value: item.label,
        textStyle: {
          width: formatSize(60),
          overflow: "truncate",
        },
      };
    });
    options.xAxis.boundaryGap = needStart;
    options.xAxis.axisLabel.interval = interval;
  }
  if (direction === "2") {
    options.yAxis.data = data.childs[0].childs.map((item) => item.label);
    options.yAxis.forEach((item) => {
      item.boundaryGap = needStart;
      item.axisLabel.interval = interval;
    });
  }

  options.series = [data].map((item, index) => {
    let i = {
      name: item.label,
      color: `rgb(${item.color})`,
      type:
        item.style === "0"
          ? "line"
          : item.style === "1"
          ? "bar"
          : item.style === "2"
          ? "pie"
          : "",
      data: item.childs.map((k) => {
        return k.value;
      }),
    };
    if (!singleYAxis) {
      i.yAxisIndex = index;
    }
    if (item.style === "1") {
      // i.barWidth = formatSize(26)
      if (stackTotal) {
        i.stack = "total";
      }
      if (colorStops) {
        i.itemStyle = {
          normal: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              {
                // 四个数字分别对应 数组中颜色的开始位置，分别为 右，下，左，上。例如（1,0,0,0 ）代表从右边开始渐
                // 变。offset取值为0~1，0代表开始时的颜色，1代表结束时的颜色，柱子表现为这两种颜色的渐变。
                offset: 0,
                color: `rgba(${item.color}, 0.8)`,
              },
              {
                offset: 1,
                color: `rgba(${item.color}, 1)`,
              },
            ]),
          },
        };
      }
    }
    if (item.style === "0") {
      i.smooth = smooth;
      i.symbol = "none";
      i.lineStyle = Object.assign(
        {},
        {
          width: formatSize(2),
        }
      );
      if (needShadow) {
        i.areaStyle = {
          color: {
            type: "linear",
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: `rgba(${item.color}, 0.8)`, // 100% 处的颜色
                opacity: 0.1,
              },
              {
                offset: 1,
                color: `rgba(${item.color}, 0)`, //   0% 处的颜色
              },
            ],
            global: false, // 缺省为 false
          },
        };
      }
    }
    return i;
  });
  return options;
};
