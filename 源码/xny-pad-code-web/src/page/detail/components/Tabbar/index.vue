<template>
  <div class="project-data-tabbar">
    <div v-for="(item, index) in tabbar" :key="index" class="tabbar-item" :class="{ active: index === activeItem }" @click="activeItem = index">{{ item.label }}</div>
  </div>
</template>
<script>
export default {
  props: {
    tabbar: {
      type: Array,
      default: () => [],
    },
    active: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {}
  },
  computed: {
    activeItem:{
      get(){
        return this.active
      },
      set(val){
        this.$emit('update:active', val)
      }
    }
  },
  methods: {},
}
</script>
<style lang="scss" scoped>
.project-data-tabbar {
  display: flex;
  align-items: center;
  justify-content: center;
  .tabbar-item {
    height: 64px;
    min-width: 212px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/data/top-hotovoltaic02.png');
    font-size: 22px;
    color: #15a3f2;
    line-height: 64px;
    text-align: center;
  }
  .active {
    background-image: url('~@/assets/img/data/top-hotovoltaic01.png');
    color: #f2ba41;
  }
}
</style>
