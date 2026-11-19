<template>
  <div class="fbsgf-clear view">
    <MyHeader></MyHeader>
    <div :class="`clear-robot robot-${index + 1}`" v-for="(item, index) in 5" :key="index">
      <div :class="['robot-icon', conputedRobot(index), working ? 'working' : '']"></div>
    </div>
    <!-- <div class="robot-group">
      <div class="robot-name" v-for="(item, index) in 6" :key="index">
        <span></span><span>设备{{ index + 1 }}</span>
      </div>
    </div> -->
    <div class="view-container">
      <div class="l">
        <TextTittle :label="dataView.label || ''"></TextTittle>
        <div class="dataView-content">
          <div class="dataView-item" v-for="(item, index) in dataView.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `/${item.unit}` : '' }}</div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
        <!-- <TextTittle :label="ltOffLineEquipmentData.label || ''"></TextTittle>
        <div class="no-data" v-if="!ltOffLineEquipmentData.childs?.length"></div>
        <div class="blowerInfo-content" v-if="ltOffLineEquipmentData.childs?.length">
          <div class="scroll">
            <div class="blowerInfo-item normal" v-for="(item, index) in ltOffLineEquipmentData.childs || []" :key="index">
              <div class="ltOffLineEquipmentData-icon"></div>
              <div class="ltOffLineEquipmentData-values">
                <div class="blowerInfo-row" v-for="(item1, index1) in item.childs || []" :key="index1">
                  <div class="label">{{ item1.label }}</div>
                  <div class="value">
                    <span>
                      {{ item1.value }}<span class="unit" v-if="item1.unit">{{ item1.unit }} </span>
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div> -->
      </div>
      <div class="r">
        <TextTittle :label="onlineEquipmentData.label || ''"></TextTittle>
        <div class="no-data" v-if="!onlineEquipmentData.childs?.length"></div>
        <div class="onlineEquipmentData-content" v-if="onlineEquipmentData.childs?.length">
          <div class="onlineEquipmentData-scroll">
            <div class="onlineEquipmentData-item" :class="{ active: index == active }" v-for="(item, index) in onlineEquipmentData.childs || []" :key="index" @click="handleCheck(item, index)">
              <div class="onlineEquipmentData-icon"></div>
              <div class="onlineEquipmentData-name" v-if="item.childs?.length">
                {{ item.childs[0]?.value || '' }}
              </div>
            </div>
          </div>
        </div>
        <div class="equipmentDetail-content">
          <div class="equipmentDetail-item" v-for="(item, index) in equipmentDetail || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `/${item.unit}` : '' }}</div>
            <div class="value" :class="`equipmentDetail-value-${index} ${computedScroll('.equipmentDetail-value-' + index) ? 'needScroll' : ''}`" :key="`${valueKey}${index}`">
              <div>{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { hfqxGlobalView, detailEquipment } from '@/api/hfqx'
export default {
  name: 'clear',
  data() {
    return {
      dataView: {},
      ltOffLineEquipmentData: {},
      onlineEquipmentData: {},
      equipmentDetail: [],

      scrollTimer: null,
      scrollTopNum: 0,

      onlineScroll: null,
      onlineTopNum: 0,

      active: 0,
      valueKey: 0,

      robot1: 'robot-run',
      robot2: 'robot-run',
      robot3: 'robot-run',
      robot4: 'robot-run',
      robot5: 'robot-run',
      robot6: 'robot-run',

      refreshTime: null,
    }
  },
  created() {
    this.initData()
    clearInterval(this.refreshTime)
    this.refreshTime = null
    this.refreshTime = setInterval(() => {
      this.initData()
    }, 60 * 1000)
  },
  watch: {
    robot1() {
      if (this.robot1 == 'robot-stop') {
        this.robotsTimeout('robot1', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot1', 4000, 'robot-stop')
      }
    },
    robot2() {
      if (this.robot2 == 'robot-stop') {
        this.robotsTimeout('robot2', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot2', 8000, 'robot-stop')
      }
    },
    robot3() {
      if (this.robot3 == 'robot-stop') {
        this.robotsTimeout('robot3', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot3', 4000, 'robot-stop')
      }
    },
    robot4() {
      if (this.robot4 == 'robot-stop') {
        this.robotsTimeout('robot4', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot4', 14000, 'robot-stop')
      }
    },
    robot5() {
      if (this.robot5 == 'robot-stop') {
        this.robotsTimeout('robot5', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot5', 14000, 'robot-stop')
      }
    },
    robot6() {
      if (this.robot6 == 'robot-stop') {
        this.robotsTimeout('robot6', 1000, 'robot-run')
      } else {
        this.robotsTimeout('robot6', 6000, 'robot-stop')
      }
    },
  },
  computed: {
    conputedRobot() {
      return (val) => {
        let keys = ['robot1', 'robot2', 'robot3', 'robot4', 'robot5', 'robot6']
        return this[keys[val]]
      }
    },
    computedScroll() {
      return (p) => {
        let pw = document.querySelector(p)?.offsetWidth
        let cw = document.querySelector(`${p} > div`)?.offsetWidth
        return cw > pw
      }
    },
    working() {
      return (
        this.onlineEquipmentData.childs?.length &&
        this.equipmentDetail.length &&
        this.equipmentDetail.find((item) => {
          return item.value == '工作中' && item.label == '工作状态'
        })
      )
    },
  },
  mounted() {
    this.robotsTimeout('robot1', 4000, 'robot-stop')
    this.robotsTimeout('robot2', 8000, 'robot-stop')
    this.robotsTimeout('robot3', 4000, 'robot-stop')
    this.robotsTimeout('robot4', 14000, 'robot-stop')
    this.robotsTimeout('robot5', 14000, 'robot-stop')
    this.robotsTimeout('robot6', 6000, 'robot-stop')
  },
  beforeUpdate() {
    this.$nextTick(() => {
      this.roomdetailscroll('.blowerInfo-content', '.scroll', 'scrollTimer', 'scrollTopNum')
      this.roomdetailscroll('.onlineEquipmentData-content', '.onlineEquipmentData-scroll', 'onlineScroll', 'onlineTopNum')
    })
    this.handleScroll()
  },
  methods: {
    robotsTimeout(key, time, status) {
      setTimeout(() => {
        this[key] = status
      }, time)
    },
    handleScroll() {
      let tableContent = document.querySelector('.blowerInfo-content')
      if (tableContent) {
        tableContent.addEventListener('scroll', () => {
          this.scrollTopNum = tableContent.scrollTop
          this.roomdetailscroll('.blowerInfo-content', '.scroll', 'scrollTimer', 'scrollTopNum')
        })
      }

      let onlineEquipmentData = document.querySelector('.onlineEquipmentData-content')
      if (onlineEquipmentData) {
        onlineEquipmentData.addEventListener('scroll', () => {
          this.onlineTopNum = onlineEquipmentData.scrollTop
          this.roomdetailscroll('.onlineEquipmentData-content', '.onlineEquipmentData-scroll', 'onlineScroll', 'onlineTopNum')
        })
      }
    },
    handleCheck(item, index) {
      this.active = index
      this.getDetail(item.childs[1].value)
    },
    initData() {
      hfqxGlobalView().then((res) => {
        let data = res.data
        this.dataView = data.dataView || {}
        this.ltOffLineEquipmentData = data.ltOffLineEquipmentData || {}
        this.onlineEquipmentData = data.onlineEquipmentData || {}
        if (this.onlineEquipmentData.childs?.length) {
          this.getDetail(this.onlineEquipmentData.childs[0].childs[1].value)
        }
      })
    },
    getDetail(deviceNumber) {
      detailEquipment({ deviceNumber: deviceNumber }).then((res) => {
        let data = res.data

        this.equipmentDetail = data.equipmentDetail || []
        this.$nextTick(() => {
          this.valueKey++
        })
      })
    },
    /**
     * 控制房间详情滚动条的滚动。
     * @param {string} p 查询选择器，用于获取需要滚动的表格内容区域。
     * @param {string} c 查询选择器，用于获取表格外部容器。
     * @param {string} timerKey 用于存储定时器的键名，确保定时器能够被正确清除。
     * @param {string} topNumKey 用于存储滚动位置的键名。
     */

    roomdetailscroll(p, c, timerKey, topNumKey) {
      // 尝试获取表格内容区域和外部容器
      let tableContent = document.querySelector(p)
      let tableScroll = document.querySelector(c)
      // 如果任一元素未找到，则直接返回
      if (!tableContent || !tableScroll) return

      // 如果已存在定时器，则清除旧的定时器
      if (this[timerKey]) {
        clearInterval(this[timerKey])
      }

      // 设置定时器，持续滚动表格内容
      this[timerKey] = setInterval(() => {
        // 当滚动到接近底部时，重置滚动位置到顶部
        if (this[topNumKey] > tableScroll.clientHeight - tableContent.clientHeight) {
          this[topNumKey] = 0
        }
        // 逐渐增加滚动位置，实现平滑滚动效果
        this[topNumKey] += 1
        tableContent.scrollTo({ top: this[topNumKey], left: 0, behavior: 'smooth' })
      }, 100)
    },
  },
  beforeDestroy() {
    clearInterval(this.scrollTimer)
    clearInterval(this.onlineScroll)
    clearInterval(this.refreshTime)
  },
}
</script>
<style lang="scss" scoped>
.fbsgf-clear {
  background-image: url('~@/assets/img/fbsgf/bg_sp018.png');
  position: relative;
  @keyframes robot-1 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(-30px, -20px);
    }
  }
  @keyframes robot-2 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(168px, -70px);
    }
  }
  @keyframes robot-3 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(-30px, -20px);
    }
  }
  @keyframes robot-4 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(-270px, -200px);
    }
  }
  @keyframes robot-5 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(-280px, -200px);
    }
  }
  @keyframes robot-6 {
    0% {
      transform: translate(0, 0);
    }
    100% {
      transform: translate(120px, -50px);
    }
  }
  @keyframes valueScroll {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(-100%);
    }
  }
  .clear-robot {
    position: absolute;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    .robot-icon {
      background-size: 100% 100%;
      background-repeat: no-repeat;
      width: 43px;
      height: 42px;
    }
  }
  .robot-1 {
    left: 887px;
    top: 234px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse013.png');
    width: 137px;
    height: 73px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse014.png');
      margin-top: 20px;
      margin-left: 60px;
    }
    .working {
      animation: robot-1 4s linear infinite alternate;
      animation-fill-mode: forwards;
    }
  }
  .robot-2 {
    left: 673px;
    top: 257px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse011.png');
    width: 223px;
    height: 93px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse012.png');
      margin-top: 50px;
      margin-left: 2px;
    }
    .working {
      animation: robot-2 8s linear infinite alternate;
    }
  }
  .robot-3 {
    left: 597px;
    top: 342px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse009.png');
    width: 137px;
    height: 73px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse010.png');
      margin-top: 20px;
      margin-left: 60px;
    }
    .working {
      animation: robot-3 4s linear infinite alternate;
    }
  }
  .robot-4 {
    left: 681px;
    top: 555px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse07.png');
    width: 370px;
    height: 253px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse08.png');
      margin-top: 200px;
      margin-left: 300px;
      height: 62px;
      width: 62px;
    }
    .working {
      animation: robot-4 14s linear infinite alternate;
    }
  }
  .robot-5 {
    left: 592px;
    top: 572px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse05.png');
    width: 401px;
    height: 282px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse06.png');
      margin-top: 210px;
      margin-left: 310px;
      height: 62px;
      width: 62px;
    }
    .working {
      animation: robot-5 14s linear infinite alternate;
    }
  }
  .robot-6 {
    left: 1155px;
    top: 487px;
    background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse015.png');
    width: 227px;
    height: 112px;
    .robot-icon {
      background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse016.png');
      margin-top: 50px;
      margin-left: 30px;
      height: 52px;
      width: 52px;
    }
    .working {
      animation: robot-6 6s linear infinite alternate;
    }
  }
  .robot-stop {
    animation-play-state: paused !important;
  }
  .robot-run {
    animation-play-state: running !important;
  }
  .robot-group {
    position: absolute;
    top: 906px;
    left: 1313px;
    .robot-name {
      font-size: 12px;
      color: #eef3ff;
      span {
        vertical-align: middle;
        &:first-child {
          border-radius: 5px;
          margin-right: 8px;
          width: 22px;
          height: 12px;
          display: inline-block;
        }
      }
      &:nth-child(1) {
        span {
          &:first-child {
            background: rgba(255, 144, 0, 0.22);
            border: 1px solid #ff7200;
          }
        }
      }
      &:nth-child(2) {
        span {
          &:first-child {
            background: rgba(178, 20, 218, 0.22);
            border: 1px solid #b214da;
          }
        }
      }
      &:nth-child(3) {
        span {
          &:first-child {
            background: rgba(10, 223, 255, 0.22);
            border: 1px solid #0adfff;
          }
        }
      }
      &:nth-child(4) {
        span {
          &:first-child {
            background: rgba(0, 0, 255, 0.22);
            border: 1px solid #0000ff;
          }
        }
      }
      &:nth-child(5) {
        span {
          &:first-child {
            background: rgba(213, 244, 38, 0.22);
            border: 1px solid #d5f426;
          }
        }
      }
      &:nth-child(6) {
        span {
          &:first-child {
            background: rgba(0, 255, 108, 0.22);
            border: 1px solid #00ff6c;
          }
        }
      }
    }
  }
  .view-container {
    position: relative;
    .no-data {
      width: 360px;
      height: 284px;
      margin: 0 auto;
      margin-top: 60px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/fbsgf/clear/icon_default.png');
      transform: scale(0.7);
    }
    .l {
      width: 420px;
      .title-comtainer {
        &:nth-child(3) {
          margin-top: 30px;
        }
      }
      .dataView-content {
        margin-top: 10px;
        overflow: hidden;
        .dataView-item {
          width: 420px;
          height: 61px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse01.png');
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
            width: 220px;
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
      .blowerInfo-content {
        display: flex;
        flex-wrap: wrap;
        gap: 29px 40px;
        margin-top: 10px;
        height: 580px;
        overflow-y: auto;
        .blowerInfo-item {
          width: 434px;
          height: 170px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          //   padding: 0 34px 0 0;
          display: flex;
          align-items: center;
          background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse03.png');
          background-repeat: no-repeat;
          background-size: 100% 100%;
          &:first-child {
            margin-top: 0;
          }
          .ltOffLineEquipmentData-icon {
            background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse02.png');
            background-repeat: no-repeat;
            background-size: 100% 100%;
            height: 142px;
            width: 142px;
            margin-left: 27px;
          }
          .ltOffLineEquipmentData-values {
            min-height: 170px;
            width: calc(100% - 169px);
            padding: 0 30px 0 20px;
            margin-top: 30px;
            .blowerInfo-row {
              display: flex;
              align-items: center;
              justify-content: space-between;
              min-height: 44px;
              .label {
                color: #fff;
                font-size: 16px;
                width: 70px;
                line-height: 23px;
              }
              .value {
                font-weight: bold;
                font-size: 18px;
                width: calc(100% - 70px);
                text-align: right;
                word-break: break-all;
                line-height: 23px;
                & > span {
                  background: linear-gradient(0deg, #4bbefa 0%, #fff);
                  -webkit-background-clip: text;
                  -webkit-text-fill-color: transparent;
                }
                .unit {
                  font-size: 16px;
                }
              }
            }
          }
        }
      }
    }
    .r {
      position: absolute;
      right: 0;
      top: 0;
      .onlineEquipmentData-content {
        margin-top: 20px;
        width: 420px;
        max-height: 240px;
        overflow: auto;
        .onlineEquipmentData-scroll {
          display: flex;
          flex-wrap: wrap;
          gap: 10px 10px;
        }
        .onlineEquipmentData-item {
          width: calc((100% - 20px) / 3);
          cursor: pointer;
          display: flex;
          flex-direction: column;
          align-items: center;
          .onlineEquipmentData-icon {
            width: 79px;
            height: 90px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse04.png');
          }
          .onlineEquipmentData-name {
            font-weight: bold;
            font-size: 14px;
            color: #ffffff;
            text-align: center;
          }
        }
        .active {
          .onlineEquipmentData-icon {
            background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse19.png');
          }
        }
      }
      .equipmentDetail-content {
        margin-top: 10px;
        overflow: hidden;
        .equipmentDetail-item {
          width: 420px;
          height: 61px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fbsgf/clear/bg_cleanse01.png');
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
            width: 180px;
            text-align: center;
            white-space: nowrap;
            overflow: hidden;
            margin-right: 20px;
            & > div {
              font-size: 22px;
              font-weight: bold;
              display: inline-block;
              background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
          .needScroll {
            & > div {
              animation: valueScroll 10s linear infinite;
            }
          }
        }
      }
    }
  }
}
</style>
