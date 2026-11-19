<template>
  <div class="login">
    <video class="video" v-if="videoPlay" :class="{ opactiyVideo: opactiyVideo }" ref="video" muted autoplay poster="@/assets/img/poster.png">
      <source src="@/assets/img/9606dd5c6504eaff497fbfe05da6d2b5.mp4" type="video/mp4" />
    </video>
    <div class="login-form" v-show="formView">
      <div class="title"></div>
      <div class="login-form">
        <el-form :model="form" ref="dynamicValidateForm" :rules="roles" class="demo-dynamic">
          <el-form-item prop="accountNumber">
            <div class="row"><i class="el-icon-user"></i><el-input v-model="form.accountNumber" placeholder="请输入用户名称" @input="(value) => (form.accountNumber = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input></div>
          </el-form-item>
          <el-form-item prop="password">
            <div class="row"><i class="el-icon-lock"></i><el-input v-model="form.password" placeholder="请输入登录密码" :show-password="true" @input="(value) => (form.password = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input></div>
          </el-form-item>
        </el-form>
      </div>
      <div class="submit" @click="handleSubmit">登录</div>
    </div>
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
      show: false,
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
      formView: false,
    }
  },
  computed: {
    ...mapState({
      videoPlay: (state) => state.app.videoPlay,
      userName: (state) => state.user.userName,
      account: (state) => state.user.account,
    }),
  },
  created() {
    if (localStorage.getItem('_account') && localStorage.getItem('_password')) {
      this.form.accountNumber = localStorage.getItem('_account')
      this.form.password = localStorage.getItem('_password')
    }
    localStorage.setItem('viewWidth', document.documentElement.clientWidth || document.body.clientWidth)
  },
  mounted() {
    if (this.videoPlay) {
      this.playVideo()
      setTimeout(() => {
        this.opactiyVideo = true
        this.formView = true
      }, 4300)
      setTimeout(() => {
        this.$store.commit('app/video', false)
      }, 5300)
    }
  },
  methods: {
    playVideo() {
      this.$refs.video.play()
    },
    handleSubmit() {
      this.$refs['dynamicValidateForm'].validate((valid) => {
        if (valid) {
          this.$store
            .dispatch('user/login', this.form)
            .then((res) => {
              try {
                localStorage.setItem('_account', this.form.accountNumber)
                localStorage.setItem('_password', this.form.password)
              } catch (error) {}
              this.$router.push('/')
            })
            .catch((error) => {
              console.log(error)
            })
        }
      })
    },
  },
}
</script>
<style lang="scss" scope>
@keyframes identifier {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
.login {
  height: 1600px;
  background-image: url('~@/assets/img/login/bg_register.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  justify-content: center;
  .title {
    width: 787px;
    height: 48px;
    margin-bottom: 58px;
    background-image: url('~@/assets/img/login/size.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
  }
  .login-form {
    width: 710px;
    display: flex;
    flex-direction: column;
    margin-right: 218px;
    .row {
      display: flex;
      align-items: center;
      i {
        font-size: 36px;
        color: #999;
      }
      & > i {
        margin-right: 34px;
      }
      .el-input {
        width: 620px;
        input {
          background-color: transparent;
          height: 78px;
          font-size: 24px;
          color: #999;
          border-radius: 10px;
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
      top: 20px;
    }
    .submit {
      width: 621px;
      height: 78px;
      border-radius: 10px;
      line-height: 78px;
      text-align: center;
      color: #ffffff;
      font-size: 30px;
      margin-left: 70px;
      margin-top: 100px;
      background: linear-gradient(0deg, #367bf8 0%, #6bb1ff 100%);
      box-shadow: 0px 10px 20px 0px rgba(27, 63, 136, 0.26);
    }
  }
  .video {
    height: 1600px;
    width: 100vw;
    object-fit: fill;
    position: absolute;
    z-index: 99999;
  }
  .opactiyVideo {
    animation: identifier 1s linear;
  }
}
</style>
