<template>
  <div class="dialog" v-if="show">
    <div class="dialog-container" ref="container" :style="{ marginLeft: -1 * (width / 2) + 'px' }"><slot></slot></div>

    <div class="mask" @click.stop="handleDialog"></div>
  </div>
</template>
<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      width: 0,
    }
  },
  updated() {
    if (this.show) {
      this.$nextTick(() => {
        this.width = this.$refs.container.offsetWidth
      })
    }
  },
  computed: {
    show: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      },
    },
  },
  methods: {
    handleDialog() {
      this.closeDialog()
    },
    closeDialog() {
      this.show = false
      this.$emit('close')
    },
  },
}
</script>
<style lang="scss" scoped>
.dialog {
  height: 1600px;
  width: 100vw;
  .mask {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    z-index: 2021;
    background-color: rgba($color: #000000, $alpha: 0.3);
  }
  .dialog-container {
    position: absolute;
    left: 50%;
    top: 318px;
    z-index: 2022;
  }
}
</style>
