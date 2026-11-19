<template>
  <RouterView class="container" />
</template>
<script setup>
import { ref, inject, nextTick } from "vue";

const { $bus } = inject("globalProperties");

let resizeTimer = ref(null);
nextTick(() => {
  initFontSize();
});
window.addEventListener("resize", () => {
  // 清除当前定时器
  clearTimeout(resizeTimer.value);
  // 设置新的定时器，在50毫秒后重新初始化字体大小并触发全局窗口尺寸改变事件
  resizeTimer.value = setTimeout(() => {
    initFontSize();
    $bus.emit("onresize");
  }, 50);
});
function initFontSize() {
  // 获取本地存储的视图高度值
  let width = document.documentElement.clientWidth || document.body.clientWidth;

  // 计算并设置html元素的基础字体大小（rem单位）
  const rem = Number(width / 100).toFixed(4);
  document.querySelector("html").style.fontSize = `${rem}px`;
}
</script>
<style scoped>
.container {
  height: 100%;
}
</style>
