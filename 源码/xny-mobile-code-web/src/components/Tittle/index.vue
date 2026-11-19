<template>
  <div class="text-tittle" @click="goDetail()">
    <div class="text-tittle-l">
      <div class="text-tittle-icon"></div>
      <div class="text-tittle-text">{{ text }}</div>
    </div>
    <div class="detail-icon" v-if="needDetail"></div>
    <slot></slot>
  </div>
</template>
<script>
export default {
  props: {
    text: {
      type: String,
      default: '',
    },
    needDetail: {
      type: Boolean,
      default: false,
    },
    detail: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  methods: {
    goDetail() {
      this.$emit('goDetail', this.detail)
    },
  },
}
</script>
<style lang="scss" scoped>
.text-tittle {
  height: 80px;
  background: linear-gradient(90deg, #e9f1ff 0%, #ffffff 100%);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  position: relative;
  .text-tittle-l {
    display: flex;
    align-items: center;
    .text-tittle-icon {
      height: 30px;
      width: 30px;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-image: url('~@/assets/img/common/icon_charging02.png');
    }
    .text-tittle-text {
      margin-left: 15px;
      color: #292929;
      font-size: 32px;
      font-weight: bold;
    }
  }

  .detail-icon {
    height: 24px;
    width: 24px;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/common/list_advance.png');
    top: 28px;
    right: 20px;
    position: absolute;
  }
}
</style>
