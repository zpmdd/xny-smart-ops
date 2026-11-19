<template>
  <div class="cnxm_deviceMonitor container">
    <div class="devOpsData content-item">
      <div class="content">
        <div class="devOpsData-l">
          <div class="p-icon"></div>
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(0, 1) || []" :key="index">
            <div class="value">
              {{ item.value }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
            <div class="label">{{ item.label || '' }}</div>
          </div>
        </div>
        <div class="devOpsData-r">
          <div class="devOpsData-item" v-for="(item, index) in devOpsData.childs?.slice(1) || []" :key="index">
            <div class="label">{{ item.label || '' }}</div>
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="storageInfoMonitor content-item">
      <MTittle :text="storageInfoMonitor.label || ''"></MTittle>
      <div class="content">
        <div class="storageInfoMonitor-t">
          <div class="storageInfoMonitor-t-item" v-for="(item, index) in storageInfoMonitor.childs?.slice(0, 3) || []" :key="index">
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label || '' }}{{ item.unit ? `/${item.unit}` : '' }}</div>
          </div>
        </div>
        <div class="storageInfoMonitor-b">
          <div class="storageInfoMonitor-b-item" v-for="(item, index) in storageInfoMonitor.childs?.slice(3) || []" :key="index">
            <div class="value">
              {{ item.value }} <span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
            <div class="label">{{ item.label || '' }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="runningMonitor content-item">
      <MTittle :text="runningMonitor.label || ''"></MTittle>
      <div class="content">
        <div class="runningMonitor-item" v-for="(item, index) in runningMonitor.childs || []" :key="index">
          <div class="value">
            {{ item.value || '' }}
          </div>
          <div class="label">
            {{ item.label || '' }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="warnList content-item">
      <MTittle :text="'异常状态分析'"></MTittle>
      <div class="content">
        <div class="warnList-item" v-for="(item, index) in computedWarnList || []" :key="index">
          <div class="warnList-items" v-for="(ite, ind) in item" :key="ind">
            <div class="label">{{ warnLabel[ind] }}</div>
            <div class="value">{{ ite }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { newDeviceMonitor } from '@/api/cnxm.js'
export default {
  name: 'deviceMonitor',
  data() {
    return {
      devOpsData: {},
      storageInfoMonitor: {},
      storageStatusMonitor: {},
      runningMonitor: {},
      warnList: [],
    }
  },
  created() {
    this.initData()
  },
  computed: {
    computedWarnList() {
      if (this.warnList.length > 1) {
        return this.warnList.slice(1)
      }
      return []
    },
    warnLabel() {
      if (this.warnList.length) {
        return this.warnList[0]
      }
      return []
    },
  },
  mounted() {},
  methods: {
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
}
</script>
<style lang="scss" scoped>
.cnxm_deviceMonitor {
  display: flex;
  flex-direction: column;
  .devOpsData {
    .content {
      padding: 35px 30px;
      display: flex;
      align-items: center;
      .devOpsData-l {
        width: 280px;
        display: flex;
        border-right: 1px solid #e0e5ec;
        .p-icon {
          width: 80px;
          height: 80px;
          background-image: url('~@/assets/img/common/icon_charging01.png');
        }
        .devOpsData-item {
          flex: 1;
          margin-left: 20px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .value {
            font-family: douyv;
            font-size: 38px;
            color: #292929;
            .unit {
              font-size: 24px;
            }
          }
          .label {
            color: #333333;
            font-size: 24px;
          }
        }
      }
      .devOpsData-r {
        flex: 1;
        .devOpsData-item {
          display: flex;
          justify-content: space-between;
          padding-left: 40px;
          margin-top: 20px;
          &:first-child {
            margin-top: 0;
          }
          .label {
            width: 180px;
            color: #666666;
            font-size: 26px;
          }
          .value {
            max-width: 120px;
            font-weight: bold;
            font-size: 28px;
            color: #333333;
            .unit {
              font-size: 24px;
              font-weight: 400;
            }
          }
        }
      }
    }
  }
  .runningMonitor {
    .content {
      display: flex;
      flex-wrap: wrap;
      gap: 20px 20px;
      .runningMonitor-item {
        width: 206px;
        min-height: 150px;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .value {
          color: #292929;
          font-size: 36px;
        }
        .label {
          color: #666666;
          font-size: 23px;
          margin-top: 10px;
        }
      }
    }
  }
  .warnList {
    .content {
      .warnList-item {
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 10px;
        margin-top: 20px;
        padding: 20px;
        &:first-child {
          margin-top: 0;
        }
        .warnList-items {
          display: flex;
          .label {
            font-size: 26px;
            color: #666666;
            width: 160px;
          }
          .value {
            font-weight: bold;
            font-size: 28px;
            color: #333333;
            max-width: calc(100% - 170px);
          }
        }
      }
    }
  }
  .storageInfoMonitor {
    .storageInfoMonitor-t {
      display: flex;
      justify-content: space-between;
      .storageInfoMonitor-t-item {
        flex: 1;
        text-align: center;
        min-height: 150px;
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        margin-left: 20px;
        display: flex;
        justify-content: center;
        flex-direction: column;
        &:first-child {
          margin-left: 0;
        }
        .value {
          font-weight: bold;
          font-size: 36px;
          color: #292929;
        }
        .label {
          font-size: 24px;
          color: #666666;
          margin-top: 10px;
        }
      }
    }
    .storageInfoMonitor-b {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
      background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
      border-radius: 14px;
      min-height: 150px;
      align-items: center;
      .storageInfoMonitor-b-item {
        flex: 1;
        text-align: center;
        display: flex;
        justify-content: center;
        flex-direction: column;
        border-left: 1px solid #e0e5ec;
        &:first-child {
          border: none;
        }
        .value {
          font-weight: bold;
          font-size: 36px;
          color: #292929;
          .unit {
            font-size: 24px;
          }
        }
        .label {
          font-size: 24px;
          color: #666666;
          margin-top: 10px;
        }
      }
    }
  }
  .storageStatusMonitor {
    .storageStatusMonitor-item {
      display: flex;
      justify-content: space-between;
      min-height: 80px;
      align-items: center;
      font-size: 28px;
      border-radius: 10px;
      padding: 0 20px;
      margin-top: 12px;
      background-color: rgba(#b5b5b5, 0.05);
      &:first-child {
        margin-top: 0;
      }
      .label {
        color: #333333;
        width: 400px;
      }
      .value {
        position: relative;
        max-width: 200px;
        text-align: right;
        &::before {
          content: '';
          position: absolute;
          width: 11px;
          height: 11px;
          border-radius: 50%;
          left: -22px;
          top: 16px;
        }
      }
    }
    .err {
      background-color: rgba(#f40909, 0.05);
      .value {
        color: #ff0000;
        &::before {
          background-color: #ff0000;
        }
      }
    }
    .normal {
      .value {
        color: #00b348;
        &::before {
          background-color: #00d957;
        }
      }
    }
    .notEnabled {
      .value {
        color: #666666;
        &::before {
          background-color: #b5b5b5;
        }
      }
    }
  }
}
</style>
