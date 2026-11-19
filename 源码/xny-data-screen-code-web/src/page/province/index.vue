<template>
  <div class="province view">
    <Side></Side>
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="tip-icon" :class="iconName(item)" v-for="(item, index) in mapTip" :key="index" :style="{ 'background-image': 'url(' + projectIcons.provinceMapIcon[item.projectTypeId] + ')' }">
        <div class="mask" @click.stop="handleTip(item)">
          <div class="popover" v-if="popoverShow && currentActive == item.id">
            <div class="close"><i class="el-icon-close" @click.stop="popoverShow = false"></i></div>
            <div class="label">
              <span>{{ item.projectTypeName || '' }}</span>
            </div>
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
            <div class="detail-btn" @click.stop="handleView(item)"><span>查看详情</span></div>
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
import { sysDict } from '@/api/dict.js'
import { mapData, listProject } from '@/api/mapData.js'
import icons from '@/utils/icons.js'
const Side = () => import('@/components/Side')
export default {
  data() {
    return {
      type: '',
      projectIcons: icons,
      projectList: [],
      mapTip: [],
      currentActive: '',
      popoverShow: false,

      //项目总数量
      totalItems: {},
      //   总发电量
      grossGeneration: {},
      //   总装机容量
      grossCapacity: {},
      //   总收益
      totalRevenue: {},
      //社会效益
      socialBenefit: {},
      //总上网
      totalAccess: {},
    }
  },
  computed: {
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
    iconName() {
      return (obj) => {
        let arr = ['', 'fbsgf', 'jzsgf', 'cnxm', 'cdxm', 'fdxm', 'sdxm']
        return arr[obj.projectTypeId]
      }
    },
  },
  async created() {
    // this.type = this.$route.query.type || ''
    await this.getProjectList()
    await this.getMapData()
    let data = {
      projectTypeId: '',
      provinceCode: '610000',
    }
    listProject(data).then((res) => {
      this.mapTip = res.data
    })
  },
  mounted() {
    document.addEventListener('click', () => {
      this.popoverShow = false
    })

    this.$bus.$on('refreshData', () => {
      this.getMapData()
    })
  },
  methods: {
    handleView(obj) {
      let pathObj = {
        1: '/fbsgf',
        2: '/jzsgf',
        3: '/cnxm',
        4: '/cdxm',
        5: '/fdxm',
      }
      this.$router.push({
        path: pathObj[obj.projectTypeId],
        query: {
          id: obj.id,
          projectName: obj.projectName,
        },
      })
    },
    handleTip(obj) {
      this.currentActive = obj.id
      this.popoverShow = true
    },
    getProjectList() {
      return new Promise((resolve, reject) => {
        sysDict({
          type: 'project_type',
        })
          .then((res) => {
            this.projectList = res.data
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
          projectTypeId: this.type,
          provinceCode: '610000',
        }
        mapData(data)
          .then((res) => {
            let data = res.data
            this.totalItems = data.totalItems || {}
            this.grossGeneration = data.grossGeneration || {}
            this.grossCapacity = data.grossCapacity || {}
            this.totalRevenue = data.totalRevenue || {}
            this.socialBenefit = data.socialBenefit || {}
            this.totalAccess = data.totalAccess || {}
            resolve(res.data)
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
.province {
  background-image: url('~@/assets/img/province/bg_mosan.png');
  position: relative;
  .tip-icon {
    background-size: 100% 100%;
    background-repeat: no-repeat;
    position: absolute;
    .mask {
      position: relative;
      height: 66px;
      width: 57px;
      margin-top: 25px;
      cursor: pointer;
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
          i {
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
  .fdxm {
    width: 345px;
    height: 195px;
    left: 404px;
    top: 153px;
    .mask {
      margin-left: 31px;
      .popover {
        margin-left: -306px;
      }
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
