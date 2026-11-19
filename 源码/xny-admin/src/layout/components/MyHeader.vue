<script setup>
import { storeToRefs } from "pinia";
import { useToken } from "@/stores/user.js";

const userStore = useToken();
const { userName } = storeToRefs(userStore);

const logout = () => {
  userStore.clearToken();
};
</script>
<template>
  <div class="headers p-icon">
    <div class="logo">新能源 + 智慧运维后台管理系统</div>
    <div class="user">
      <div class="avatar p-icon"></div>
      <div>{{ userName }}</div>
      <el-divider direction="vertical" />
      <div class="logout" @click="logout">
        <el-icon><SwitchButton /></el-icon>
        &nbsp; 退出登录
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.headers {
  height: 80px;
  background-image: url("@/assets/img/bg_head.png");
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 38px;
  .logo {
    font-family: douyv;
    font-weight: normal;
    font-size: 22px;
    color: #ffffff;
  }
  .user {
    display: flex;
    align-items: center;
    gap: 0 20px;
    font-size: 18px;
    color: #ffffff;
    .avatar {
      height: 50px;
      width: 50px;
      background-image: url("@/assets/img/avatar1.png");
    }
    .logout {
      display: flex;
      align-items: center;
      cursor: pointer;
    }
  }
}
</style>
