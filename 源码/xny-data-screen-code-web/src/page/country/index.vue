<template>
  <div class="country view overview-screen">
    <ScreenSide v-if="!$route.meta.standalone" :back="false" />
    <MyHeader></MyHeader>
    <div class="country-content">
      <router-link v-if="hasProject" to="/province" class="sx" aria-label="进入陕西省" title="进入陕西省"></router-link>
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
        <div class="active-content">
          <div>
            <div class="label">选择项目类型，可查看该类型项目分布情况</div>
            <div class="value">
              <button v-for="(label, index) in projectTypes" :key="label" type="button" class="active-item"
                :class="{ active: active.includes(index + 1) }" :aria-pressed="active.includes(index + 1)"
                @click="toggleType(index + 1)">{{ label }}</button>
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
import icons from '@/utils/icons'
import { projects, projectTypes, titleText } from '@/data/overview.mjs'
import overviewMetrics from '@/data/overviewMetrics'

export default {
  mixins: [overviewMetrics],
  data() {
    return { projectIcons: icons, projectTypes, active: [] }
  },
  computed: {
    hasProject() {
      return !this.active.length || projects.some(project => this.active.includes(project.type))
    },
  },
  methods: {
    titleText,
    toggleType(type) {
      this.active = this.active.includes(type) ? this.active.filter(item => item !== type) : [...this.active, type]
    },
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
      z-index: 1;
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
      justify-content: flex-end;
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
          background-color: transparent;
          width: 127px;
          height: 64px;
          background-repeat: no-repeat;
          background-size: 100% 100%;
          background-image: url('~@/assets/img/country/bg_nationwide06.png');
          display: flex;
          align-items: center;
          justify-content: center;
          color: #90dcfa;
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
