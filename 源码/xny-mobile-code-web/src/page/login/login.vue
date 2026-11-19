<template>
  <div class="login page">
    <div class="top">
      <div class="top-bg">
        <div class="container">
          您好，欢迎使用
          <br />
          新能源+智慧运维管理平台
        </div>
      </div>
      <div class="login-form container">
        <el-form :model="form" ref="dynamicValidateForm" :rules="roles" label-width="0px">
          <div class="form-label">账号</div>
          <el-form-item prop="accountNumber">
            <el-input v-model="form.accountNumber" placeholder="请输入账号" @input="(value) => (form.accountNumber = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
          </el-form-item>
          <div class="form-label">密码</div>
          <el-form-item prop="password">
            <el-input v-model="form.password" placeholder="请输入登录密码" :show-password="true" @input="(value) => (form.password = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
          </el-form-item>
          <el-button class="submit-btn" type="primary" size="default" @click="handleSubmit">提交</el-button>
        </el-form>
      </div>
    </div>
    <div class="logo"></div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import htmlFontSize from '@/utils/htmlFontSize.js'
export default {
  mixins: [htmlFontSize],
  data() {
    const validacount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('账号不能为空'))
      }
      setTimeout(() => {
        console.log(this.vailtAccount(value))
        if (!this.vailtAccount(value)) {
          callback(new Error('账号格式错误'))
        } else {
          callback()
        }
      })
    }
    const validPsw = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('密码不能为空'))
      }
      setTimeout(() => {
        if (!this.vailtPassword(value)) {
          callback(new Error('密码格式错误'))
        } else {
          callback()
        }
      })
    }
    return {
      opactiyVideo: false,
      pswType: 'password',
      form: {
        accountNumber: '',
        password: '',
      },
      codeUrl: '',
      imgKey: 0,
      savePsw: false,
      roles: {
        accountNumber: [{ required: true, validator: validacount, trigger: 'blur' }],
        password: [{ required: true, validator: validPsw, trigger: 'blur' }],
      },
    }
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.user.userInfo,
    }),
  },
  created() {
    if (localStorage.getItem('_account') && localStorage.getItem('_password')) {
      this.form.accountNumber = localStorage.getItem('_account')
      this.form.password = localStorage.getItem('_password')
    }
  },
  mounted() {},
  methods: {
    playVideo() {
      this.$refs.video.play()
    },
    handleSubmit() {
      this.$refs['dynamicValidateForm'].validate((valid) => {
        if (valid) {
          this.$store.dispatch('user/login', this.form).then((res) => {
            try {
              localStorage.setItem('_account', this.form.accountNumber)
              localStorage.setItem('_password', this.form.password)
            } catch (error) {}
            this.$router.push('/')
          })
        }
      })
    },
  },
}
</script>
<style lang="scss" scope>
.login {
  .top {
    flex: 1;
    .top-bg {
      height: 523px;
      width: 100%;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/login/register_bg01.png');
      font-size: 32px;
      color: #333333;
      font-weight: bold;
      padding-top: 190px;
    }
    .login-form {
      padding: 60px 50px 74px;
      background: #fff;
      border-radius: 30px;
      margin-top: -100px;
      .form-label {
        font-size: 30px;
        color: #292929;
        font-weight: bold;
      }
      input {
        height: 80px;
        background-color: #f2f7fa;
        border-radius: 10px;
        font-size: 26px;
        margin-top: 24px;
        border: none;
      }
      .el-form-item__error {
        font-size: 22px;
      }
      .submit-btn {
        width: 100%;
        height: 80px;
        background: #3775f2;
        border-radius: 10px;
        margin-top: 66px;
        font-size: 26px;
      }
      .el-input__suffix {
        height: 80px;
        margin-top: 24px;
        right: 30px;
        .el-input__suffix-inner {
          i {
            font-size: 30px;
            line-height: 80px;
          }
        }
      }
    }
  }

  .logo {
    width: 463px;
    height: 39px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/login/register_logo.png');
    margin: 0 auto;
    margin-bottom: 75px;
  }
}
</style>
