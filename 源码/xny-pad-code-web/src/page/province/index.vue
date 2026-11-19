<template>
  <div class="province">
    <Header></Header>
    <div class="map" :class="{ map1: pageType1.includes(type) || type == 'fbsgf' || type == 'jzsgf' }">
      <!-- <div class="shanxi-icon" @click.stop="handleProvince"></div> -->
      <div class="tip-icon" :class="iconName(item)" v-for="(item, index) in mapTip" :key="index" :style="{ 'background-image': 'url(' + projectIcons.provinceMapIcon[item.projectTypeId] + ')' }">
        <div class="mask" @click.stop="handleTip(item)">
          <div class="popover" v-if="popoverShow && currentActive == item.id">
            <div class="close"><i class="el-icon-close" @click.stop="popoverShow = false"></i></div>
            <div class="label">{{ item.projectTypeName || '' }}</div>
            <div class="typeName">
              <div>项目名称</div>
              <div>{{ item.projectName || '' }}</div>
            </div>
            <div class="director">
              <div>负责人</div>
              <div>{{ item.headName || '' }}</div>
            </div>
            <div class="tel">
              <div>联系方式</div>
              <div>{{ item.contactInfo || '' }}</div>
            </div>
            <div class="address">
              <div>项目地址</div>
              <div>{{ item.province || '' }}{{ item.city || '' }}</div>
            </div>
            <div class="detail-btn" @click.stop="handleView(item)">查看详情</div>
          </div>
        </div>
      </div>
      <div class="pu_sx08" v-if="pageType1.includes(type)"></div>
    </div>
    <div class="gfxm" v-if="pageType2.includes(type)">
      <div class="gfxm-item">
        <div class="nation">{{ initData?.socialBenefitVO?.label || '' }}</div>
        <div class="gfxm-value">
          <div class="data-item" v-for="(item, index) of initData?.socialBenefitVO?.childs || []" :key="index">
            <div class="icon"><img :src="projectIcons.socialBenefitVO[item.icon]" alt="" /></div>
            <div class="value">
              <div class="data-title" v-html="item.label"></div>
              <div class="data-value">
                <span>{{ item.value }}</span>
                <span>{{ item.unit }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="gfxm-item">
        <div class="nation">{{ initData.projectNumberVO.label || '' }}</div>
        <div class="gfxm-value">
          <div v-if="initData.projectNumberVO">
            <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[1] + ')' }"></div>
            <div class="bias"></div>
            <div class="data">
              <div class="value">{{ initData.projectNumberVO.value | formatNum }}</div>
              <div class="label">{{ initData.projectNumberVO.label }}{{ initData.projectNumberVO.unit }}</div>
            </div>
          </div>
          <div v-if="initData.powerGenerationVO">
            <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[2] + ')' }"></div>
            <div class="bias"></div>
            <div class="data">
              <div class="value">{{ initData.powerGenerationVO.value | formatNum }}</div>
              <div class="label">{{ initData.powerGenerationVO.label }}{{ initData.powerGenerationVO.unit }}</div>
            </div>
          </div>
          <div v-if="initData.installedCapacityVO">
            <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[3] + ')' }"></div>
            <div class="bias"></div>
            <div class="data">
              <div class="value">{{ initData.installedCapacityVO.value | formatNum }}</div>
              <div class="label">{{ initData.installedCapacityVO.label }}{{ initData.installedCapacityVO.unit }}</div>
            </div>
          </div>
          <div v-if="initData.incomeVO">
            <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[4] + ')' }"></div>
            <div class="bias"></div>
            <div class="data">
              <div class="value">{{ initData.incomeVO.value | formatNum }}</div>
              <div class="label">{{ initData.incomeVO.label }}{{ initData.incomeVO.unit }}</div>
            </div>
          </div>
          <div v-if="initData.powerGridVO">
            <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[5] + ')' }"></div>
            <div class="bias"></div>
            <div class="data">
              <div class="value">{{ initData.powerGridVO.value | formatNum }}</div>
              <div class="label">{{ initData.powerGridVO.label }}{{ initData.powerGridVO.unit }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="country-data" v-if="pageType1.includes(type)">
      <div class="data-content">
        <div class="data-item" v-for="(item, index) of initData?.socialBenefitVO?.childs || []" :key="index">
          <div class="icon"><img :src="projectIcons.socialBenefitVO[item.icon]" alt="" /></div>
          <div class="value">
            <div class="data-title" v-html="item.label"></div>
            <div class="data-value">
              <span>{{ item.value }}</span>
              <span>{{ item.unit }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="cn" v-if="pageType3.includes(type) || pageType4.includes(type)">
      <ul>
        <li v-if="initData.projectNumberVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[1] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.projectNumberVO.value | formatNum }}</div>
            <div class="label">{{ initData.projectNumberVO.label }}{{ initData.projectNumberVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.powerGenerationVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[2] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.powerGenerationVO.value | formatNum }}</div>
            <div class="label">{{ initData.powerGenerationVO.label }}{{ initData.powerGenerationVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.installedCapacityVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[3] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.installedCapacityVO.value | formatNum }}</div>
            <div class="label">{{ initData.installedCapacityVO.label }}{{ initData.installedCapacityVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.incomeVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[type == 'cdxm' ? 4 : 6] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.incomeVO.value | formatNum }}</div>
            <div class="label">{{ initData.incomeVO.label }}{{ initData.incomeVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.powerGridVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[5] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.powerGridVO.value | formatNum }}</div>
            <div class="label">{{ initData.powerGridVO.label }}{{ initData.powerGridVO.unit }}</div>
          </div>
        </li>
      </ul>
    </div>
    <div class="popup-content" v-if="pageType1.includes(type)">
      <div class="popup-item" v-if="initData.projectNumberVO">
        <div class="nation">{{ initData.projectNumberVO.label }} {{ initData.projectNumberVO.value }} {{ initData.projectNumberVO.unit }}</div>
        <div class="popup-value income">
          <div class="project-item" v-for="(item, index) in initData?.projectNumberVO?.childs || []" :key="index">
            <div class="project-icon" :style="{ 'background-image': 'url(' + projectIcons.incomeVO[index + 1] + ')' }"></div>
            <div class="value">{{ item.value }}</div>
            <div class="label">{{ item.label }}</div>
          </div>
        </div>
      </div>
      <div class="popup-item" v-if="initData.powerGenerationVO">
        <div class="nation">{{ initData.powerGenerationVO.label }} {{ initData.powerGenerationVO.value }} {{ initData.powerGenerationVO.unit }}</div>
        <div class="popup-value project">
          <div class="project-item" v-for="(item, index) of initData?.powerGenerationVO?.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div class="popup-item" v-if="initData.installedCapacityVO">
        <div class="nation">{{ initData.installedCapacityVO.label }} {{ initData.installedCapacityVO.value }} {{ initData.installedCapacityVO.unit }}</div>
        <div class="popup-value project">
          <div class="project-item" v-for="(item, index) of initData?.installedCapacityVO?.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div class="popup-item" v-if="initData.incomeVO">
        <div class="nation">{{ initData.incomeVO.label }} {{ initData.incomeVO.value }} {{ initData.incomeVO.unit }}</div>
        <div class="popup-value project">
          <div class="project-item" v-for="(item, index) of initData?.incomeVO?.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
      <div class="popup-item" v-if="initData.powerGridVO">
        <div class="nation">{{ initData.powerGridVO.label }} {{ initData.powerGridVO.value }} {{ initData.powerGridVO.unit }}</div>
        <div class="popup-value project">
          <div class="project-item" v-for="(item, index) of initData?.powerGridVO?.childs || []" :key="index">
            <div class="label">{{ item.label }}</div>
            <div class="value">{{ item.value }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Header from '@/components/Header'
import icons from '@/utils/icons.js'
import { mapData, listProject } from '@/api/mapData.js'
import { sysDict } from '@/api/dict.js'
export default {
  name: 'province',
  data() {
    return {
      type: '',
      projectIcons: icons,
      pageType1: ['xmfb'],
      pageType2: ['fbsgf', 'jzsgf', 'fdxm', 'sdxm'],
      pageType3: ['cnxm'],
      pageType4: ['cdxm'],
      project: ['', 'fbsgf', 'jzsgf', 'cnxm', 'cdxm', 'fdxm', 'sdxm'],
      projectList: [],
      initData: {
        projectNumberVO: {},
        powerGenerationVO: {},
        installedCapacityVO: {},
        incomeVO: {},
        powerGridVO: {},
        socialBenefitVO: {},
      },
      mapTip: [],
      currentActive: '',
      popoverShow: false,
    }
  },
  computed: {
    iconName() {
      return (val) => {
        let arr = ['', 'fbsgf', 'jzsgf', 'cnxm', 'cdxm', 'fdxm', 'sdxm']
        return arr[val.projectTypeId]
      }
    },
    tipShow() {
      return (obj) => {
        if (this.type == 'xmfb') {
          return true
        } else if (this.type == 'jzsgf') {
          return obj.type == this.type
        } else {
          return obj.type == 'fdxm'
        }
      }
    },
  },
  async created() {
    this.type = this.$route.query.type
    await this.getProjectList()
    await this.getMapData()
    let data = {
      projectTypeId: this.projectList.find((item) => item.type === this.type)?.id || '',
      provinceCode: '610000',
    }
    listProject(data).then((res) => {
      this.mapTip = res.data
      let addr = {
        6: '汉中市',
        5: '榆林市',
      }
      this.mapTip.forEach((item) => {
        if (addr[item.projectTypeId]) {
          item.city = addr[item.projectTypeId]
        }
      })
    })
  },
  mounted() {
    document.addEventListener('click', () => {
      this.popoverShow = false
    })
  },
  methods: {
    getProjectList() {
      return new Promise((resolve, reject) => {
        sysDict({
          type: 'project_type',
        })
          .then((res) => {
            this.projectList = res.data.map((item) => {
              return {
                id: item.id,
                dictLabel: item.dictLabel,
                type: this.project[item.id],
              }
            })
            resolve(res.data)
          })
          .catch((err) => {
            reject(err)
          })
      })
    },
    getMapData() {
      return new Promise((resolve, reject) => {
        let data = {
          projectTypeId: this.projectList.find((item) => item.type === this.type)?.id || '',
          provinceCode: '610000',
        }
        mapData(data).then((res) => {
          if (this.pageType1.includes(this.type) || this.pageType2.includes(this.type)) {
            // 项目分布
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.grossGeneration,
              installedCapacityVO: res.data.grossCapacity,
              incomeVO: res.data.totalRevenue,
              powerGridVO: res.data.totalAccess,
              socialBenefitVO: res.data.socialBenefit,
            }
          }
          if (this.pageType3.includes(this.type)) {
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.chargeToday,
              installedCapacityVO: res.data.totalCharge,
              incomeVO: res.data.totalDischarge,
              powerGridVO: res.data.currentDischarge,
            }
          }
          if (this.pageType4.includes(this.type)) {
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.totalChargeTimes,
              installedCapacityVO: res.data.totalCharge,
              incomeVO: res.data.totalRevenue,
              powerGridVO: res.data.totalChargeTime,
            }
          }
          resolve()
        })
      })
    },
    handleTip(obj) {
      this.currentActive = obj.id
      this.popoverShow = true
    },
    handleView(obj) {
      let path = {
        2: 'jzsgf',
        1: 'fbsgf',
        3: 'cnxm',
        4: 'cdxm',
        5: 'fdxm',
        6: 'sdxm',
      }
      if (path[obj.projectTypeId]) {
        this.$router.push({
          path: `/detail/${path[obj.projectTypeId]}`,
        })
      }
    },
  },
  components: {
    Header,
  },
}
</script>
<style lang="scss" scoped>
.province {
  height: 1600px;
  overflow: hidden;
  .map {
    width: 3223px;
    height: 1600px;
    margin-top: -198px;
    background-size: 100% 100%;
    overflow: hidden;
    margin-left: -280px;
    background-image: url('~@/assets/img/province/map.png');
    .pu_sx08 {
      width: 333px;
      height: 224px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/province/icon_pu_sx08.png');
      margin-top: 1140px;
      margin-left: 1988px;
    }
    .tip-icon {
      background-size: 100% 100%;
      background-repeat: no-repeat;
      position: absolute;
      .mask {
        position: relative;
        height: 130px;
        width: 130px;
        .popover {
          width: 386px;
          min-height: 462px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          padding: 20px 18px 22px 53px;
          background-image: url('~@/assets/img/province/bg_pu_sx01.png');
          position: absolute;
          z-index: 99999;
          .close {
            font-size: 22px;
            color: #fff;
            text-align: right;
          }
          .label {
            margin-left: -27px;
            font-size: 26px;
            min-width: 253px;
            padding: 13px 0 12px 32px;
            background-image: url('~@/assets/img/province/bg_pu_sx03.png');
            font-weight: bold;
            color: #b4ebff;
            background-size: 100% 100%;
            background-repeat: no-repeat;
          }
          .typeName,
          .tel,
          .director,
          .address {
            margin-top: 20px;
            background-image: url('~@/assets/img/province/bg_pu_sx04.png');
            background-size: 100% 100%;
            padding: 12px 0;
            background-repeat: no-repeat;
            display: flex;
            align-items: flex-start;
            & > div {
              &:first-child {
                text-align: right;
                color: #00f0ff;
                font-size: 18px;
                min-width: 102px;
                padding-left: 30px;
              }
              &:last-child {
                margin-left: 18px;
                color: #d9ebf7;
                font-size: 24px;
              }
            }
          }
          .typeName {
            margin-top: 16px;
          }
          .detail-btn {
            margin: 22px auto 0;
            width: 240px;
            height: 44px;
            line-height: 44px;
            text-align: center;
            background-image: url('~@/assets/img/province/bg_pu_sx02.png');
            background-size: 100% 100%;
            background-repeat: no-repeat;
            color: #8fb9f7;
            font-size: 22px;
          }
        }
      }
    }
    .fdxm {
      width: 275px;
      height: 205px;
      margin-left: 1349px;
      margin-top: 244px;
      .mask {
        .popover {
          margin-left: -386px;
        }
      }
    }
    .jzsgf {
      width: 315px;
      height: 202px;
      margin-left: 1625px;
      margin-top: 448px;
      .mask {
        margin-left: calc(100% - 130px);
        .popover {
          margin-left: 110px;
        }
      }
    }
    .cnxm {
      width: 364px;
      height: 204px;
      margin-left: 1523px;
      margin-top: 776px;
      .mask {
        margin-left: calc(100% - 130px);
        .popover {
          margin-left: 110px;
        }
      }
    }
    .cdxm {
      width: 357px;
      height: 138px;
      margin-left: 1523px;
      margin-top: 894px;
      .mask {
        margin-left: calc(100% - 130px);
        .popover {
          margin-left: 110px;
        }
      }
    }
    .fbsgf {
      width: 508px;
      height: 234px;
      margin-left: 1045px;
      margin-top: 783px;
      .mask {
        .popover {
          margin-left: -386px;
        }
      }
    }
    .sdxm {
      width: 365px;
      height: 204px;
      margin-left: 947px;
      margin-top: 973px;
      .mask {
        left: 0;
        .popover {
          margin-left: 110px;
        }
      }
    }
  }
  .map1 {
    margin-left: -660px;
  }
  .gfxm {
    position: absolute;
    top: 270px;
    left: 1638px;
    width: 692px;
    .gfxm-item {
      margin-top: 80px;
      .nation {
        height: 54px;
        width: 100%;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/country/popup/bg_pupup01.png');
        background-repeat: no-repeat;
        padding-left: 70px;
        font-size: 26px;
        color: #eef3ff;
        text-shadow: 4px 2px 4px rgba(#fff, 0.87);
        line-height: 54px;
        font-weight: bold;
        font-style: italic;
      }
      .gfxm-value {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        margin-top: 22px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/country/popup/bg_pupup02.png');
        padding: 60px 38px;
        & > div {
          min-width: 49%;
          margin-top: 40px;
          &:nth-child(1),
          &:nth-child(2) {
            margin-top: 0;
          }
        }
      }
      &:first-child {
        margin-top: 0;
        .data-item {
          display: flex;
          align-items: center;
          .icon {
            width: 94px;
            height: 94px;
            margin-right: 12px;
            img {
              height: 94px;
              width: 94px;
            }
          }
          .value {
            .data-title {
              color: #fff;
              font-size: 24px;
              background: linear-gradient(90deg, rgba(#286eab, 1), rgba(#286eab, 0));
              padding: 0 15px;
              border-left: 4px solid #80c5f0;
            }
            .data-value {
              color: #86d9ff;
              span {
                &:nth-child(1) {
                  font-size: 38px;
                  font-weight: bold;
                }
                &:nth-child(2) {
                  font-size: 18px;
                }
              }
            }
          }
        }
      }
      &:last-child {
        .gfxm-value {
          & > div {
            display: flex;
            align-items: center;
            .icon {
              width: 80px;
              height: 80px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              margin-right: 8px;
            }
            .bias {
              transform: rotate(10deg);
              color: #233847;
              height: 78px;
              width: 0;
              border-right: 2px solid #233847;
              margin-right: 20px;
            }
            .data {
              display: flex;
              flex-direction: column;
              align-items: flex-start;
              .value {
                font-size: 36px;
                color: #fff;
                font-family: douyv;
              }
              .label {
                color: #98c3f4;
                font-size: 20px;
              }
            }
          }
        }
      }
    }
  }
  .popup-content {
    position: absolute;
    right: 0;
    height: 83%;
    width: 31%;
    top: 217px;
    padding: 0 140px 0 0;
    overflow-y: auto;
    .nation {
      height: 54px;
      width: 100%;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/country/popup/bg_pupup01.png');
      background-repeat: no-repeat;
      padding-left: 70px;
      font-size: 26px;
      color: #eef3ff;
      text-shadow: 4px 2px 4px rgba(#fff, 0.87);
      line-height: 54px;
      font-weight: bold;
      font-style: italic;
    }
    .popup-item {
      margin-top: 50px;
      &:first-child {
        margin-top: 0;
      }
      .popup-value {
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/country/popup/bg_pupup02.png');
      }
    }
    .project {
      display: flex;
      justify-content: space-between;
      flex-wrap: wrap;
      padding: 2px 18px 22px;
      .project-item {
        width: 194px;
        height: 90px;
        background-image: url('~@/assets/img/country/popup/icon_pupup01.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
        text-align: center;
        margin-top: 20px;
        .label {
          color: #00ddff;
          font-size: 20px;
          margin-top: 10px;
        }
        .value {
          color: #68d9ea;
          font-size: 30px;
          font-weight: bold;
          margin-top: 6px;
        }
      }
    }
    .electricity {
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;
      padding: 16px 20px 20px;
      .project-item {
        display: flex;
        align-items: center;
        margin-top: 20px;
        min-width: 34%;
        &:nth-child(1),
        &:nth-child(2) {
          margin-top: 0;
        }
        .project-icon {
          height: 50px;
          width: 50px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-right: 10px;
        }
        .project-data {
          .label {
            color: #00ddff;
            font-size: 20px;
          }
          .value {
            color: #68d9ea;
            font-size: 30px;
            font-weight: bold;
          }
        }
      }
    }
    .income {
      display: flex;
      justify-content: space-around;
      padding: 13px 10px 17px;
      .project-item {
        display: flex;
        // flex: 1;
        flex-direction: column;
        align-items: center;
        text-align: center;
        .project-icon {
          width: 68px;
          height: 108px;
          margin-top: -12px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
        }
        .label {
          color: #fff;
          font-size: 16px;
        }
        .value {
          color: #49c5ff;
          font-size: 22px;
          font-weight: bold;
          text-shadow: 0px 4px 1px rgba(43, 159, 214, 0.3);
        }
      }
    }
    .access {
      display: flex;
      flex-wrap: wrap;
      justify-content: space-around;
      padding: 40px 0 44px;
      .project-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        text-align: center;
        // min-width: 30%;
        // &:nth-child(4),
        // &:nth-child(5),
        // &:nth-child(6) {
        //   margin-top: 20px;
        // }
        .project-icon {
          width: 112px;
          height: 22px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
        }

        .label {
          color: #fff;
          font-size: 18px;
          margin-top: -4px;
        }
        .value {
          color: #49c5ff;
          font-size: 26px;
          font-weight: bold;
          text-shadow: 0px 4px 1px rgba(43, 159, 214, 0.3);
        }
      }
    }
  }
  .country-data {
    margin-top: -220px;
    width: 1042px;
    height: 142px;
    margin-left: 317px;
    display: flex;
    justify-content: center;
    align-items: center;
    .data-content {
      display: flex;
      height: 142px;
      justify-content: center;
      background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
      border: 2px solid;
      border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
      border-image-slice: 1;
      gap: 0 100px;
      ul {
        width: 100%;
        display: flex;
        justify-content: center;
        li {
          width: 386px;
          display: flex;
          align-items: center;
          .icon {
            height: 94px;
            width: 94px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            margin-right: 8px;
          }
          .bias {
            transform: rotate(10deg);
            color: #233847;
            height: 78px;
            width: 0;
            border-right: 2px solid #233847;
            margin-right: 20px;
          }
          .data {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            .value {
              font-size: 40px;
              color: #fff;
              font-family: douyv;
            }
            .label {
              color: #98c3f4;
              font-size: 20px;
            }
          }
        }
      }
      .data-item {
        display: flex;
        align-items: center;
        margin-left: 60px;
        .icon {
          height: 94px;
          width: 94px;
          margin-right: 12px;
          img {
            height: 94px;
            width: 94px;
          }
        }
        .value {
          .data-title {
            color: #fff;
            font-size: 24px;
            background: linear-gradient(90deg, rgba(#286eab, 1), rgba(#286eab, 0));
            padding: 0 15px;
            border-left: 4px solid #80c5f0;
          }
          .data-value {
            color: #86d9ff;
            span {
              &:nth-child(1) {
                font-size: 38px;
                font-weight: bold;
              }
              &:nth-child(2) {
                font-size: 18px;
              }
            }
          }
        }
      }
    }
  }
  .cn {
    position: absolute;
    left: 1745px;
    bottom: 129px;
    ul {
      width: 550px;
      li {
        width: 550px;
        display: flex;
        align-items: center;
        .icon {
          height: 94px;
          width: 94px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin-right: 18px;
        }
        .bias {
          transform: rotate(10deg);
          color: #233847;
          height: 78px;
          width: 0;
          border-right: 2px solid #233847;
          margin-right: 20px;
        }
        .data {
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          .value {
            font-size: 40px;
            color: #fff;
            font-family: douyv;
          }
          .label {
            color: #98c3f4;
            font-size: 20px;
          }
        }
      }
    }
  }
}
</style>
