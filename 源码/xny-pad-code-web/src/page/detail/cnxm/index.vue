<template>
  <div class="energyStorage project-data-page">
    <Tabbar :tabbar="tabbarList" :active.sync="active"></Tabbar>
    <div class="data-content">
      <div class="content1" v-if="active === 0">
        <div class="content-item msg">
          <div class="title">
            <div class="title-text">{{ active1Data?.energyStorageMsg?.label || '' }}</div>
          </div>
          <div class="content-value">
            <div class="value-item" v-for="(item, index) in active1Data?.energyStorageMsg?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.cnxmDetail1Top[index + 1] + ')' }">
              <div class="key">
                {{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}
                <!-- <div class="arrow">
                  <div :class="{ 'arrow-up': item.status == 1, 'arrow-down': item.status == 0 }"></div>
                </div> -->
              </div>
              <div class="val">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="content-item status">
          <div class="status-l">
            <div class="title">
              <div class="title-text">{{ active1Data.devOpsData?.label || '' }}</div>
            </div>
            <div class="devOpsData-value">
              <div class="devOpsData-item" v-for="(item, index) in active1Data.devOpsData?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.cnxmDetailDevOpsData[index + 1] + ')' }">
                <div class="label">{{ item.label || '' }}</div>
                <div class="value">
                  {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="status-r">
            <div class="title-text">{{ active1Data?.runningMonitor?.label || '' }}</div>
            <div class="center-list">
              <div class="center-item" :class="{ mt0: index < 5 }" v-for="(item, index) in active1Data?.runningMonitor?.childs || []" :key="index">
                <div class="value">
                  {{ item.value | formatNum }} <span class="unit">{{ item.unit ? `${item.unit}` : '' }}</span>
                </div>
                <div class="label">
                  <div class="l-icon"></div>
                  <div class="name">{{ item.label || '' }}</div>
                  <div class="r-icon"></div>
                </div>
              </div>
            </div>
            <div class="title-text">{{ '异常状态分析' }}</div>
            <div class="warnList-content">
              <div class="table-content">
                <div class="scroll">
                  <div v-for="(item, index) in active1Data.warnList || []" :key="index">
                    <div v-for="(item1, index1) in item" :key="index1">
                      <em>{{ item1 }}</em>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="content2" v-if="active === 1">
        <div class="top">
          <div class="top-item" :class="matteryStatus(item, index)" v-for="(item, index) in active2Data?.topData?.childs || []" :key="index" :style="{ 'background-image': 'url(' + projectIcons.cnxmDetail2Top[index + 1] + ')' }">
            <div class="label">
              {{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}
              <i class="status-icon"></i>
            </div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <div class="center">
          <div class="title-text">{{ active2Data?.centerData?.label || '' }}</div>
          <div class="center-list">
            <div class="center-item" v-for="(item, index) in active2Data?.centerData?.childs || []" :key="index">
              <div class="value">
                {{ item.value | formatNum }} <span class="unit">{{ item.unit ? `${item.unit}` : '' }}</span>
              </div>
              <div class="label">
                <div class="l-icon"></div>
                <div class="name">{{ item.label || '' }}</div>
                <div class="r-icon"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom">
          <div class="cl">
            <div class="title-text">{{ chart1.label || '' }}</div>
            <div class="chart-content" ref="chart1"></div>
          </div>
          <div class="cr">
            <div class="title-text">{{ active2Data?.errorList?.label || '' }}</div>
            <div class="cr-list">
              <ul>
                <li v-for="(item, index) in active2Data?.errorList?.childs || []" :key="index">
                  <div class="title">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
                  <div class="value">{{ item.value }}</div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Header from '@/components/Header'
import Tabbar from '../components/Tabbar'
import icons from '@/utils/icons.js'
import { mapState } from 'vuex'
import { getDeviceMonitor, getBatteryMonitor, newDeviceMonitor } from '@/api/cnxm'
export default {
  name: 'cnxm',
  data() {
    return {
      tabbarList: [
        {
          label: '储能设备监测',
        },
        // { label: '储能电池监测' },
      ],
      active: 0,
      statusTip: [
        {
          color: '#0072CE',
          value: '正常',
        },
        {
          color: '#C02E40',
          value: '告警',
        },
        {
          color: '#469E90',
          value: '未启用',
        },
      ],
      projectIcons: icons,
      active1Data: {
        energyStorageStatusList: {},
        energyStorageMsg: {},
        devOpsData: {},
      },
      active2Data: {
        topData: {},
        centerData: {},
        errorList: {},
      },
      chart1: {},
    }
  },
  watch: {
    active: {
      handler(val) {
        if (val == 0) {
          this.getDeviceMonitor()
        }
        if (val == 1) {
          this.getBatteryMonitor()
        }
      },
      immediate: true,
    },
  },
  computed: {
    matteryStatus() {
      return (obj, index) => {
        if (index !== 0) {
          return ''
        }
        if (obj.value == '充电中') {
          return 'charge'
        }
        if (obj.value == '放电中') {
          return 'discharge'
        }
        if (obj.value == '故障') {
          return 'fault'
        }
        if (obj.value == '待机') {
          return 'idle'
        }
        return ''
      }
    },
  },
  created() {},

  mounted() {},
  methods: {
    getDeviceMonitor() {
      newDeviceMonitor().then((res) => {
        if (res.data) {
          this.active1Data = {
            energyStorageMsg: res.data.storageInfoMonitor,
            energyStorageStatusList: res.data.storageStatusMonitor,
            devOpsData: res.data.devOpsData,
            runningMonitor: res.data.runningMonitor,
            warnList: res.data.warnList,
          }
        }
      })
    },
    getBatteryMonitor() {
      getBatteryMonitor().then((res) => {
        if (res.data) {
          this.active2Data = {
            topData: res.data.topInfo,
            centerData: res.data.runningMonitor,
            errorList: res.data.abnormalAlarm,
          }
          this.chart1 = res.data.chartList[0]
          this.initCharts()
        }
      })
    },
    initCharts() {
      this.chart1.childs[0].style = '0'
      this.chart1.childs[0].color = '44, 216, 111'
      this.chart1.childs[1].style = '0'
      this.chart1.childs[1].color = '14, 156, 255'
      let chart1Data = {
        data: this.chart1,
      }
      let chart1 = this.$echarts.init(this.$refs.chart1)
      chart1.setOption(this.$initChart(chart1Data))
    },
  },
  components: {
    Header,
    Tabbar,
  },
}
</script>
<style lang="scss" scoped>
@keyframes up {
  0% {
    transform: translateY(100%);
  }
  50% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-100%);
  }
}
@keyframes down {
  0% {
    transform: translateY(-100%);
  }
  50% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(100%);
  }
}
@keyframes slideInLeft {
  0% {
    transform: translateX(-100%);
    opacity: 0;
  }
  100% {
    transform: translateX(0);
    opacity: 1;
  }
}
.energyStorage {
  height: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  .data-content {
    margin-top: 22px;
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
    .content1 {
      .content-item {
        margin-top: 40px;
        &:first-child {
          margin-top: 0;
        }
        .title {
          display: flex;
          justify-content: space-between;

          .status-tip {
            display: flex;
            align-items: center;
            & > div {
              margin-left: 30px;
              display: flex;
              align-items: center;
              & > div {
                width: 30px;
                height: 14px;
              }
              span {
                font-size: 16px;
                color: #fff;
                margin-left: 10px;
              }
            }
          }
        }
        .content-value {
          margin-top: 30px;
          display: flex;
          flex-wrap: wrap;
          justify-content: space-between;
          .value-item {
            height: 168px;
            width: 430px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            padding-left: 193px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            .key {
              color: #cde3f6;
              font-size: 20px;
              display: flex;
              align-items: center;
              .arrow {
                height: 20px;
                width: 14px;
                overflow: hidden;
                margin-left: 12px;
                & > div {
                  height: 20px;
                  width: 14px;
                  background-repeat: no-repeat;
                  background-position: center;
                  background-size: 100% 100%;
                }
                .arrow-up {
                  background-image: url('~@/assets/img/data/icon_arrow02.png');
                  animation: up 2s linear infinite;
                }
                .arrow-down {
                  background-image: url('~@/assets/img/data/icon_arrow01.png');
                  animation: down 2s linear infinite;
                }
              }
            }
            .val {
              font-family: douyv;
              font-size: 40px;
              color: #f4fdff;
            }
          }
        }
      }
      .status {
        display: flex;
        align-items: flex-start;
        .status-l {
          width: 374px;
          .devOpsData-value {
            height: 928px;
            width: 366px;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            background-image: url('~@/assets/img/energyStorage/bg_edsed01.png');
            overflow-y: auto;
            .devOpsData-item {
              width: 288px;
              height: 241px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              margin: 53px auto 0;
              text-align: center;
              padding-top: 132px;
              &:first-child {
                margin-top: 48px;
              }
              .label {
                color: #84d5ff;
                font-size: 24px;
              }
              .value {
                color: #ffffff;
                font-size: 38px;
                font-weight: bold;
                .unit {
                  font-size: 18px;
                  font-weight: 400;
                }
              }
            }
          }
        }
        .status-r {
          flex: 1;
          margin-left: 20px;
          .title-text {
            &:nth-child(3) {
              margin-top: 20px;
            }
          }
          .warnList-content {
            margin-top: 20px;
            .table-content {
              height: 490px;
              overflow-y: auto;
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
                    background-color: rgb(20, 21, 43);
                    background-size: 100% 100%;
                    background-repeat: no-repeat;
                    border: none;
                    margin-top: 0;
                    * {
                      color: #afbed8 !important;
                      font-size: 20px;
                    }
                  }
                  & > div {
                    min-width: 210px;
                    max-width: 210px;
                    display: flex;
                    align-items: center;
                    padding: 0 25px;
                    word-break: break-all;
                    &:first-child {
                      color: #afbed8;
                      min-width: 310px;
                      max-width: 310px;
                    }
                    &:nth-child(2) {
                      flex: 1;
                      min-width: 910px;
                      max-width: 910px;
                    }
                    em {
                      color: #a1b8cd;
                      font-size: 16px;
                    }
                  }
                }
              }
            }
          }
          .center-list {
            display: flex;
            flex-wrap: wrap;
            margin-top: 30px;

            gap: 30px 20px;
            .center-item {
              width: calc((100% - 80px) / 5);

              .value {
                width: 100%;
                height: 96px;
                line-height: 96px;
                background-size: 100% 100%;
                background-repeat: no-repeat;
                background-position: center;
                background-image: url('~@/assets/img/energyStorage/se_bg_battery04.png');
                text-align: center;
                color: #ffffff;
                font-size: 42px;
                font-weight: bold;
                .unit {
                  font-size: 24px;
                  font-weight: 400;
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
                  min-width: 144px;
                  text-align: center;
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
          .status-item {
            width: 462px;
            // margin-left: 150px;
            &:nth-child(4n-3) {
              margin-left: 0;
            }
            .status-icon {
              width: 462px;
              height: 220px;
              background-repeat: no-repeat;
              background-size: 100% 100%;
              background-position: center center;
            }
            .err {
              background-image: url('~@/assets/img/energyStorage/bg_energy_equipment06.png');
            }
            .normal {
              background-image: url('~@/assets/img/energyStorage/bg_energy_equipment05.png');
            }
            .notEnabled {
              background-image: url('~@/assets/img/energyStorage/bg_energy_equipment07.png');
            }
            .status-name {
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 30px;
              margin-top: 16px;
              color: #fff;
              .name {
                min-width: 240px;
                text-align: center;
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
          .mt {
            margin-top: 40px;
          }
        }
      }
    }
    .content2 {
      .top {
        display: flex;
        justify-content: space-between;
        .top-item {
          height: 185px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          padding-left: 204px;
          min-width: 448px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .label {
            color: #cde3f6;
            font-size: 20px;
            position: relative;
            .status-icon {
              display: inline-block;
              vertical-align: text-top;
              height: 21px;
              width: 21px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
            }
          }
          .value {
            color: #cde3f6;
            font-size: 40px;
            font-family: douyv;
            margin-top: 10px;
          }
        }
        .charge {
          background-image: url('~@/assets/img/energyStorage/se_bg_battery_01.png') !important;
          .status-icon {
            background-image: url('~@/assets/img/energyStorage/bg_battery_0111.png') !important;
          }
        }
        .discharge {
          background-image: url('~@/assets/img/energyStorage/se_bg_battery_13.png') !important;
          .status-icon {
            background-image: url('~@/assets/img/energyStorage/bg_battery_0122.png') !important;
          }
        }
        .fault {
          background-image: url('~@/assets/img/energyStorage/se_bg_battery_01.png') !important;
          .status-icon {
            background-image: url('~@/assets/img/energyStorage/bg_battery_0111.png') !important;
          }
        }
        .idle {
          background-image: url('~@/assets/img/energyStorage/se_bg_battery_12.png') !important;
          .status-icon {
            background-image: url('~@/assets/img/energyStorage/bg_battery_0133.png') !important;
          }
        }
      }
      .center {
        margin-top: 30px;
        .center-list {
          display: flex;
          flex-wrap: wrap;
          justify-content: space-between;
          margin-top: 30px;
          .center-item {
            min-width: 434px;
            margin-top: 50px;

            .value {
              width: 434px;
              height: 96px;
              line-height: 96px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              background-position: center;
              background-image: url('~@/assets/img/energyStorage/se_bg_battery04.png');
              text-align: center;
              color: #ffffff;
              font-size: 42px;
              font-weight: bold;
              .unit {
                font-size: 24px;
                font-weight: 400;
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
                min-width: 144px;
                text-align: center;
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
          .mt0 {
            margin-top: 0;
          }
        }
      }
      .bottom {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-top: 58px;
        .cl {
          width: 1620px;
          .chart-content {
            height: 500px;
          }
        }
        .cr {
          flex: 1;
          .cr-list {
            width: 100%;
            margin-top: 30px;
            overflow: hidden;
            height: 472px;
            display: flex;
            overflow-y: auto;
            justify-content: flex-end;
            ul {
              width: 644px;
              li {
                min-height: 80px;
                width: 644px;
                background-repeat: no-repeat;
                background-size: 100% 100%;
                background-position: center;
                background-image: url('~@/assets/img/energyStorage/bg_energy01.png');
                margin-top: 18px;
                padding-left: 87px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                &:first-child {
                  margin-top: 0px;
                }
                &:nth-child(1) {
                  animation: slideInLeft 0.2s linear;
                }
                &:nth-child(2) {
                  animation: slideInLeft 0.3s linear;
                }
                &:nth-child(3) {
                  animation: slideInLeft 0.4s linear;
                }
                &:nth-child(4) {
                  animation: slideInLeft 0.5s linear;
                }
                &:nth-child(5) {
                  animation: slideInLeft 0.6s linear;
                }
                .title {
                  font-size: 21px;
                  line-height: 82px;
                  color: #fff;
                  width: 260px;
                }
                .value {
                  font-size: 36px;
                  font-weight: bold;
                  text-align: center;
                  width: 400px;
                  background: linear-gradient(0deg, #ffbfbf 0%, rgba(255, 255, 255, 0.87) 100%);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
