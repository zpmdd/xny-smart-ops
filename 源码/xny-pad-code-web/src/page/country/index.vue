<template>
  <div class="country">
    <div class="map">
      <div class="shanxi-icon" v-if="hasProject" @click.stop="handleProvince"></div>
    </div>
    <Header></Header>
    <div class="alarmData">
      <div class="alarmData-item" v-for="(item, index) in  []" :key="index" :style="`background-image: url(${projectIcons.alarmData[(index % 2) + 1]})`">
        <div class="label">{{ item.label || '' }}{{ item.unit ? `(${item.unit})` : '' }}</div>
        <div class="value">{{ item.value }}</div>
      </div>
    </div>
    <div class="country-data">
      <div class="data-content" v-if="pageType1.includes(type) || pageType2.includes(type) || pageType3.includes(type) || pageType4.includes(type)">
        <div class="data-item" v-for="(item, index) of initData?.devOpsData?.childs || []" :key="index">
          <div class="icon"><img :src="projectIcons.devOpsData[index + 1]" alt="" /></div>
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
    <div class="fbsgf" v-if="pageType2.includes(type) || pageType3.includes(type) || pageType4.includes(type)">
      <ul class="socialBenefitVO1">
        <li v-for="(item, index) in initData?.socialBenefitVO?.childs || []" :key="index">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.socialBenefitVO1[index + 1] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ item.value | formatNum }}</div>
            <div class="label" v-html="item.label + (item.unit ? `(${item.unit})` : '')"></div>
          </div>
        </li>
      </ul>
      <ul>
        <li v-if="initData.projectNumberVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[1] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.projectNumberVO.value | formatNum }}</div>
            <div class="label">{{ initData.projectNumberVO.label }}{{ initData.projectNumberVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.powerGenerationVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[2] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.powerGenerationVO.value | formatNum }}</div>
            <div class="label">{{ initData.powerGenerationVO.label }}{{ initData.powerGenerationVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.installedCapacityVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[3] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.installedCapacityVO.value | formatNum }}</div>
            <div class="label">{{ initData.installedCapacityVO.label }}{{ initData.installedCapacityVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.incomeVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.cnxmAndCdxm[type == 'cnxm' ? 6 : 4] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.incomeVO.value | formatNum }}</div>
            <div class="label">{{ initData.incomeVO.label }}{{ initData.incomeVO.unit }}</div>
          </div>
        </li>
        <li v-if="initData.powerGridVO">
          <div class="icon" :style="{ 'background-image': 'url(' + projectIcons.fbsgf[5] + ')' }"></div>
          <div class="bias"></div>
          <div class="data">
            <div class="value">{{ initData.powerGridVO.value | formatNum }}</div>
            <div class="label">{{ initData.powerGridVO.label }}{{ initData.powerGridVO.unit }}</div>
          </div>
        </li>
      </ul>
    </div>
    <div class="popup-control" @click="handlePopup" v-if="type == 'xmfb'"></div>
    <el-drawer v-if="type == 'xmfb'" :visible.sync="drawer" direction="rtl" custom-class="country-popup" :show-close="false" size="32%">
      <div class="close"><i class="el-icon-close" @click="drawer = false"></i></div>
      <div class="popup-content">
        <div class="popup-item" v-if="initData.socialBenefitVO">
          <div class="nation">
            <span>{{ initData.socialBenefitVO.label }} {{ initData.socialBenefitVO.value }} {{ initData.socialBenefitVO.unit ? `(${initData.socialBenefitVO.unit})` : '' }}</span>
          </div>
          <div class="popup-value socialBenefitVO">
            <div class="project-item" v-for="(item, index) in initData?.socialBenefitVO?.childs || []" :key="index">
              <div class="project-icon" :style="{ 'background-image': 'url(' + projectIcons.socialBenefitVO[index + 1] + ')' }"></div>
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
        <div class="popup-item" v-if="initData.projectNumberVO">
          <div class="nation">
            <span>{{ initData.projectNumberVO.label }} {{ initData.projectNumberVO.value }} {{ initData.projectNumberVO.unit ? `(${initData.projectNumberVO.unit})` : '' }}</span>
          </div>
          <div class="popup-value income">
            <div class="project-item" v-for="(item, index) in initData?.projectNumberVO?.childs || []" :key="index">
              <div class="project-icon" :style="{ 'background-image': 'url(' + projectIcons.incomeVO[index + 1] + ')' }"></div>
              <div class="value">{{ item.value }}</div>
              <div class="label">{{ item.label }}</div>
            </div>
          </div>
        </div>
        <div class="popup-item" v-if="initData.powerGenerationVO">
          <div class="nation">
            <span>{{ initData.powerGenerationVO.label }} {{ initData.powerGenerationVO.value }} {{ initData.powerGenerationVO.unit ? `(${initData.powerGenerationVO.unit})` : '' }}</span>
          </div>
          <div class="popup-value project">
            <div class="project-item" v-for="(item, index) of initData?.powerGenerationVO?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="popup-item" v-if="initData.installedCapacityVO">
          <div class="nation">
            <span>{{ initData.installedCapacityVO.label }} {{ initData.installedCapacityVO.value }} {{ initData.installedCapacityVO.unit ? `(${initData.installedCapacityVO.unit})` : '' }}</span>
          </div>

          <div class="popup-value project">
            <div class="project-item" v-for="(item, index) of initData?.installedCapacityVO?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="popup-item" v-if="initData.incomeVO">
          <div class="nation">
            <span>{{ initData.incomeVO.label }} {{ initData.incomeVO.value }} {{ initData.incomeVO.unit ? `(${initData.incomeVO.unit})` : '' }}</span>
          </div>
          <div class="popup-value project">
            <div class="project-item" v-for="(item, index) of initData?.incomeVO?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
        <div class="popup-item" v-if="initData.powerGridVO">
          <div class="nation">
            <span>{{ initData.powerGridVO.label }} {{ initData.powerGridVO.value }} {{ initData.powerGridVO.unit ? `(${initData.powerGridVO.unit})` : '' }}</span>
          </div>
          <div class="popup-value project">
            <div class="project-item" v-for="(item, index) of initData?.powerGridVO?.childs || []" :key="index">
              <div class="label">{{ item.label }}</div>
              <div class="value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>
<script>
import Header from '@/components/Header'
import icons from '@/utils/icons.js'
import { mapData, listProject } from '@/api/mapData.js'
import { sysDict } from '@/api/dict.js'
export default {
  name: 'country',
  data() {
    return {
      type: '',
      projectIcons: icons,
      projectList: [],
      pageType1: ['xmfb'],
      pageType2: ['fbsgf', 'jzsgf', 'fdxm', 'sdxm'],
      pageType3: ['cnxm'],
      pageType4: ['cdxm'],
      project: ['', 'fbsgf', 'jzsgf', 'cnxm', 'cdxm', 'fdxm', 'sdxm'],
      initData: {
        projectNumberVO: {},
        powerGenerationVO: {},
        installedCapacityVO: {},
        incomeVO: {},
        powerGridVO: {},
        socialBenefitVO: {},
      },
      drawer: false,
      hasProject: false,
    }
  },
  async created() {
    this.type = this.$route.query.type
    await this.getProjectList()
    await this.getMapData()
    let data = {
      projectTypeId: this.projectList.find((item) => item.type === this.type)?.id || '',
    }
    listProject(data).then((res) => {
      this.hasProject = res.data.length > 0
    })
  },
  methods: {
    handleProvince() {
      console.log('handleProvince')
      this.$router.push({
        path: '/province',
        query: {
          type: this.type,
        },
      })
    },
    handlePopup() {
      this.popupShow = true
      this.drawer = true
    },
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
        }
        mapData(data).then((res) => {
          if (this.pageType1.includes(this.type)) {
            // 项目分布
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.grossGeneration,
              installedCapacityVO: res.data.grossCapacity,
              incomeVO: res.data.totalRevenue,
              powerGridVO: res.data.totalAccess,
              socialBenefitVO: res.data.socialBenefit,
              alarmData: res.data.alarmData,
              devOpsData: res.data.devOpsData,
            }
          }
          if (this.pageType2.includes(this.type)) {
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.grossGeneration,
              installedCapacityVO: res.data.grossCapacity,
              incomeVO: res.data.totalRevenue,
              powerGridVO: res.data.totalAccess,
              socialBenefitVO: res.data.socialBenefit,
              alarmData: res.data.alarmData,
              devOpsData: res.data.devOpsData,
            }
          }
          if (this.pageType3.includes(this.type)) {
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.chargeToday,
              installedCapacityVO: res.data.totalCharge,
              incomeVO: res.data.totalDischarge,
              powerGridVO: res.data.currentDischarge,
              alarmData: res.data.alarmData,
              devOpsData: res.data.devOpsData,
            }
          }
          if (this.pageType4.includes(this.type)) {
            this.initData = {
              projectNumberVO: res.data.totalItems,
              powerGenerationVO: res.data.totalChargeTime,
              installedCapacityVO: res.data.totalCharge,
              incomeVO: res.data.totalRevenue,
              powerGridVO: res.data.totalChargeTimes,
              alarmData: res.data.alarmData,
              devOpsData: res.data.devOpsData,
            }
          }
          resolve()
        })
      })
    },
  },

  components: {
    Header,
  },
}
</script>
<style lang="scss" scoped>
.country {
  height: 1600px;
  position: relative;
  overflow: hidden;
  // padding-top: 47px;
  .alarmData {
    position: absolute;
    margin-top: 17px;
    padding: 0 229px;
    display: flex;
    flex-wrap: wrap;
    gap: 30px 30px;
    .alarmData-item {
      height: 94px;
      width: 270px;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      padding-left: 70px;
      padding-top: 10px;
      .label,
      .value {
        width: calc(100% - 70px);
      }
      .label {
        font-size: 20px;
        color: #1ff2ff;
      }
      .value {
        color: #e8feff;
        font-size: 30px;
        font-weight: bold;
        margin-top: 4px;
        padding-left: 10px;
      }
      &:nth-child(2n) {
        .label {
          color: #e1afa5;
        }
      }
    }
  }
  .map {
    position: absolute;
    width: 3147px;
    height: 2141px;
    top: 0;
    left: 0;
    background-size: 100% 100%;
    margin-top: -230px;
    margin-left: -370px;
    background-image: url('~@/assets/img/country/item_bg_nationwide.png');
    .shanxi-icon {
      width: 123px;
      height: 146px;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/country/icon_coordinate_sx.png');
      margin-top: 900px;
      margin-left: 1590px;
    }
  }
  .popup-control {
    width: 270px;
    height: 280px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/country/icon_left_slide.png');
    position: absolute;
    right: 0;
    top: 50%;
    margin-top: -140px;
  }
  ::v-deep .el-drawer {
    background-color: transparent;
    box-shadow: none;
    height: 95%;
    top: 2.5%;
    .el-drawer__header {
      display: none;
    }
    .el-drawer__body {
      background-image: url('~@/assets/img/country/popup/bg_pupup.png');
      background-repeat: no-repeat;
      background-size: 100% 100%;
      display: flex;
      flex-direction: column;
      .close {
        display: flex;
        justify-content: flex-end;
        padding: 0 62px;
        .el-icon-close {
          font-size: 50px;
          color: #fff;
          margin-top: 62px;
        }
      }
      .popup-content {
        padding: 0 78px 0 98px;
        flex: 1;
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
          margin-top: 30px;
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
        .socialBenefitVO {
          display: flex;
          align-items: center;
          flex-wrap: wrap;
          padding: 27px 0;
          .project-item {
            display: flex;
            align-items: center;
            margin-left: 60px;
            .project-icon {
              height: 94px;
              width: 94px;
              margin-right: 12px;
              background-size: 100% 100%;
              background-repeat: no-repeat;
              img {
                height: 94px;
                width: 94px;
              }
            }
            .value {
              .data-title {
                color: #fff;
                font-size: 18px;
                background: linear-gradient(90deg, rgba(#286eab, 1), rgba(#286eab, 0));
                padding: 0 15px;
                border-left: 4px solid #80c5f0;
              }
              .data-value {
                color: #86d9ff;
                span {
                  &:nth-child(1) {
                    font-size: 28px;
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
    }
  }
  .country-data {
    position: absolute;
    width: 100%;
    bottom: 120px;
    display: flex;
    justify-content: center;
    .data-content {
      display: flex;
      justify-content: center;
      padding: 30px 200px;
      background: linear-gradient(90deg, rgba(14, 33, 61, 0), rgba(14, 33, 61, 0.6) 50%, rgba(14, 33, 61, 0));
      border: 2px solid;
      border-image: linear-gradient(to right, rgba(#51c8ff, 0), rgba(#51c8ff, 1) 50%, rgba(#51c8ff, 0));
      border-image-slice: 1;
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
  .fbsgf {
    position: absolute;
    right: 0;
    bottom: 286px;
    ul {
      width: 550px;
      padding: 22px 0;
      li {
        width: 550px;
        display: flex;
        align-items: center;
        margin-top: 18px;
        &:nth-child(1) {
          margin-top: 0;
        }
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
    .socialBenefitVO1 {
      padding-bottom: 22px;
      position: relative;
      &::after {
        width: 373px;
        height: 1px;
        background: linear-gradient(270deg, rgba(255, 255, 255, 0.01) 0%, rgba(57, 175, 189, 0.94) 47%, rgba(173, 245, 254, 0) 100%);
        content: '';
        position: absolute;
        bottom: 0;
      }
    }
  }
}
</style>
