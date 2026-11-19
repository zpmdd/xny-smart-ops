<template>
  <div class="title-comtainer">
    <div class="title-l">{{ label }}</div>
  </div>
</template>
<script>
export default {
  props: {
    label: {
      type: String,
      default: '',
    },
  },
}
</script>
<style lang="scss" scoped>
.title-comtainer {
  width: 438px;
  height: 48px;
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-image: url('~@/assets/img/common/sp_title.png');
  padding-left: 105px;
  margin-left: -20px;
  .title-l {
    font-family: douyv;
    font-size: 18px;
    color: #ffffff;
    line-height: 48px;
    white-space: nowrap;
  }
}
</style>
