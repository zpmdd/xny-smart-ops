<template>
  <div class="tab-bar" v-if="!child">
    <div class="tab-item" v-for="(item, index) in tabList" :key="index" :class="actived == index ? 'active' : ''" @click="handleTab(item, index)">{{ item.name }}</div>
  </div>
  <div class="tab-bar1" v-else>
    <div class="tab-item" v-for="(item, index) in tabList" :key="index" :class="actived == index ? 'active' : ''" @click="handleTab(item, index)">{{ item.name }}</div>
  </div>
</template>
<script>
export default {
  props: {
    active: {
      type: Number,
      default: 0,
    },
    tabList: {
      type: Array,
      default: () => [],
    },
    child: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    actived: {
      get() {
        return this.active
      },
      set(val) {
        this.$emit('update:active', val)
      },
    },
  },
  mounted() {
    let _index = this.tabList.findIndex((item) => item.path == this.$route.path)
    this.actived = _index == -1 ? this.actived : _index
  },
  methods: {
    handleTab(obj, index) {
      this.actived = index
      this.$router.replace({ path: obj.path })
    },
  },
}
</script>
<style lang="scss" scoped>
.tab-bar {
  display: flex;
  height: 72px;
  color: #666666;
  font-size: 28px;
  background-color: #fff;
  .tab-item {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 1;
  }
  .active {
    color: #292929;
    font-weight: bold;
    position: relative;
    &::after {
      content: '';
      width: 32px;
      height: 6px;
      background: #004fdf;
      border-radius: 3px;
      position: absolute;
      bottom: 0;
      left: 50%;
      margin-left: -16px;
    }
  }
}
.tab-bar1 {
  display: flex;
  height: 76px;
  margin: 20px 0 0;
  border-radius: 38px;
  color: #333333;
  font-size: 26px;
  background: rgba($color: #004fdf, $alpha: 0.06);
  .tab-item {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: center;
    border-radius: 38px;
  }
  .active {
    color: #fff;
    background-color: #004fdf;
  }
}
</style>
