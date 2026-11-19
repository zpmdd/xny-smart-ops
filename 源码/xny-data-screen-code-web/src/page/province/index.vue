<template>
  <div class="province view overview-screen" @click="currentActive = null" @keydown.esc="currentActive = null">
    <ScreenSide v-if="!$route.meta.standalone" />
    <MyHeader></MyHeader>
    <router-link class="overview-back" to="/country">‹ 返回主页面</router-link>
    <div class="view-container">
      <div v-for="item in markers" :key="item.id" class="tip-icon"
        :class="[item.id === 'weijiazhuang' ? 'baoji' : markerClasses[item.type], { selected: currentActive === item.id }]">
        <img class="marker-art" alt="" :src="projectIcons.provinceMapIcon[item.type]" />
        <div class="mask">
          <button type="button" class="marker-button" :aria-label="item.city + ' · ' + item.projectName"
            :title="item.projectName" :aria-expanded="currentActive === item.id"
            @click.stop="currentActive = currentActive === item.id ? null : item.id">
            <span v-if="item.id === 'weijiazhuang'" class="marker-name">{{ item.projectName }}</span>
          </button>
          <div class="popover" v-if="currentActive === item.id" @click.stop>
            <div class="close"><button type="button" aria-label="关闭项目详情" @click="currentActive = null">×</button></div>
            <div class="label"><span>{{ projectTypes[item.type - 1] }}</span></div>
            <div class="typeName"><div>项目名称</div><div>{{ item.projectName }}</div></div>
            <template v-if="item.contact">
              <div class="director"><div>负责人</div><div>{{ item.contact }}</div></div>
              <div class="tel"><div>联系方式</div><div>{{ item.extension }}</div></div>
            </template>
            <div class="address"><div>项目地址</div><div>陕西省{{ item.city }}</div></div>
            <div v-if="item.id === 'weijiazhuang'" class="director"><div>装机容量</div><div>{{ item.capacity }} MW</div></div>
            <div v-if="item.id === 'weijiazhuang'" class="director"><div>运行状态</div><div>正常运行</div></div>
            <router-link v-if="item.id === 'weijiazhuang' || item.permitted"
              class="detail-btn" :to="item.id === 'weijiazhuang' ? item.path : { path: item.path, query: { id: item.id, projectName: item.projectName } }">查看详情</router-link>
          </div>
        </div>
      </div>
      <div class="pu_sx08"></div>
      <div class="totalItems">
        <TextTittle :label="titleText(totalItems) || ''"></TextTittle>
        <div class="totalItems-content">
          <div class="totalItems-item" v-for="(item, index) in totalItems.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div class="r-content">
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
            <div class="icon" :style="{ backgroundImage: `url(${projectIcons.country.totalRevenue[index]})` }"></div>
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label }}</div>
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
  props: { permittedProjects: Function },
  data() {
    return { projectIcons: icons, permitted: [], projectTypes, currentActive: null,
      markerClasses: { 1: 'fbsgf', 2: 'jzsgf', 3: 'cnxm', 4: 'cdxm' } }
  },
  computed: {
    markers() {
      return projects.map(project => {
        if (project.id === 'weijiazhuang') return project
        const match = this.permitted.find(p => Number(p.projectTypeId) === project.type)
        return match ? { ...project, id: match.id, projectName: match.projectName, city: match.city,
          contact: match.headName, extension: match.contactInfo, permitted: true } : project
      })
    },
  },
  async mounted() {
    // Existing station details use the signed-in user's project directory.
    if (this.permittedProjects) {
      try { this.permitted = (await this.permittedProjects()).data || [] } catch { this.permitted = [] }
    }
  },
  methods: { titleText },
}
</script>
<style lang="scss" scoped>
.province {
  background-image: url('~@/assets/img/province/bg_mosan.png');
  position: relative;
  .tip-icon {
    background-size: 100% 100%;
    background-repeat: no-repeat;
    position: absolute;
    pointer-events: none;
    .marker-art {
      position: absolute;
      inset: 0;
      width: 100%;
      height: 100%;
    }
    &.selected { z-index: 3; }
    .mask {
      position: relative;
      height: 66px;
      width: 57px;
      margin-top: 25px;
      pointer-events: auto;
      .marker-button {
        cursor: pointer;
        width: 100%;
        height: 100%;
        background: transparent;
      }
      .popover {
        width: 275px;
        min-height: 360px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        padding: 0 7px 15px 37px;
        background-image: url('~@/assets/img/province/bg_mosan_popup0.png');
        position: absolute;
        z-index: 9;
        .close {
          font-size: 22px;
          height: 27px;
          color: #fff;
          text-align: right;
          cursor: pointer;
          button {
            background: none;
            color: inherit;
            cursor: pointer;
            margin-top: 10px;
          }
        }
        .label {
          margin-left: -18px;
          font-size: 19px;
          min-width: 251px;
          padding: 10px 22px;
          background-image: url('~@/assets/img/province/bg_mosan_popup011.png');
          font-weight: bold;
          color: #b4ebff;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          span {
            background: linear-gradient(0deg, #9dbae2 0%, #fff);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
        .typeName,
        .tel,
        .director,
        .address {
          margin-top: 15px;
          background-image: url('~@/assets/img/province/bg_mosan_popup02.png');
          background-size: 100% 100%;
          padding: 10px 0;
          background-repeat: no-repeat;
          display: flex;
          align-items: flex-start;
          & > div {
            &:first-child {
              text-align: right;
              color: #00f0ff;
              font-size: 13px;
              width: 74px;
              min-width: 74px;
            }
            &:last-child {
              width: 140px;
              margin-left: 12px;
              word-break: break-all;
              color: #d9ebf7;
              font-size: 17px;
            }
          }
        }
        .typeName {
          margin-top: 16px;
        }
        .detail-btn {
          display: block;
          margin: 15px auto 0;
          width: 173px;
          height: 33px;
          line-height: 33px;
          text-align: center;
          background-image: url('~@/assets/img/province/bg_mosan_popup03.png');
          background-size: 100% 100%;
          background-repeat: no-repeat;
          color: #8fb9f7;
          cursor: pointer;
          font-size: 16px;
          span {
            background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
        }
      }
    }
  }
  .fbsgf {
    width: 342px;
    height: 209px;
    left: 375px;
    top: 479px;
    .mask {
      margin-left: 31px;
      .popover {
        margin-left: -306px;
      }
    }
  }
  .jzsgf {
    width: 303px;
    height: 196px;
    left: 765px;
    top: 247px;
    .mask {
      margin-left: calc(100% - 88px);
      .popover {
        margin-left: 88px;
      }
    }
  }
  .cnxm {
    width: 345px;
    height: 193px;
    left: 731px;
    top: 457px;
    .mask {
      margin-left: calc(100% - 88px);
      .popover {
        margin-left: 88px;
      }
    }
  }
  .cdxm {
    width: 367px;
    height: 172px;
    left: 736px;
    top: 528px;
    .mask {
      margin-left: calc(100% - 88px);
      .popover {
        margin-left: 88px;
      }
    }
  }
  .baoji {
    width: 303px;
    height: 196px;
    left: 322px;
    top: 506px;
    .marker-art { transform: scaleX(-1); }
    .mask {
      margin: 25px 0 0 33px;
      .popover { left: -275px; bottom: 10px; }
    }
    .marker-name {
      position: absolute;
      right: 100%;
      top: 18px;
      white-space: nowrap;
      color: #d7f9ff;
      background: #0b263f;
      border: 1px solid #4da6bd;
      border-radius: 4px;
      padding: 4px 12px;
      font-size: 17px;
    }
  }
  .pu_sx08 {
    width: 333px;
    height: 221px;
    top: 720px;
    left: 911px;
    position: absolute;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/province/icon_identification06.png');
  }
  .totalItems {
    position: absolute;
    bottom: 57px;
    left: 280px;
    .totalItems-content {
      display: flex;
      align-items: flex-start;
      padding-left: 16px;
      margin-top: 8px;
      .totalItems-item {
        width: 161px;
        height: 80px;
        padding-top: 10px;
        text-align: center;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/common/bg_nationwide0.png');
        .label {
          font-size: 16px;
          color: #00ddff;
        }
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
  }
  .r-content {
    position: absolute;
    left: 1263px;
    top: 172px;
    .socialBenefit-content {
      display: flex;
      flex-wrap: wrap;
      // padding-left: 16px;
      margin-top: 22px;
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
          font-size: 16px;
          color: #00ddff;
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
      // margin-top: 10px;
      .grossCapacity-item {
        .icon {
          height: 122px;
          width: 122px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: -30px auto 0;
        }
        .label {
          // margin-top: -6px;
          font-size: 14px;
          color: #ffffff;
        }
        .value {
          margin-top: -20px;
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
      // margin-top: 10px;
      .totalRevenue-item {
        .icon {
          height: 122px;
          width: 122px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: -30px auto 0;
        }
        .label {
          // margin-top: 6px;
          font-size: 14px;
          color: #ffffff;
        }
        .value {
          margin-top: -20px;
          font-size: 26px;
          font-weight: bold;
          background: linear-gradient(0deg, #68d9ea 0.4150390625%, #fff);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
    .totalAccess-content {
      display: flex;
      justify-content: space-around;
      text-align: center;
      // margin-top: 22px;
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
    .title-comtainer {
      &:nth-child(3) {
        margin-top: 30px;
      }
      &:nth-child(5) {
        margin-top: 16px;
      }
      &:nth-child(7) {
        margin-top: 16px;
      }
      &:nth-child(9) {
        margin-top: 18px;
      }
    }
  }
}
</style>
