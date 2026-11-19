<template>
  <div class="country view">
    <Side :back="false"></Side>
    <MyHeader></MyHeader>
    <div class="country-content">
      <div class="sx" v-if="hasProject" @click="handleSX()"></div>
      <div class="l">
        <TextTittle :label="titleText(totalItems) || ''"></TextTittle>
        <div class="totalItems-content">
          <div class="totalItems-item" v-for="(item, index) in totalItems.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="titleText(grossGeneration) || ''"> </TextTittle>
        <div class="grossGeneration-content">
          <div class="grossGeneration-item" v-for="(item, index) in grossGeneration.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.country.grossGeneration[index]})` }"></div>
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="titleText(grossCapacity) || ''"> </TextTittle>
        <div class="grossCapacity-content">
          <div class="grossCapacity-item" v-for="(item, index) in grossCapacity.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.country.grossCapacity[index]})` }"></div>
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="titleText(totalRevenue) || ''"></TextTittle>
        <div class="totalRevenue-content">
          <div class="totalRevenue-item" v-for="(item, index) in totalRevenue.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.country.totalRevenue[index]})` }"></div>
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label }}</div>
          </div>
        </div>
      </div>
      <div class="c">
        <div class="alarmData-content">
          <div class="alarmData-item" v-for="(item, index) in []" :key="index" :style="{ backgroundImage: `url(${projectIcons.country.alarmData[index]})` }">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <div class="active-content">
          <div>
            <div class="label">选择项目类型，可查看该类型项目分布情况</div>
            <div class="value">
              <div class="active-item" :class="{ active: !!checked(item) }" v-for="(item, index) in tabList" :key="index" @click="hancleTab(item, index)">
                {{ item.dictLabel }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="r">
        <TextTittle :label="titleText(devOpsData) || ''"></TextTittle>
        <div class="devOpsData-content">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData?.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
        <TextTittle :label="titleText(runStatus) || ''"></TextTittle>
        <div class="runningState-content">
          <div class="runningState-item" v-for="(item, index) in runStatus.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="titleText(socialBenefit) || ''"></TextTittle>
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
        <TextTittle :label="titleText(totalAccess) || ''"></TextTittle>
        <div class="totalAccess-content">
          <div class="totalAccess-item" v-for="(item, index) in totalAccess.childs || []" :key="index">
            <div class="value">
              <span>{{ item.value }}</span>
            </div>
            <div class="label">
              {{ item.label }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { mapData, listProject } from '@/api/mapData.js'
import { sysDict } from '@/api/dict.js'
const Side = () => import('@/components/Side')
export default {
  data() {
    return {
      projectIcons: icons,
      hasProject: false,

      //项目总数量
      totalItems: {},
      //   总发电量
      grossGeneration: {},
      //   总装机容量
      grossCapacity: {},
      //   总收益
      totalRevenue: {},
      //   告警
      alarmData: {},
      //   运维管理
      devOpsData: {},
      //运行状况
      runningState: {},
      //社会效益
      socialBenefit: {},
      //总上网
      totalAccess: {},
      runStatus: {},
      // tab列表
      tabList: [],
      active: [],
    }
  },
  computed: {
    checked() {
      return (obj) => {
        return this.active.find((item) => {
          return item == obj.id
        })
      }
    },
    titleText() {
      return (obj) => {
        if (obj.label === null || obj.label === undefined) {
          obj.label = ''
        }
        if (obj.unit === null || obj.unit === undefined) {
          obj.unit = ''
        }
        if (obj.value === null || obj.value === undefined) {
          obj.value = ''
        }
        return `${obj.label}${obj.unit ? '(' + obj.unit + ')' : ''}${obj.value ? ':' + obj.value : ''}`
      }
    },
  },
  async created() {
    await this.getProjectList()
    this.initIcon()
    await this.getMapData()
  },
  mounted() {
    this.$bus.$on('refreshData', () => {
      this.getMapData()
    })
  },
  methods: {
    handleSX() {
      this.$router.push({
        path: '/province',
      })
    },
    hancleTab(obj, _index) {
      let hasTab = this.active.findIndex((item) => {
        return item == obj.id
      })
      if (hasTab == -1) {
        this.active.push(obj.id)
      } else {
        this.active.splice(hasTab, 1)
      }
      this.initIcon()
    },
    getMapData() {
      return new Promise((resolve, reject) => {
        mapData()
          .then((res) => {
            let data = res.data
            this.totalItems = data.totalItems || {}
            this.grossGeneration = data.grossGeneration || {}
            this.grossCapacity = data.grossCapacity || {}
            this.totalRevenue = data.totalRevenue || {}
            this.alarmData = data.alarmData || {}
            this.devOpsData = data.devOpsData || {}
            this.runningState = data.runningState || {}
            this.socialBenefit = data.socialBenefit || {}
            this.totalAccess = data.totalAccess || {}
            this.runStatus = data.runStatus || {}
            resolve()
          })
          .catch((err) => {
            reject(err)
          })
      })
    },
    initIcon() {
      let data = {
        projectTypeIdList: this.active,
      }
      listProject(data).then((res) => {
        this.hasProject = res.data.length > 0
      })
    },
    getProjectList() {
      return new Promise((resolve, reject) => {
        sysDict({
          type: 'project_type',
        })
          .then((res) => {
            this.tabList = res.data
              .map((item) => {
                return {
                  id: item.id,
                  dictLabel: item.dictLabel,
                }
              })
              .filter((item) => item.id != 6)
            resolve(res)
          })
          .catch((err) => {
            reject(err)
          })
      })
    },
  },
  components: {
    Side,
  },
  beforeDestroy() {
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
.country {
  background-image: url('~@/assets/img/country/bg_nm.png');
  .country-content {
    display: flex;
    padding-top: 125px;
    align-items: flex-start;
    height: 100%;
    .sx {
      position: absolute;
      top: 485px;
      left: 997px;
      height: 84px;
      width: 85px;
      cursor: pointer;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/country/bg_sx.png');
    }
    .l,
    .r {
      width: 420px;
    }
    .c {
      flex: 1;
      padding: 27px 30px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 100%;
    }
    .l {
      .title-comtainer {
        &:nth-child(3) {
          margin-top: 34px;
        }
        &:nth-child(5) {
          margin-top: 25px;
        }
        &:nth-child(7) {
          margin-top: 32px;
        }
      }
    }
    .r {
      .title-comtainer {
        &:nth-child(3) {
          margin-top: 40px;
        }
        &:nth-child(5) {
          margin-top: 34px;
        }
        &:nth-child(7) {
          margin-top: 40px;
        }
      }
    }
    .totalItems-content,
    .grossGeneration-content,
    .grossCapacity-content,
    .runningState-content {
      .label {
        font-size: 16px;
        color: #00ddff;
      }
    }
    .totalItems-content {
      display: flex;
      flex-wrap: wrap;
      padding-left: 16px;
      margin-top: 10px;
      .totalItems-item {
        width: 161px;
        height: 80px;
        padding-top: 10px;
        text-align: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/common/bg_nationwide0.png');
        .value {
          margin-top: 6px;
          font-size: 20px;
          font-weight: bold;
          background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
    .grossGeneration-content {
      display: flex;
      justify-content: space-around;
      text-align: center;
      margin-top: 10px;
      .grossGeneration-item {
        .icon {
          height: 46px;
          width: 47px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: 0 auto;
        }
        .label {
          margin-top: 6px;
        }
        .value {
          //   margin-top: 6px;
          font-size: 26px;
          font-weight: bold;
          background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
    .grossCapacity-content {
      display: flex;
      justify-content: space-around;
      text-align: center;
      margin-top: 10px;
      .grossCapacity-item {
        .icon {
          height: 49px;
          width: 40px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: 0 auto;
        }
        .label {
          margin-top: 6px;
        }
        .value {
          //   margin-top: 6px;
          font-size: 26px;
          font-weight: bold;
          background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
    .totalRevenue-content {
      display: flex;
      justify-content: space-around;
      text-align: center;
      margin-top: 10px;
      .totalRevenue-item {
        .icon {
          height: 122px;
          width: 122px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: -24px auto 0;
        }
        .label {
          // margin-top: 6px;
          color: #fff;
        }
        .value {
          margin-top: -24px;
          font-size: 26px;
          font-weight: bold;
          background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
    .devOpsData-content {
      margin-top: 18px;
      overflow: hidden;
      .devOpsData-item {
        width: 420px;
        height: 61px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/common/bg_nationwide04.png');
        margin-top: 12px;
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
    .runningState-content {
      display: flex;
      flex-wrap: wrap;
      margin-top: 10px;
      .runningState-item {
        width: 210px;
        height: 101px;
        padding-top: 20px;
        text-align: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/common/bg_nationwide05.png');
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
    .socialBenefit-content {
      display: flex;
      flex-wrap: wrap;
      // padding-left: 16px;
      margin-top: 30px;
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
    .totalAccess-content {
      display: flex;
      justify-content: space-around;
      text-align: center;
      margin-top: 22px;
      .totalAccess-item {
        .label {
          font-size: 16px;
          color: #fff;
        }
        .value {
          text-align: center;
          width: 120px;
          min-height: 48px;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/common/icon_nationwide13.png');
          background-size: contain;
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
    .alarmData-content {
      display: flex;
      flex-wrap: wrap;
      gap: 19px 19px;
      .alarmData-item {
        width: 193px;
        height: 69px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding-left: 50px;
        padding-top: 8px;
        .label {
          font-size: 14px;
        }
        .value {
          color: #e8feff;
          font-size: 22px;
          padding-left: 6px;
          margin-top: 4px;
        }
        &:nth-child(1) {
          .label {
            color: #1ff2ff;
          }
        }
        &:nth-child(2) {
          .label {
            color: #e5a974;
          }
        }
      }
    }
    .active-content {
      display: flex;
      justify-content: center;
      margin-bottom: 48px;
      .label {
        color: #bbe5fd;
        font-size: 18px;
        padding-left: 5px;
      }
      .value {
        display: flex;
        .active-item {
          cursor: pointer;
          width: 127px;
          height: 64px;
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-image: url('~@/assets/img/country/bg_nationwide06.png');
          display: flex;
          align-items: center;
          justify-content: center;
          color: #44baed;
          font-size: 16px;
        }
        .active {
          color: #ffffff;
          background-image: url('~@/assets/img/country/bg_nationwide07.png');
        }
      }
    }
  }
}
</style>
