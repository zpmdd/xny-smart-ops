<template>
  <div class="fbsgf_systemArchitecture container">
    <div class="sysImg content-item">
      <el-image style="width: 100%" :src="url" :preview-src-list="srcList"> </el-image>
    </div>
    <div class="realTimeData content-item">
      <MTittle :text="'逆变器实时数据'"></MTittle>
      <div class="content">
        <div class="row" v-for="(item, index) in realTimeData || []" :key="index">
          <div class="row-item">
            <div class="col" v-for="(i, index1) in item || []" :key="index1">{{ i.value }}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="inverterDeviceData content-item">
      <MTittle :text="inverterDeviceData.label || ''"></MTittle>
      <div class="content">
        <div class="inverterDeviceData-b">
          <div class="inverterDeviceData-item" v-for="(item, index) in inverterDeviceData.childs || []" :key="index">
            <div class="value">
              {{ item.value || '' }}
            </div>
            <div class="label">
              {{ item.label || '' }}<span class="unit" v-if="item.unit">{{ item.unit ? `/${item.unit}` : '' }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="storageDeviceData content-item">
      <MTittle :text="storageDeviceData.label || ''"></MTittle>
      <div class="content">
        <div class="storageStatus-item err">
          <div class="label">{{ '储能蓄电池运行状态' }}</div>
          <div class="value">{{ storageStatus || '' }}</div>
        </div>
        <div class="storageDeviceData-b" v-if="storageDeviceData.childs?.length">
          <div class="storageDeviceData-row" v-for="(item, index) in Math.ceil(storageDeviceData.childs.length / 3)" :key="index">
            <div class="storageDeviceData-item" v-for="(item1, index1) in storageDeviceData.childs.slice(index * 3, (index + 1) * 3)" :key="index1">
              <div class="value">
                {{ item1.value || '' }}
              </div>
              <div class="label">
                {{ item1.label || '' }}<span class="unit" v-if="item1.unit">{{ item1.unit ? `/${item1.unit}` : '' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { systemArchitecture } from '@/api/fbsgf'
export default {
  name: 'systemArchitecture',
  data() {
    return {
      url: require('@/assets/img/common/photovoltaic_bg003.png'),
      srcList: [require('@/assets/img/common/photovoltaic_bg003.png')],

      realTimeData: [],
      inverterDeviceData: {},
      storageDeviceData: {},
      storageStatus: '',
    }
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      systemArchitecture().then((res) => {
        let data = res.data
        let realTimeData = []
        if (data.realTimeData.length) {
          for (let index = 0; index < data.realTimeData[0].length; index++) {
            let realItem = []
            for (let index1 = 0; index1 < data.realTimeData.length; index1++) {
              realItem.push(data.realTimeData[index1][index])
            }
            realTimeData.push(realItem)
          }
        }
        this.realTimeData = realTimeData
        this.inverterDeviceData = data.inverterDeviceData || {}
        this.storageDeviceData = data.storageDeviceData || {}
        this.storageStatus = data.storageStatus || ''
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fbsgf_systemArchitecture {
  display: flex;
  flex-direction: column;
  .sysImg {
    border-radius: 10px;
    overflow: hidden;
  }
  .realTimeData {
    .content {
      overflow-x: auto;
      .row {
        height: 80px;
        border-radius: 14px;
        font-size: 28px;
        color: #333333;
        white-space: nowrap;
        display: flex;
        border-bottom: 1px solid #d3d3d3;
        &:first-child {
          background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
          border-bottom: none;
          .col {
            font-weight: bold;
            font-size: 28px;
            color: #333333;
          }
        }
        &:last-child {
          border-bottom: none;
        }
        .col {
          padding: 0 10px;
          width: 143px;
          display: inline-flex;
          align-items: center;
          word-break: break-all;
          white-space: break-spaces;
          vertical-align: middle;
          height: 100%;
          &:nth-child(1) {
            width: 230px;
          }
        }
      }
    }
  }
  .inverterDeviceData {
    .inverterDeviceData-b {
      background: linear-gradient(0deg, #e9f0fd 0%, #f7faff 100%);
      border-radius: 14px;
      display: flex;
      flex-wrap: wrap;
      position: relative;
      &::before {
        content: '';
        position: absolute;
        height: 74px;
        width: 0px;
        border-left: 1px solid #e0e5ec;
        left: 50%;
        top: 50%;
        margin-top: -37px;
      }
      .inverterDeviceData-item {
        min-height: 134px;
        width: 50%;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .label {
          margin-top: 10px;
          color: #666666;
          font-size: 24px;
        }
        .value {
          font-weight: bold;
          font-size: 36px;
          color: #292929;
        }
      }
    }
  }
  .storageDeviceData {
    .storageStatus-item {
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
    .storageDeviceData-b {
      margin-top: 20px;
      .storageDeviceData-row {
        background: linear-gradient(0deg, #f3f7fd 0%, #fafcff 100%);
        border-radius: 18px;
        margin-top: 20px;
        min-height: 150px;
        &:first-child {
          margin-top: 0;
        }
        display: flex;
        align-items: center;
        .storageDeviceData-item {
          flex: 1;
          display: flex;
          flex-direction: column;
          text-align: center;
          justify-content: center;
          align-items: center;
          border-right: 1px solid #e0e5ec;
          &:last-child {
            border-right: none;
          }
          .value {
            font-weight: bold;
            font-size: 36px;
            color: #292929;
          }
          .label {
            font-size: 24px;
            color: #666666;
            margin-top: 15px;
          }
        }
      }
    }
  }
}
</style>
