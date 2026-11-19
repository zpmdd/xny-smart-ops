<template>
  <div class="page-header">
    <i class="el-icon-arrow-left" @click="back" v-if="backBtn"></i>
    <div class="title">{{ text }}</div>
  </div>
</template>
<script>
export default {
  props: {
    text: {
      type: String,
      default: '',
    },
    backBtn: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {}
  },
  watch: {},
  methods: {
    back() {
      this.$router.go(-1)
    },
  },
}
</script>
<style lang="scss" scoped>
.page-header {
  height: 166px;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  position: relative;
  color: #333333;
  font-size: 34px;
  font-weight: bold;
  background-color: #fff;
  i {
    position: absolute;
    left: 20px;
    font-weight: bold;
    font-size: 38px;
  }
}
</style>
