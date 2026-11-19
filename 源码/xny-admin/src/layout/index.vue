<template>
  <div class="app-wrapper">
    <MyHeader></MyHeader>
    <Main class="main"></Main>
  </div>
</template>
<script setup>
import Main from "./components/Main.vue";
import MyHeader from "./components/MyHeader.vue";
</script>
<style lang="scss">
.app-wrapper {
  height: 100%;
  .main {
    height: calc(100% - 80px);
    background-color: #f6f6f6;
  }
}
</style>
