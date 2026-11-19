<template>
  <div class="fdxm-blowerMonitor view">
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="topInfo-content">
        <div class="topInfo-item" v-for="(item, index) in topInfo.childs || []" :key="index">
          <div class="value">{{ item.value }}</div>
          <div class="label">
            <div class="l-icon"></div>
            <div class="name">{{ item.label }}{{ item.unit ? `${item.unit}` : '' }}</div>
            <div class="r-icon"></div>
          </div>
        </div>
      </div>
      <div class="blowerInfo-content">
        <div class="blowerInfo-item" v-for="(item, index) in blowerInfo.childs || []" :key="index" :class="{ err: item?.childs[0]?.value == '故障', normal: item?.childs[0]?.value == '正常发电' }">
          <div class="blowerInfo-row" v-for="(item1, index1) in item.childs?.slice(1) || []" :key="index1">
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
  </div>
</template>
<script>
import icons from '@/utils/icons.js'
import { blowerMonitor } from '@/api/fdxm.js'
export default {
  data() {
    return {
      projectIcons: icons,
      // 顶部数据
      topInfo: {},
      // 风机数据
      blowerInfo: {},
    }
  },
  created() {
    this.initData()
  },
  mounted() {},
  methods: {
    initData() {
      blowerMonitor().then((res) => {
        let data = res.data
        this.topInfo = data.topInfo || {}
        this.blowerInfo = data.blowerInfo || {}
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.fdxm-blowerMonitor {
  .view-container {
    .topInfo-content {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .topInfo-item {
        .value {
          width: 271px;
          height: 71px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/fdxm/bg_wtm01.png');
          font-size: 34px;
          line-height: 71px;
          color: #fff;
          font-weight: bold;
          text-align: center;
        }
        .label {
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 19px;
          margin-top: 6px;
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
    .blowerInfo-content {
      display: flex;
      flex-wrap: wrap;
      gap: 29px 40px;
      margin-top: 30px;
      .blowerInfo-item {
        width: 434px;
        height: 170px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding: 0 34px 0 187px;
        .blowerInfo-row {
          display: flex;
          align-items: center;
          justify-content: space-between;
          min-height: 50px;
          .label {
            color: #fff;
            font-size: 18px;
            width: 70px;
          }
          .value {
            font-weight: bold;
            font-size: 24px;
            width: calc(100% - 70px);
            text-align: right;
            word-break: break-all;
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
      .err {
        background-image: url('~@/assets/img/fdxm/bg_wtm03.png');
      }
      .normal {
        background-image: url('~@/assets/img/fdxm/bg_wtm02.png');
      }
    }
  }
}
</style>
