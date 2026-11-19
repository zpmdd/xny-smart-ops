<template>
  <div class="fbsgf-systemArchitecture view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="l">
        <TextTittle :label="inverterDeviceData.label || ''"></TextTittle>
        <div class="inverterDeviceData-content">
          <div class="inverterDeviceData-item" v-for="(item, index) in inverterDeviceData.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? '/' + item.unit : '' }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
        <TextTittle :label="storageDeviceData.label || ''"></TextTittle>
        <div class="storageDeviceData-content">
          <div class="storageDeviceData-item" v-for="(item, index) in storageDeviceData.childs || []" :key="index">
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.fbsgf.storageDeviceData[index % 2]})` }"></div>
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
          </div>
        </div>
        <div class="devOpsData-content">
          <div class="devOpsData-item" v-for="(item, index) in electricData.childs || []" :key="index">
            <div class="label">{{ item.label }}{{ item.unit ? `(${item.unit})` : '' }}</div>
            <div class="value">
              <span> {{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="r">
        <div class="system-img">
          <div class="perpendicular" v-for="item in 10" :key="item"></div>
        </div>
        <div class="table-content">
          <div class="table-view">
            <div class="table-head">
              <div class="table-head-cell" v-for="(item, index) in realTimeData[0] || []" :key="index">
                {{ item.value }}
              </div>
            </div>
            <div class="tabel-body">
              <div class="table-body-row" v-for="(item, index) in realTimeData.slice(1) || []" :key="index">
                <div class="table-body-cell" v-for="(item1, index1) in item" :key="index1">
                  {{ item1.value }}
                </div>
              </div>
            </div>
          </div>
          <div class="storageStatus">
            <div class="label">储能蓄电池</div>
            <div class="value">
              <div><div></div></div>
              {{ storageStatus || '' }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { systemArchitecture } from '@/api/fbsgf.js'
export default {
  data() {
    return {
      projectIcons: icons,

      // 逆变器设备数据
      inverterDeviceData: {},
      //   储能设备数据
      storageDeviceData: {},
      //   表格数据
      realTimeData: [],
      //   电池状态
      storageStatus: '',

      electricData: {},
    }
  },
  created() {
    this.initData()
  },
  mounted() {
    this.$bus.$on('refreshData', () => {
      this.initData()
    })
  },
  methods: {
    initData() {
      systemArchitecture().then((res) => {
        let data = res.data
        this.inverterDeviceData = data.inverterDeviceData || {}
        this.storageDeviceData = data.storageDeviceData || {}
        this.realTimeData = data.realTimeData || []
        this.storageStatus = data.storageStatus || ''
        this.electricData = data.electricData || {}
      })
    },
  },
  beforeDestroy() {
    this.$bus.$off('refreshData')
  },
}
</script>
<style lang="scss" scoped>
@keyframes perpendicular {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-120px);
  }
}
@keyframes perpendicular1 {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-110px);
  }
}
.fbsgf-systemArchitecture {
  .view-container {
    display: flex;
    justify-content: space-between;
    .l {
      width: 420px;
      .title-comtainer {
        &:nth-child(3) {
          margin-top: 26px;
        }
      }
      .inverterDeviceData-content {
        display: flex;
        flex-wrap: wrap;
        margin-top: 16px;
        .inverterDeviceData-item {
          width: 210px;
          height: 101px;
          padding-top: 20px;
          text-align: center;
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-image: url('~@/assets/img/common/bg_nationwide05.png');
          .label {
            font-size: 16px;
            color: #00ddff;
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
      .storageDeviceData-content {
        display: flex;
        justify-content: space-around;
        text-align: center;
        // margin-top: 10px;
        .storageDeviceData-item {
          .icon {
            height: 122px;
            width: 122px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            margin: -12px auto 0;
          }
          .label {
            // margin-top: -6px;
            font-size: 14px;
            color: #ffffff;
          }
          .value {
            margin-top: -13px;
            font-size: 32px;
            font-weight: bold;
            text-shadow: 0px 6px 0px rgba(43, 159, 214, 0.2);
            background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
      }
      .devOpsData-content {
        margin-top: 13px;
        overflow: hidden;
        .devOpsData-item {
          width: 420px;
          height: 61px;
          margin-top: 2px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/common/bg_nationwide04.png');
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
    }
    .r {
      width: 1390px;
      .system-img {
        width: 1353px;
        height: 667px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/fbsgf/bg_psad03.png');
        position: relative;
        .perpendicular {
          position: absolute;
          width: 7px;
          height: 36px;
          background-color: #ffda0f;
          top: 510px;
          animation: perpendicular 4s linear infinite;
          &:nth-child(1) {
            left: 45px;
          }
          &:nth-child(2) {
            left: 176px;
          }
          &:nth-child(3) {
            left: 307px;
          }
          &:nth-child(4) {
            left: 437px;
          }
          &:nth-child(5) {
            left: 568px;
          }
          &:nth-child(6) {
            left: 817px;
          }
          &:nth-child(7) {
            left: 947px;
          }
          &:nth-child(8) {
            left: 1078px;
          }
          &:nth-child(9) {
            left: 1150px;
            top: 340px;
          }
          &:nth-child(10) {
            left: 1173px;
            top: 330px;
            animation: perpendicular1 3s linear infinite;
          }
        }
      }
      .table-content {
        display: flex;
        justify-content: space-between;
        .table-view {
          background: linear-gradient(90deg, rgba(14, 33, 61, 0) 0%, rgba(14, 33, 61, 0.7) 50%, rgba(14, 33, 61, 0) 100%);
          width: 1190px;
        }
        .storageStatus {
          text-align: center;
          width: 200px;
          margin-top: 22px;
          .label {
            font-size: 16px;
            color: #00ffff;
          }
          .value {
            color: #ffe0df;
            font-size: 16px;
            display: flex;
            align-items: center;
            margin-top: 10px;
            margin-left: 28px;
            justify-content: center;
            & > div {
              width: 16px;
              height: 16px;
              margin-left: -20px;
              background: rgba(255, 0, 0, 0.33);
              border-radius: 50%;
              margin-right: 8px;
              display: flex;
              align-items: center;
              justify-content: center;
              & > div {
                width: 7px;
                height: 7px;
                background-color: #ff0000;
                border-radius: 50%;
              }
            }
          }
        }
        .table-head {
          margin-top: 20px;
          display: flex;
          .table-head-cell {
            color: #00ffff;
            font-size: 16px;
            width: 120px;
            padding: 0 4px;
            word-break: break-all;
            text-align: center;
            &:first-child {
              width: 0;
              padding: 0;
            }
            &:nth-child(4) {
              margin-left: 10px;
            }
            &:nth-child(5) {
              margin-left: 16px;
            }
            &:nth-child(6) {
              margin-left: 16px;
            }
            &:nth-child(7) {
              margin-left: 126px;
            }
            &:nth-child(8) {
              margin-left: 10px;
            }
            &:nth-child(9) {
              margin-left: 10px;
            }
          }
        }
        .tabel-body {
          margin-top: 20px;
          .table-body-row {
            display: flex;
            min-height: 48px;
            border: 1px solid;
            border-image: linear-gradient(to right, rgba(#51c8ff, 0) 0%, rgba(#51c8ff, 0.8) 50%, rgba(#51c8ff, 0)) 1;
            border-left: none;
            border-right: none;
            border-top: none;
            &:first-child {
              border-top: 1px solid;
              border-image: linear-gradient(to right, rgba(#51c8ff, 0) 0%, rgba(#51c8ff, 0.8) 50%, rgba(#51c8ff, 0)) 1;
            }
            .table-body-cell {
              font-size: 16px;
              color: #00ffff;
              width: 120px;
              word-break: break-all;
              padding: 0 4px;
              text-align: center;
              display: flex;
              align-items: center;
              justify-content: center;
              &:first-child {
                font-size: 14px;
                width: 48px;
                padding: 0;
              }
              &:nth-child(2) {
                width: 72px;
              }
              &:nth-child(4) {
                margin-left: 10px;
              }
              &:nth-child(5) {
                margin-left: 16px;
              }
              &:nth-child(6) {
                margin-left: 16px;
              }
              &:nth-child(7) {
                margin-left: 126px;
              }
              &:nth-child(8) {
                margin-left: 10px;
              }
              &:nth-child(9) {
                margin-left: 10px;
              }
            }
          }
        }
      }
    }
  }
}
</style>
