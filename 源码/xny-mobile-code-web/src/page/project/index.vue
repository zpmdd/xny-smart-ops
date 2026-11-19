<template>
  <div class="project">
    <MHeader text="项目列表" backBtn></MHeader>
    <div class="container">
      <div class="item-content item" v-for="(item, index) in list" :key="index">
        <MTittle :detail="item" :text="item.projectName || ''" :needDetail="!!item.id" @goDetail="goDetail"></MTittle>
        <div class="project-content" v-if="item.id">
          <div class="project-img" :style="{ backgroundImage: 'url(' + icons.projectBanner[item.projectTypeId] + ')' }"  @click="goDetail(item)"></div>
          <div class="descriptions">
            <div class="label">项目维度</div>
            <div class="value">{{ item.projectDimension || '' }}</div>
          </div>
          <div class="descriptions">
            <div class="label">联系人</div>
            <div class="value">{{ item.headName || '' }}</div>
          </div>
          <div class="descriptions">
            <div class="label">联系方式</div>
            <div class="value">{{ item.contactInfo || '' }}</div>
          </div>
          <div class="descriptions">
            <div class="label">项目地址</div>
            <div class="value">{{ item.province || '' }}{{ item.city || '' }}{{ item.area || '' }}{{ item.address || '' }}</div>
          </div>
        </div>
        <div class="project-content1" v-else>
          <div class="project-img"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { listProject } from '@/api/mapData'
import icons from '@/utils/icons'
export default {
  data() {
    return {
      icons: icons,
      list: [],
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      listProject().then((res) => {
        this.list = res.data.filter((item) => item.projectTypeId == 1 || item.projectTypeId == 2).concat({ projectName: '风电项目' })
      })
    },
    goDetail(data) {
      if(!data.projectTypeId){
        return
      }
      let arr = ['', 'fbsgf', 'jzsgf']
      this.$router.push({
        path: `/${arr[data.projectTypeId]}`,
      })
    },
  },
}
</script>
<style lang="scss" scoped>
.project {
  display: flex;
  flex-direction: column;
  height: 100%;
  .container {
    flex: 1;
    overflow-y: auto;
    padding-bottom: 10px;
    .item {
      margin-top: 20px;
      .project-content {
        padding: 24px 0 0;
        background-color: #fff;
        .project-img {
          height: 372px;
          width: 662px;
          margin: 0 auto;
          background-size: 100% 100%;
          background-repeat: no-repeat;
        }
        .descriptions {
          display: flex;
          justify-content: space-between;
          border-bottom: 1px solid #f0f1f4;
          padding: 0 30px;
          font-size: 28px;
          min-height: 86px;

          &:last-child {
            border: none;
          }
          .label,
          .value {
            display: flex;
            align-items: center;
          }
          .label {
            color: #999999;
            width: 200px;
          }
          .value {
            justify-content: flex-end;
            color: #333333;
            width: 460px;
          }
        }
      }
      .project-content1 {
        padding: 24px 0 60px;
        background-color: #fff;
        .project-img {
          height: 346px;
          width: 384px;
          margin: 0 auto;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/project/list_bg01.png');
        }
      }
    }
  }
}
</style>
