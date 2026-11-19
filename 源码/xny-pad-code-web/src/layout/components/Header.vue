<template>
  <div class="header" :class="{ gray: !isHome }">
    <div class="header-l">
      <div class="logo"></div>
      <span class="" style="opacity: 0.6; margin: 0 25px 0 32px">丨</span>
      <div class="title"><span>源网荷储一体化系统</span><span> 欢迎您!</span></div>
    </div>
    <div class="header-r">
      <div v-if="!isHome" @click="$router.push('/')"><i class="iconfont icon-shouye-zhihui"></i> 返回首页</div>
      <div>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link" :style="{ color: isHome ? '#fff' : '#333', fontSize: '16px' }">
            {{ $store.getters.userInfo.userName }}
            <i class="el-icon-arrow-down"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="loginout">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <el-badge :value="total" :hidden="!total" :max="99" class="item">
        <div @click="$router.push({ path: '/completed' })" :style="{ color: isHome ? '#fff' : '#333', fontSize: '16px' }"><i class="el-icon-s-operation" style="font-size: 18px"></i> 任务列表</div>
      </el-badge>
    </div>
  </div>
</template>
<script>
import { carouselAndMessage } from '@/api/message.js'
export default {
  data() {
    return {
      totalNumberOfTasks: 0
    }
  },
  computed: {
    messageQuery() {
      return this.$store.getters.messageQuery
    },
    isHome() {
      return this.$route.path === '/index'
    },
    total() {
      return this.totalNumberOfTasks
    }
  },
  watch: {
    messageQuery() {
      this.getTotalTask()
    }
  },
  created() {
    this.getTotalTask()
  },
  mounted() {
    clearInterval(this.timer)
    this.timer = null
    if (this.timer === null) {
      this.timer = setInterval(() => {
        this.getTotalTask()
      }, 1000 * 60)
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'loginout') {
        this.loginout()
      }
    },
    loginout() {
      this.$store.dispatch('user/resetToken').then(() => {
        location.reload()
      })
    },
    getTotalTask() {
      carouselAndMessage().then(res => {
        this.totalNumberOfTasks = parseInt(res.data.messgeCount)
      })
    }
  },
  destroyed: function() {
    // 每次离开当前界面时，清除定时器
    clearInterval(this.timer)
    this.timer = null
  }
}
</script>
<style lang="scss">
@import url('~@/assets/style/home/iconfont.css');
.header {
  height: 72px;
  min-width: 1200px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30px;
  border-bottom: 1px solid #fff;
  .header-l {
    display: flex;
    align-items: center;
    font-size: 16px;
    color: #fff;
    .logo {
      background-image: url('~@/assets/img/logo.png');
      height: 38px;
      width: 450px;
    }
    .title {
      span {
        &:first-child {
          font-weight: bold;
        }
      }
    }
  }
  .header-r {
    display: flex;
    align-items: center;
    & > div {
      padding: 0 22px;
      border-right: 2px solid rgba($color: #fff, $alpha: 0.6);
      cursor: pointer;
      font-size: 16px;
      color: #333;
      &:last-child {
        border: none;
      }
    }
  }
}
.gray {
  border-bottom: 1px solid rgba($color: #626262, $alpha: 0.4);
  .header-l {
    color: #fff;
    & > span {
      color: #333;
    }
    .title {
      span {
        &:first-child {
          color: #16a787;
        }
        &:last-child {
          color: #333333;
        }
      }
    }
  }
  .header-r {
    & > div {
      border-right: 2px solid rgba($color: #aaa, $alpha: 0.6);
    }
  }
}
</style>
