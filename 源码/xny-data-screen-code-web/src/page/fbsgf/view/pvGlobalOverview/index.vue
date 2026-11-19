<template>
  <div class="fbsgf-pvGlobalOverview view">
    <MyHeader></MyHeader>
    <div class="device-item" :class="'device-item' + index" v-for="(item, index) in deviceList || []" :key="index">
      <div class="deviceName">{{ item.deviceName }}</div>
      <div class="values" v-for="(item1, index1) in item.otherData?.childs || []" :key="index1">
        <span class="label">{{ item1.label }}</span>
        <span class="value">{{ item1.value }}</span>
      </div>
    </div>
    <div class="clear-icon" @click="handleClearBtn"></div>
    <div class="view-container">
      <div class="l">
        <TextTittle :label="dataView.label || ''"></TextTittle>
        <div class="dataView-content">
          <div class="dataView-item" v-for="(item, index) in dataView.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
        <div class="otherStatistics-content">
          <div class="otherStatistics-item" v-for="(item, index) in otherStatistics.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="devOpsData.label || ''"></TextTittle>
        <div class="devOpsData-content">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs || []" :key="index">
            <div class="value">
              <span>{{ item.value }}</span>
            </div>
            <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
          </div>
        </div>
        <TextTittle :label="socialBenefit.label || ''"></TextTittle>
        <div class="socialBenefit-content">
          <div class="socialBenefit-item" v-for="(item, index) in socialBenefit.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.country.socialBenefit[index]})` }"></div>
            <div class="values">
              <div class="label" v-html="item.label"></div>
              <div class="value">
                {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="r">
        <TextTittle :label="chart1Data.label || ''"></TextTittle>
        <div class="chart1-content" ref="chart1"></div>
        <div class="chart2-content" ref="chart2"></div>
        <!-- <TextTittle :label="chart3Data.label || ''"></TextTittle>
        <div class="chart3-content" ref="chart3"></div>
        <div class="chart4-content" ref="chart4"></div> -->
      </div>
    </div>
    <div class="envDataToday-content">
      <div class="envDataToday-item" v-for="(item, index) in envDataToday.childs || []" :key="index">
        <div class="icon" :style="{ backgroundImage: `url(${projectIcons.fbsgf.envDataToday[index + 1]})` }"></div>
        <div class="values">
          <div class="label" v-html="item.label"></div>
          <div class="value">
            {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="router-cnxm" @click="handleRouter(1)" v-if="roles.includes('10300')">储能项目</div>
    <div class="router-cdxm" @click="handleRouter(2)" v-if="roles.includes('10400')">充电项目</div>
    <div class="router-video" @click="handleRouter(3)">智能监控</div>
    <Dialog :visible="videoShow" @close="handleDialog">
      <div class="video-content">
        <div class="close-icon" @click="handleDialog"></div>
        <div id="ezuikit-player"></div>
      </div>
    </Dialog>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { pvGlobalOverview, getPlayInfo } from '@/api/fbsgf.js'
import { getPermsTree } from '@/api/user'
import { mapState } from 'vuex'
import Dialog from '@/components/Dialog'

const videoSdks = require.context('@/static', false, /^\.\/ezuikit\.js$/)

export default {
  name: 'pvGlobalOverview',
  data() {
    return {
      projectIcons: icons,
      // 数据一览
      dataView: {},
      // 运维数据
      devOpsData: {},
      // 设备列表
      deviceList: [],
      // 今日环境数据
      envDataToday: {},
      // 其他数据
      otherStatistics: {},
      // 社会效益
      socialBenefit: {},
      chart1Dom: null,
      chart1Data: {},
      chart2Dom: null,
      chart2Data: {},
      chart3Dom: null,
      chart3Data: {},
      chart4Dom: null,
      chart4Data: {},

      roles: [],

      videoShow: false,

      player: null,
      accessToken: '',
      playerUrl: '',
    }
  },
  computed: {
    ...mapState({
      loginId: (state) => state.user.loginId,
    }),
  },
  async created() {
    await this.getPermsTree()
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
    handleDialog() {
      this.videoShow = false
    },
    handleClearBtn() {
      this.$router.push('/fbsgf/clear')
    },
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
    destroy() {
      try {
        let destroyPromise = this.player.destroy()
        destroyPromise.then((data) => {
          console.log('promise 获取 数据', data)
        })
      } catch (error) {}
      this.player = null
    },
    handleRouter(flag) {
      if (flag == 1) {
        this.$router.push('/cnxm/deviceMonitor')
      }
      if (flag == 2) {
        this.$router.push('/cdxm/chargingStation')
      }
      if (flag == 3) {
        if (!videoSdks.keys().includes('./ezuikit.js')) {
          this.$message.warning('视频 SDK 尚未配置')
          return
        }
        const sdkModule = videoSdks('./ezuikit.js')
        const EZUIKit = sdkModule.default || sdkModule
        if (this.player) {
          this.destroy()
        }
        getPlayInfo().then((res) => {
          let data = res.data
          this.playerUrl = data.url
          this.accessToken = data.token
          this.videoShow = true
          this.$nextTick(() => {
            let _that = this
            this.player = new EZUIKit.EZUIKitPlayer({
              id: `ezuikit-player`,
              url: _that.playerUrl,
              accessToken: _that.accessToken,
              // simple: 极简版; pcLive: pc直播; pcRec: pc回放; mobileLive: 移动端直播; mobileRec: 移动端回放;security: 安防版; voice: 语音版;
              template: 'pcLive',
              // width: 755,
              // height:430,
            })
            try {
              // this.$nextTick(() => {
                
              // })
              setTimeout(() => {
                this.player.play()
              }, 1000);
            } catch (error) {}
          })
        })
      }
    },
    initData(needInitChart) {
      pvGlobalOverview().then((res) => {
        let data = res.data
        this.dataView = data.dataView || {}
        this.devOpsData = data.devOpsData || {}
        this.deviceList = data.deviceList || []
        this.envDataToday = data.envDataToday || {}
        this.otherStatistics = data.otherStatistics || {}
        this.socialBenefit = data.socialBenefit || {}
        this.chart1Data = data.chartList[0]
        this.chart1Data.childs[0].style = '0'
        this.chart1Data.childs[0].color = '50, 195, 104'
        this.chart2Data = data.chartList[1]
        try {
          this.chart2Data.childs[0].style = '1'
          this.chart2Data.childs[0].color = '126, 184, 255'
        } catch (error) {}
        this.chart3Data = data.chartList[2]
        this.chart3Data.childs[0].style = '0'
        this.chart3Data.childs[0].color = '29, 210, 255'
        this.chart4Data = data.chartList[3]
        try {
          this.chart4Data.childs[0].style = '1'
          this.chart4Data.childs[0].color = '37, 161, 115'
        } catch (error) {}
        try {
          this.chart4Data.childs[1].style = '1'
          this.chart4Data.childs[1].color = '0, 174, 255'
        } catch (error) {}
        this.$nextTick(() => {
          if (!needInitChart) {
            this.chart1Dom = this.$echarts.init(this.$refs.chart1, null, { renderer: 'svg' })
            this.chart2Dom = this.$echarts.init(this.$refs.chart2, null, { renderer: 'svg' })
            // this.chart3Dom = this.$echarts.init(this.$refs.chart3, null, { renderer: 'svg' })
            // this.chart4Dom = this.$echarts.init(this.$refs.chart4, null, { renderer: 'svg' })
          }
          this.initChart()
        })
      })
    },
    initChart() {
      this.$nextTick(() => {
        let chart1Options = {
          data: this.chart1Data,
        }
        this.chart1Dom.setOption(this.$initChart(chart1Options))
        this.chart1Dom.resize()
        let chart2Options = {
          data: this.chart2Data,
          needStart: true,
          needShadow: false,
          smooth: false,
          vm: this,
        }
        this.chart2Dom.setOption(this.$initChart(chart2Options))
        this.chart2Dom.resize()
        // let chart3Options = {
        //   data: this.chart3Data,
        // }
        // this.chart3Dom.setOption(this.$initChart(chart3Options))
        // this.chart3Dom.resize()
        // let chart4Options = {
        //   data: this.chart4Data,
        //   needStart: true,
        //   needShadow: false,
        //   smooth: false,
        //   vm: this,
        // }
        // this.chart4Dom.setOption(this.$initChart(chart4Options))
        // this.chart4Dom.resize()
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('onresize')
    this.$bus.$off('refreshData')
    this.destroy()
  },
  components: {
    Dialog,
  },
}
</script>
<style lang="scss" scoped>
.fbsgf-pvGlobalOverview {
  background-image: url('~@/assets/img/fbsgf/bg_sp018.png');
  position: relative;
  .clear-icon {
    position: absolute;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/fbsgf/icon_cleanse.png');
    height: 92px;
    width: 92px;
    left: 667px;
    top: 527px;
    cursor: pointer;
    z-index: 2020;
  }
  .video-content {
    width: 853px;
    height: 514px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/fbsgf/bg_mosan_popup0.png');
    padding-left: 64px;
    padding-top: 50px;
    #ezuikit-player {
      margin-left: 64px;
      margin-top: 50px;
    }
    .close-icon {
      height: 30px;
      width: 30px;
      position: absolute;
      right: 26px;
      top: 16px;
      cursor: pointer;
    }
  }
  .device-item {
    position: absolute;
    min-height: 104px;
    width: 167px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    padding: 18px;
    z-index: 2018;
    background-image: url('~@/assets/img/fbsgf/bg_pgo066.png');
    &::after {
      content: '';
      position: absolute;
      width: 93px;
      height: 71px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/fbsgf/bg_pgo067.png');
      top: 20px;
      right: -70px;
    }
    .deviceName {
      font-size: 16px;
      color: #fff;
    }
    .values {
      font-size: 16px;
      color: #eff0f1;
      word-break: break-all;
    }
  }
  .device-item0 {
    top: 260px;
    left: 510px;
  }
  .device-item1 {
    top: 190px;
    left: 980px;
    &::after {
      background-image: url('~@/assets/img/fbsgf/bg_pgo068.png');
      top: 30px;
      right: 150px;
    }
  }
  .device-item2 {
    top: 650px;
    left: 955px;
    &::after {
      background-image: url('~@/assets/img/fbsgf/bg_pgo068.png');
      top: 30px;
      right: 150px;
    }
  }
  .device-item3 {
    top: 540px;
    left: 880px;
    &::after {
      background-image: url('~@/assets/img/fbsgf/bg_pgo068.png');
      top: 30px;
      right: 150px;
    }
  }
  .device-item4 {
    top: 740px;
    left: 680px;
  }
  .device-item5 {
    top: 640px;
    left: 560px;
  }
  .device-item6 {
    top: 550px;
    left: 440px;
  }
  .device-item7 {
    top: 460px;
    left: 1020px;
  }
  .view-container {
    position: relative;
    .l {
      .title-comtainer {
        &:nth-child(4) {
          margin-top: 10px;
        }
        &:nth-child(6) {
          margin-top: 14px;
        }
      }
      width: 420px;
      .dataView-content {
        margin-top: 10px;
        overflow: hidden;
        .dataView-item {
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
      .otherStatistics-content {
        display: flex;
        flex-wrap: wrap;
        .otherStatistics-item {
          width: 210px;
          height: 101px;
          padding-top: 20px;
          text-align: center;
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-image: url('~@/assets/img/common/bg_nationwide05.png');
          .label {
            color: #00ddff;
            font-size: 16px;
          }
          .value {
            margin-top: 10px;
            font-size: 24px;
            font-weight: bold;
            background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
      }
      .devOpsData-content {
        display: flex;
        // justify-content: space-around;
        text-align: center;
        margin-top: 14px;
        .devOpsData-item {
          .label {
            font-size: 16px;
            color: #fff;
            margin-top: -4px;
          }
          .value {
            text-align: center;
            width: 120px;
            min-height: 48px;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/common/icon_nationwide13.png');
            background-position-x: 6px;
            background-position-y: 100%;
            span {
              font-size: 24px;
              font-weight: bold;
              color: #49c5ff;
              text-shadow: 0px 6px 0px rgba(43, 159, 214, 0.2);
              background: linear-gradient(0deg, #c2d6e2 0%, #fff);
              -webkit-background-clip: text;
              -webkit-text-fill-color: transparent;
            }
          }
        }
      }
      .socialBenefit-content {
        display: flex;
        flex-wrap: wrap;
        // padding-left: 16px;
        margin-top: 20px;
        justify-content: space-around;
        .socialBenefit-item {
          display: flex;
          .icon {
            width: 53px;
            height: 54px;
            background-repeat: no-repeat;
            background-size: 100% 100%;
            margin-right: 7px;
          }
          .values {
            width: 85px;
            .label {
              color: #fff;
              font-size: 14px;
              background: linear-gradient(90deg, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0));
              border-left: 2px solid #80c5f0;
              padding: 0 6px;
            }
            .value {
              color: #86d9ff;
              font-size: 24px;
              font-weight: bold;
              .unit {
                font-size: 14px;
                font-weight: 400;
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
      .chart1-content {
        height: 210px;
      }
      .chart2-content {
        height: 210px;
      }
      .chart3-content {
        height: 210px;
      }
      .chart4-content {
        height: 210px;
      }
    }
  }
  .envDataToday-content {
    position: absolute;
    display: flex;
    flex-wrap: wrap;
    bottom: 43px;
    left: 500px;
    padding: 22px 0 17px;
    justify-content: space-around;
    border: 1px solid;
    border-image: linear-gradient(to right, rgba(#51c8ff, 0) 0%, rgba(#51c8ff, 0.8) 50%, rgba(#51c8ff, 0)) 1;
    border-left: none;
    border-right: none;
    background-image: url('~@/assets/img/common/bg_pgo05.png');
    background-repeat: no-repeat;
    background-size: 100% 100%;
    .envDataToday-item {
      display: flex;
      .icon {
        width: 58px;
        height: 57px;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        margin-right: 12px;
      }
      .values {
        width: 85px;
        .label {
          color: #fff;
          font-size: 14px;
          background: linear-gradient(90deg, rgba(#286eab, 0.8) 0%, rgba(#286eab, 0));
          border-left: 2px solid #80c5f0;
          padding: 0 6px;
        }
        .value {
          color: #86d9ff;
          font-size: 24px;
          font-weight: bold;
          .unit {
            font-size: 14px;
            font-weight: 400;
          }
        }
      }
    }
  }
  .router-cnxm,
  .router-cdxm {
    width: 95px;
    height: 143px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    position: absolute;
    color: #fff;
    text-align: center;
    font-size: 12px;
    padding-top: 58px;
    cursor: pointer;
  }

  .router-video {
    width: 115px;
    height: 138px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    position: absolute;
    cursor: pointer;
    color: #fff;
    text-align: center;
    font-size: 12px;
    padding-top: 94px;
    top: 613px;
    left: 1130px;
    background-image: url('~@/assets/img/fbsgf/icon_cicic3.png');
  }
  .router-cnxm {
    top: 471px;
    left: 1363px;
    background-image: url('~@/assets/img/fbsgf/icon_cicic2.png');
  }
  .router-cdxm {
    top: 389px;
    left: 1223px;
    background-image: url('~@/assets/img/fbsgf/icon_cicic.png');
  }
}
</style>
