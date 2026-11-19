<template>
  <div class="page-header">
    <div class="logo"></div>
    <div class="title"></div>
    <div class="dateTime">
      <div class="time">{{ formatTime }}</div>
      <div class="dateAndWeek">
        <div class="date">{{ formatDate }}</div>
        <div class="week">{{ formatWeek }}</div>
      </div>
    </div>
  </div>
</template>
<script>
import { formatDate } from '@/utils/format.js'
export default {
  data() {
    return {
      timer: null,
      formatWeek: '',
      formatTime: '',
      formatDate: '',
    }
  },
  created() {
    let curremtTime = formatDate()
    this.formatWeek = curremtTime.formatWeek
    this.formatTime = curremtTime.formatTime
    this.formatDate = curremtTime.formatDate
  },
  mounted() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    this.timer = setInterval(() => {
      let curremtTime = formatDate()
      this.formatWeek = curremtTime.formatWeek
      this.formatTime = curremtTime.formatTime
      this.formatDate = curremtTime.formatDate
    }, 1000)
  },
  methods: {},
  beforeDestroy() {
    clearInterval(this.timer)
  },
}
</script>
<style lang="scss" scoped>
.page-header {
  position: relative;
  z-index: 1;
  .dateTime {
    width: 270px;
    height: 40px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    right: 0;
    top: 42px;
    position: absolute;
    color: #fff;
    display: flex;
    align-items: center;
    .time {
      width: 165px;
      font-size: 40px;
      font-family: zk;
    }
    .dateAndWeek {
      flex: 1;
      margin-left: 12px;
      .date {
        font-size: 16px;
      }
      .week {
        font-size: 16px;
      }
    }
  }
  .logo {
    width: 329px;
    height: 28px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/header/logo.png');
    background-repeat: no-repeat;
    // left: 34px;
    top: 42px;
    position: absolute;
  }
  .title {
    width: 1259px;
    height: 125px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/header/sp_title01.png');
    background-repeat: no-repeat;
    left: 50%;
    margin-left: -630px;
    position: absolute;
  }
  .swipeBtnTop {
    top: 32px;
  }
}
</style>
