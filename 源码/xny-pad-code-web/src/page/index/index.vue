<template>
  <div class="cockpit">
    <div class="cockpit-content">
      <div class="logo"></div>
      <div class="cockpit-content-header"></div>
      <div class="cockpit-content-navigation">
        <div class="left"></div>
        <div class="center">
          <div class="swiper-container">
            <div class="swiper-wrapper">
              <div class="swiper-slide" v-for="(item, index) in navigatorList" :path="item.path" :type="item.type" :key="index" :style="{ 'background-image': 'url(' + item.url + ')' }"></div>
            </div>
          </div>
        </div>
        <div class="right"></div>
      </div>
      <div class="cockpit-content-bottom"></div>
    </div>
    <MyDialog :visible.sync="diolog" @close="closeDiolog">
      <div class="dialog-content">
        <div class="close"><i class="el-icon-close" @click="diolog = false"></i></div>
        <div class="top-icon">
          <div class="label-icon"></div>
          <div>我的</div>
        </div>
        <div class="user-msg">
          <div class="l">
            <div class="avatar">
              <div class="img" :style="{ 'background-image': 'url(' + avatar + ')' }"></div>
            </div>
            <div class="msg">
              <div class="username">{{ userName || '' }}</div>
              <div class="tel">{{ account || '' }}</div>
            </div>
          </div>
          <div class="r">
            <div class="logout" @click="handleLogout"></div>
          </div>
        </div>
        <div class="change-psw">
          <div class="title">修改密码</div>
          <div class="form">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="demo-ruleForm">
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="ruleForm.oldPassword" placeholder="请输入旧密码" show-password @input="(value) => (ruleForm.oldPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="ruleForm.newPassword" placeholder="请输入新密码" show-password @input="(value) => (ruleForm.newPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="ruleForm.confirmPassword" placeholder="请再次输入新密码" show-password @input="(value) => (ruleForm.confirmPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="submit" @click="handleSubmit"><span>提交</span></div>
        </div>
      </div>
    </MyDialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Swiper from '@/assets/js/swiper.min.js'
import MyDialog from '@/components/Dialog'
import { editPassword } from '@/api/user.js'
export default {
  data() {
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
      navigatorList: [
        {
          url: require('@/assets/img/home/cockpit_navigation03.png'),
          type: 'cdxm',
          path: '/country',
        },
        {
          url: require('@/assets/img/home/cockpit_navigation02.png'),
          type: 'jzsgf',
          path: '/country',
        },
        {
          url: require('@/assets/img/home/cockpit_navigation01.png'),
          type: 'xmfb',
          path: '/country',
        },
        {
          url: require('@/assets/img/home/cockpit_navigation04.png'),
          type: 'fbsgf',
          path: '/country',
        },
        {
          url: require('@/assets/img/home/cockpit_navigation07.png'),
          type: 'cnxm',
          path: '/country',
        },
        {
          url: require('@/assets/img/home/cockpit_navigation05.png'),
          type: 'fdxm',
          path: '/country',
        },
        // {
        //   url: require('@/assets/img/home/cockpit_navigation06.png'),
        //   type: 'sdxm',
        //   path: '/country',
        // },
        {
          url: require('@/assets/img/home/cockpit_navigation08.png'),
          type: 'wd',
          path: '/user',
        },
      ],
      swiper: null,
      flag: true,
      diolog: false,
      avatar: require('@/assets/img/user/avavtor.png'),
      ruleForm: {
        newPassword: '',
        oldPassword: '',
        confirmPassword: '',
      },
      rules: {
        newPassword: [{ required: true, validator: validPsw, trigger: 'blur' }],
        oldPassword: [{ required: true, validator: validPsw, trigger: 'blur' }],
        confirmPassword: [{ required: true, validator: validPsw, trigger: 'blur' }],
      },
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
    this.flag = true
  },
  mounted() {
    let self = this
    this.$nextTick(() => {
      this.swiper = new Swiper('.swiper-container', {
        loop: true,
        slidesPerView: 5,
        effect: 'coverflow',
        coverflowEffect: {
          rotate: 10,
          stretch: -35,
          depth: 60,
          modifier: 2,
          slideShadows: false,
        },
        // autoplay: {
        //   disableOnInteraction: false,
        //   delay: 2000,
        // },
        navigation: {
          nextEl: '.right',
          prevEl: '.left',
        },
        preventLinksPropagation: true,
        on: {
          tap: function (e) {
            if (self.flag) {
              setTimeout(() => {
                self.flag = true
                self.handleNavigator(self.navigatorList[e.target.dataset.swiperSlideIndex])
              }, 100)
              self.flag = false
            }
          },
        },
      })
    })
  },
  methods: {
    handleNavigator(obj) {
      if (obj.type == 'wd') {
        try {
          this.clearForm()
          this.$refs.ruleForm.resetFields()
        } catch (error) {}
        this.diolog = true
      } else {
        this.$router.push({
          path: obj.path,
          query: {
            type: obj.type,
          },
        })
      }
    },
    closeDiolog() {
      this.flag = true
    },
    handleLogout() {
      this.$store.dispatch('user/logout').then(() => {
        this.$router.push('/login')
      })
    },
    handleSubmit() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          editPassword({ ...this.ruleForm }).then((res) => {
            this.$message.success('修改成功')
            this.handleLogout()
          })
        }
      })
    },
    clearForm() {
      this.ruleForm = {
        newPassword: '',
        oldPassword: '',
        confirmPassword: '',
      }
    },
  },
  components: {
    MyDialog,
  },
}
</script>
<style lang="scss" scoped>
.cockpit {
  height: 1600px;
  overflow-y: hidden;
  .dialog-content {
    width: 812px;
    height: 956px;
    padding: 42px 38px 0 50px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center center;
    background-image: url('~@/assets/img/user/icon_my_12.png');
    .close {
      text-align: right;
      color: #fff;
      font-size: 50px;
    }
    .top-icon {
      width: 320px;
      height: 78px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/user/bg_pu_me_sx03.png');
      display: flex;
      align-items: center;
      font-size: 30px;
      color: #fff;
      font-weight: bold;
      padding-left: 23px;
      & > div {
        &:last-child {
          background: linear-gradient(to bottom, #fff 0%, #9dbae2 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
      .label-icon {
        height: 64px;
        width: 65px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/user/icon_my_01.png');
        margin-right: 5px;
      }
    }
    .user-msg {
      margin-top: 30px;
      width: 760px;
      height: 143px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-position: center center;
      background-image: url('~@/assets/img/user/icon_my_13.png');
      padding: 0 52px 0 59px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .l {
        display: flex;
        align-items: center;
        .avatar {
          height: 110px;
          width: 110px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-position: center center;
          background-image: url('~@/assets/img/user/icon_my_02.png');
          margin-right: 20px;
          display: flex;
          align-items: center;
          justify-content: center;
          .img {
            width: 68px;
            height: 68px;
            border-radius: 50%;
            overflow: hidden;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-position: center center;
          }
        }
        .msg {
          display: flex;
          flex-direction: column;
          justify-content: center;
          .username {
            font-weight: bold;
            color: #ffffff;
            font-size: 28px;
          }
          .tel {
            font-size: 24px;
            color: #ffffff;
            margin-top: 15px;
          }
        }
      }
      .r {
        .logout {
          height: 60px;
          width: 144px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-position: center center;
          background-image: url('~@/assets/img/user/icon_my_03.png');
        }
      }
    }
    .change-psw {
      margin-top: 46px;
      width: 646px;
      margin-left: 58px;
      .title {
        font-size: 30px;
        background: linear-gradient(0deg, #9dbae2 0%, #fff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-weight: bold;
      }
      .form {
        margin-top: 32px;
        ::v-deep .el-input {
          .el-input__inner {
            background-color: transparent;
            height: 80px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/user/icon_my_04.png');
          }
        }
        ::v-deep .el-form-item__label {
          color: #fff;
          height: 80px;
          font-size: 24px;
          min-width: 140px !important;
          display: inline-flex !important;
          align-items: center !important;
        }
        ::v-deep .el-form-item__content {
          display: inline-block;
          width: 480px;
        }
        ::v-deep .el-input__inner {
          border: none;
          color: #bbe5fd;
          font-size: 24px;
        }
      }
      .submit {
        margin: 0 auto;
        margin-top: 50px;
        width: 292px;
        height: 71px;
        text-align: center;
        line-height: 71px;
        font-size: 26px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-position: center center;
        background-image: url('~@/assets/img/user/icon_my_09.png');
        span {
          background: linear-gradient(0deg, #8fb9f7 0%, rgba(#fff, 0.87) 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
  }
  .cockpit-content {
    height: 100%;
    background-image: url('~@/assets/img/home/bg_cockpit.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-position: center center;
    padding-top: 192px;
    .logo {
      position: absolute;
      width: 514px;
      height: 44px;
      background-image: url('~@/assets/img/home/logo.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-position: center center;
      left: 134px;
      top: 140px;
    }
    .cockpit-content-header {
      width: 1445px;
      height: 208px;
      margin: 0px auto 0;
      background-image: url('~@/assets/img/home/head_cockpit.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-position: center center;
    }
    .cockpit-content-bottom {
      width: 1980px;
      height: 452px;
      margin: 0 auto;
      background-image: url('~@/assets/img/home/bottom_cockpit.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-position: center center;
    }
    .cockpit-content-navigation {
      display: flex;
      align-items: center;
      padding: 0 32px;

      .left {
        width: 168px;
        height: 639px;
        background-image: url('~@/assets/img/home/an_cockpit01.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-position: center center;
      }
      .right {
        width: 168px;
        height: 639px;
        background-image: url('~@/assets/img/home/an_cockpit02.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-position: center center;
      }
      .center {
        // flex: 1;
        width: 2160px;
        overflow: hidden;
        .swiper-container {
          height: 520px;
          width: 100%;
          .swiper-slide {
            height: 513px;
            width: 398px;
            background-repeat: no-repeat;
            background-position: center center;
            background-size: 100% 100%;
          }
        }
      }
    }
  }
}
</style>
