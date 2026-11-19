<template>
  <div class="login">
    <div class="login-form-box">
      <div class="title"></div>
      <div class="login-form">
        <el-form :model="form" ref="dynamicValidateForm" :rules="roles" class="demo-dynamic">
          <el-form-item prop="accountNumber">
            <div class="row">
              <i class="el-icon-user"></i>
              <el-input v-model="form.accountNumber" placeholder="请输入账号" @enter @input="(value) => (form.accountNumber = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
            </div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="row">
              <i class="el-icon-lock"></i>
              <el-input v-model="form.password" placeholder="请输入登录密码" :show-password="true" @input="(value) => (form.password = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
            </div>
          </el-form-item>
          <el-form-item prop="captcha">
            <div class="row" id="canvas-input">
              <i class="el-icon-lock"></i> <el-input class="canvas-input-class" size="large" type="text" v-model="form.captcha" placeholder="请输入验证码" />
              <img :src="codeUrl" alt="" :key="imgKey" @click="getCode()" />
            </div>
          </el-form-item>
        </el-form>
      </div>
      <el-checkbox class="remember" label="记住密码" v-model="savePsw"></el-checkbox>
      <div class="submit" @click="handleSubmit">登录</div>
    </div>
  </div>
</template>

<script>
import htmlFontSize from '@/utils/htmlFontSize.js'
export default {
  mixins: [htmlFontSize],
  data() {
    const validacount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('账号不能为空'))
      }
      setTimeout(() => {
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
      pswType: 'password',
      form: {
        accountNumber: '',
        password: '',
        captcha: '',
      },
      codeUrl: '',
      imgKey: 0,
      savePsw: false,
      roles: {
        accountNumber: [{ required: true, validator: validacount, trigger: 'blur' }],
        password: [{ required: true, validator: validPsw, trigger: 'blur' }],
        captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
      },
    }
  },
  created() {
    try {
      this.getCode()
      let [acount, psw] = [localStorage.getItem('_acount'), localStorage.getItem('_psw')]
      this.form.accountNumber = acount
      this.form.password = psw
      if (acount && psw) {
        this.savePsw = true
      } else {
        this.savePsw = false
      }
    } catch {}
  },
  mounted() {
    this.getCode()
  },
  methods: {
    handleSubmit() {
      this.$refs['dynamicValidateForm'].validate((valid) => {
        if (valid) {
          this.$store
            .dispatch('user/login', this.form)
            .then((res) => {
              if (this.savePsw) {
                localStorage.setItem('_acount', this.form.accountNumber)
                localStorage.setItem('_psw', this.form.password)
              } else {
                localStorage.removeItem('_acount')
                localStorage.removeItem('_psw')
              }
              this.$router.push('/')
            })
            .catch((err) => {
              this.getCode()
            })
        } else {
          this.getCode()
          return
        }
      })
    },
    // 获取验证码
    getCode() {
      this.imgKey = Math.random()
      this.codeUrl = process.env.VUE_APP_BASE_API + '/login/captcha?' + this.imgKey
    },
  },
}
</script>
<style lang="scss" scope>
.login {
  height: 100%;
  background-image: url('~@/assets/img/login/bg_login.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  justify-content: center;
  .login-form-box {
    width: 568px;
    display: flex;
    flex-direction: column;
    margin-right: 166px;
    .row {
      display: flex;
      align-items: center;
      margin-left: 18px;
      i {
        font-size: 26px;
        color: #999;
      }
      & > i {
        margin-right: 34px;
      }
      .el-input {
        width: 460px;
        input {
          background-color: transparent;
          height: 58px;
          font-size: 18px;
          color: #999;
          border-radius: 10px;
        }
      }
      img {
        width: 110px;
        height: 58px;
        margin-left: -110px;
        z-index: 9;
        border-top-right-radius: 10px;
        border-bottom-right-radius: 10px;
      }
      .canvas-input-class {
        input {
          padding-right: 120px;
        }
      }
    }
    .el-form-item {
      margin-top: 44px;
      &:nth-child(1) {
        margin-top: 0;
      }
    }
    .el-form-item__error {
      margin-left: 80px;
      font-size: 16px;
    }
    .el-input__suffix {
      right: 20px;
      top: 10px;
    }
    .remember {
      margin-left: 93px;
    }
    .title {
      width: 568px;
      height: 35px;
      margin-bottom: 58px;
      background-image: url('~@/assets/img/login/size.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      margin-right: -44px;
    }
    .submit {
      width: 460px;
      height: 58px;
      border-radius: 10px;
      line-height: 58px;
      text-align: center;
      cursor: pointer;
      color: #ffffff;
      font-size: 30px;
      margin-left: 93px;
      margin-top: 50px;
      background: linear-gradient(0deg, #367bf8 0%, #6bb1ff 100%);
      box-shadow: 0px 10px 20px 0px rgba(27, 63, 136, 0.26);
    }
  }
}
</style>
