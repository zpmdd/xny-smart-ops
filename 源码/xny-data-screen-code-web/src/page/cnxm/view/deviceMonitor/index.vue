<template>
  <div class="cnxm-deviceMonitor view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="l">
        <TextTittle :label="storageInfoMonitor?.label || ''"></TextTittle>
        <div class="storageInfoMonitor-content">
          <div class="storageInfoMonitor-item" v-for="(item, index) in storageInfoMonitor.childs || []" :key="index" :style="{ backgroundImage: `url(${projectIcons.cnxm.storageInfoMonitor[index]})` }">
            <div class="label">{{ item.label }}</div>
            <!-- <div class="value">{{ item.value }}{{ item.unit ? item.unit : '' }}</div> -->
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div class="r">
        <TextTittle :label="devOpsData?.label || ''"></TextTittle>
        <div class="devOpsData-content">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
        <!-- <TextTittle :label="storageStatusMonitor?.label || ''"></TextTittle>
        <div class="storageStatusMonitor-content">
          <div class="storageStatusMonitor-item" v-for="(item, index) in storageStatusMonitor.childs || []" :key="index">
            <div class="status-icon" :class="{ err: item.value == '异常', normal: item.value == '正常', notEnabled: item.value == '未启用' }"></div>
            <div class="label">
              <div class="l-icon"></div>
              <div class="name">{{ item.label }}</div>
              <div class="r-icon"></div>
            </div>
          </div>
        </div> -->
        <TextTittle :label="runningMonitor.label || ''"></TextTittle>
        <div class="runningMonitor-content">
          <div class="runningMonitor-item" v-for="(item, index) in runningMonitor.childs || []" :key="index">
            <div class="value">
              {{ item.value | formatNum }} <span class="unit" v-if="item.unit">{{ item.unit || '' }}</span>
            </div>
            <div class="label">
              <div class="l-icon"></div>
              <div class="name">{{ item.label }}</div>
              <div class="r-icon"></div>
            </div>
          </div>
        </div>

        <TextTittle :label="'异常状态分析'"></TextTittle>
        <div class="bottom-content">
          <div class="table-content">
            <div class="scroll">
              <div v-for="(item, index) in warnList || []" :key="index">
                <div v-for="(item1, index1) in item" :key="index1">
                  <em>{{ item1 }}</em>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="no-warn" v-if="warnList.length <= 1"></div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { newDeviceMonitor } from '@/api/cnxm'
export default {
  data() {
    return {
      projectIcons: icons,

      // 运维数据
      devOpsData: {},
      // 储能信息监测
      storageInfoMonitor: {},
      // 储能状态监视
      storageStatusMonitor: {},
      runningMonitor: {},
      warnList: [],
      scrollTimer: null,
      scrollTopNum: 0,
    }
  },
  created() {
    this.initData()
  },
  mounted() {
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
        if (this.warnList.length <= 1) {
          return
        }
        if (this.scrollTopNum > tableScroll.clientHeight - tableContent.clientHeight) {
          this.scrollTopNum = 0
        }
        this.scrollTopNum += 1
        tableContent.scrollTo({ top: this.scrollTopNum, left: tableContent.scrollLeft, behavior: 'smooth' })
      }, 100)
    },
    initData() {
      newDeviceMonitor().then((res) => {
        let data = res.data
        this.devOpsData = data.devOpsData || {}
        this.runningMonitor = data.runningMonitor || {}
        this.storageInfoMonitor = data.storageInfoMonitor || {}
        this.warnList = data.warnList || []
      })
    },
  },
  beforeDestroy() {
    clearInterval(this.times)
    clearInterval(this.scrollTimer)
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.cnxm-deviceMonitor {
  .view-container {
    display: flex;
    justify-content: space-between;
    .l {
      width: 350px;
      .title-comtainer {
        width: 350px;
      }
      .storageInfoMonitor-content {
        width: 346px;
        margin-top: 20px;
        .storageInfoMonitor-item {
          height: 136px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-top: 32px;
          padding-left: 150px;
          padding-top: 34px;
          &:first-child {
            margin-top: 0;
          }
          .label {
            color: #cde3f6;
            font-size: 16px;
          }
          .value {
            color: #f4fdff;
            font-size: 32px;
            font-family: douyv;
            width: 180px;
            word-break: break-all;
          }
        }
      }
    }
    .r {
      width: 1490px;
      .devOpsData-content {
        margin-top: 20px;
        margin-bottom: 20px;
        display: flex;
        flex-wrap: wrap;
        gap: 10px 60px;
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
      .no-warn {
        width: 360px;
        height: 284px;
        margin: 0 auto;
        margin-top: -220px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/fbsgf/clear/icon_default.png');
        transform: scale(0.8);
      }
      .bottom-content {
        overflow: auto;

        .table-content {
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fbsgf/bg_pim05.png');
          background-size: 100% 104px;
          background-position: -21px 334px;
          height: 330px;
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
              }
            }
          }
        }
      }
      .runningMonitor-content {
        margin-top: 10px;
        margin-bottom: 25px;
        display: flex;
        flex-wrap: wrap;
        gap: 25px 30px;
        .runningMonitor-item {
          width: calc((100% - 120px) / 5);
          .value {
            min-height: 78px;
            text-align: center;
            padding-top: 12px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/cnxm/bg_esbm04.png');
            font-size: 34px;
            color: #fff;
            .unit {
              font-size: 18px;
            }
          }
          .label {
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 19px;
            margin-top: 10px;
            color: #fff;
            .name {
              text-align: center;
              padding: 0 10px;
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
              background-image: url('~@/assets/img/common/bg_energy_equipment08.png');
            }
            .r-icon {
              background-image: url('~@/assets/img/common/bg_energy_equipment0509.png');
            }
          }
        }
      }
      .storageStatusMonitor-content {
        margin-top: 16px;
        display: flex;
        flex-wrap: wrap;
        gap: 26px 23px;
        .storageStatusMonitor-item {
          width: 354px;
          .status-icon {
            height: 169px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .err {
            background-image: url('~@/assets/img/cnxm/bg_esem04.png');
          }
          .normal {
            background-image: url('~@/assets/img/cnxm/bg_esem03.png');
          }
          .notEnabled {
            background-image: url('~@/assets/img/cnxm/bg_esem05.png');
          }
          .label {
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 19px;
            margin-top: 12px;
            color: #fff;
            .name {
              text-align: center;
              padding: 0 10px;
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
              background-image: url('~@/assets/img/common/bg_energy_equipment08.png');
            }
            .r-icon {
              background-image: url('~@/assets/img/common/bg_energy_equipment0509.png');
            }
          }
        }
      }
    }
  }
}
</style>
