<script setup>
import { ref, onMounted, inject } from "vue";
import { useRouter } from "vue-router";
import { useToken } from "@/stores/user.js";
import { storeToRefs } from "pinia";
import { sysUser } from "@/api/system.js";

const { $vailtAccount, $vailtPassword } = inject("globalProperties");

const form = ref(null);

const loginForm = ref({
  username: "",
  password: "",
});

const vailtAccount = (rule, value, callback) => {
  if (value.trim() == "") {
    callback(new Error("请输入用户名称"));
  } else if (!$vailtAccount(value)) {
    callback(new Error("用户名格式不正确"));
  } else {
    callback();
  }
};

const vailtPassword = (rule, value, callback) => {
  if (value.trim() == "") {
    callback(new Error("请输入登录密码"));
  } else if (!$vailtPassword(value)) {
    callback(new Error("密码格式不正确"));
  } else {
    callback();
  }
};

const rules = ref({
  username: [
    {
      validator: vailtAccount,
      trigger: "blur",
    },
  ],
  password: [
    {
      validator: vailtPassword,
      trigger: "blur",
    },
  ],
});

const savePsw = ref(false);
const userStore = useToken();
const router = useRouter();
const { token } = storeToRefs(userStore);

onMounted(() => {
  if (localStorage.getItem("savePsw") == "true") {
    loginForm.value.username = localStorage.getItem("username");
    loginForm.value.password = localStorage.getItem("password");
    savePsw.value = true;
  }
});

const handleSunmit = (el) => {
  form.value.validate((vaild) => {
    if (vaild) {
      if (savePsw.value) {
        localStorage.setItem("username", loginForm.value.username);
        localStorage.setItem("password", loginForm.value.password);
      }
      localStorage.setItem("savePsw", savePsw.value);
      let config = {
        accountNumber: loginForm.value.username,
        password: loginForm.value.password,
      };
      sysUser(config).then((res) => {
        userStore.setToken(res.data.tokenValue || "");
        userStore.setUserName(res.data.userName || "");
        router.push("/");
      });
    }
  });
};
</script>
<template>
  <div class="login p-icon">
    <div class="login-form p-icon">
      <div class="form">
        <div class="title">欢迎登录</div>
        <el-form :model="loginForm" ref="form" :rules="rules" label-width="0">
          <el-form-item prop="username">
            <el-input
              class="username"
              v-model="loginForm.username"
              placeholder="请输入用户名称"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              class="password"
              v-model="loginForm.password"
              placeholder="请输入登录密码"
              show-password
              clearable
            ></el-input>
          </el-form-item>
        </el-form>

        <el-checkbox v-model="savePsw">记住密码</el-checkbox>
        <div class="submit" @click="handleSunmit()">登录</div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.login {
  background-image: url("@/assets/img/bg_bg.png");
  display: flex;
  justify-content: center;
  align-items: center;
  .login-form {
    width: 1468px;
    height: 810px;
    background-image: url("@/assets/img/bg_bg01.png");
    display: flex;
    align-items: center;
    .form {
      width: 420px;
      margin-left: 865px;
      .title {
        font-weight: 400;
        font-size: 32px;
        color: #333333;
        line-height: 42px;
        margin-bottom: 56px;
        position: relative;
        &::before {
          content: "";
          position: absolute;
          left: 0;
          bottom: -18px;
          width: 38px;
          height: 4px;
          background: #2ea48b;
        }
      }
      .username,
      .password {
        height: 62px;
        border-radius: 2px;
        border: 1px solid #e4e4e4;
        font-weight: 400;
        font-size: 20px;
        color: #999999;
        overflow: hidden;
        ::v-deep(.el-input__wrapper) {
          background: #f8f8f8;
          border-radius: 2px;
        }
      }
      .password {
        margin-top: 10px;
      }
      .submit {
        margin-top: 30px;
        height: 66px;
        background: #3873f9;
        border-radius: 6px;
        text-align: center;
        line-height: 66px;
        font-size: 24px;
        color: #ffffff;
        cursor: pointer;
        user-select: none;
        &:active {
          background-color: rgba($color: #3873f9, $alpha: 0.8);
        }
      }
    }
  }
}
</style>
