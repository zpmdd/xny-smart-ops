<template>
  <div class="page-header">
    <div class="title"></div>
    <div class="back" @click="back"></div>
  </div>
</template>
<script>
export default {
  methods: {
    back() {
      this.$router.go(-1)
    },
  },
}
</script>
<style lang="scss" scoped>
.page-header {
  position: sticky;
  height: 198px;
  z-index: 1;
  .title {
    width: 2183px;
    height: 126px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/country/head_title.png');
    margin: 0 auto;
    top: 47px;
    left: 50%;
    margin-left: -1091px;
    position: absolute;
  }
  .back {
    width: 164px;
    height: 46px;
    background-size: 100% 100%;
    background-image: url('~@/assets/img/country/back.png');
    position: absolute;
    top: 113px;
    left: 54px;
  }
}
</style>
