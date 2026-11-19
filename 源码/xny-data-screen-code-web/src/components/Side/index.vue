<template>
  <div class="side">
    <div class="side-container" :class="{ sideShow }" ref="container" @click.stop="() => {}">
      <div class="userMsg">
        <div class="userImg"></div>
        <div class="userName">{{ userName }}</div>
      </div>
      <div class="sideList" v-if="sideList.length">
        <div class="projectName" v-if="projectName">
          <div>
            <span>{{ projectName }}</span>
          </div>
        </div>
        <div class="sideList-item" :class="{ sideActive: slideActive === index }" @click="handleSideItem(item, index)" v-for="(item, index) in sideList" :key="index">{{ item.label }}</div>
      </div>
      <div class="userPage">
        <div class="userPage-title">
          <div>
            <span>{{ '系统管理' }}</span>
          </div>
        </div>
        <div class="sideList-item" :class="{ sideActive: $route.path === item.path }" v-show="hideRoute(item)" @click="handleUserSide(index)" v-for="(item, index) in userPageList" :key="index">{{ item.label }}</div>
      </div>
      <div class="back" @click="handleBack" v-if="back"></div>
      <div class="control" :class="{ controlShow: !sideShow, controlHide: sideShow }" @click.stop="handleSide"></div>
    </div>
    <div class="editPwd" v-if="editPwdShow">
      <div class="close"><span @click="handleDialog">&times;</span></div>
      <div class="edit-title">
        <div class="icon"></div>
        <span>个人中心</span>
      </div>
      <div class="user-msg">
        <div class="l">
          <div class="icon"></div>
          <div class="account">
            <div class="userName">{{ userName }}</div>
            <div class="accountNum">{{ account }}</div>
          </div>
        </div>
        <div class="logout" @click="handleLogout"></div>
      </div>
      <div class="form">
        <div class="form-title">修改密码</div>
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
    <div class="mask" v-if="maskShow" @click.stop="handleDialog"></div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { editPassword } from '@/api/user'
export default {
  props: {
    sideList: {
      type: Array,
      default: () => [],
    },
    projectName: {
      type: String,
      default: '',
    },
    slideActive: {
      type: Number,
      default: 0,
    },
    back: {
      type: Boolean,
      default: true,
    },
  },
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
      sideShow: false,
      maskShow: false,
      editPwdShow: false,
      userPageList: [
        {
          label: '个人中心',
        },
        {
          label: '人员配置',
          path: '/sysuser',
          roles: ['user'],
        },
      ],
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
      userName: (state) => state.user.userName,
      account: (state) => state.user.account,
      roles: (state) => state.user.roles,
    }),
    hideRoute() {
      return (obj) => {
        if (obj.roles) {
          return this.roles.some((role) => obj?.roles.includes(role))
        }
        return true
      }
    },
    pageActive: {
      get() {
        return this.slideActive
      },
      set(val) {
        this.$emit('update:slideActive', val)
      },
    },
  },
  mounted() {
    if (this.sideList.length) {
      let _index = this.sideList.findIndex((item) => item.path === this.$route.path)
      this.pageActive = _index == -1 ? 0 : _index
    }
  },
  methods: {
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
    handleLogout() {
      this.$store.dispatch('user/logout').then(() => {
        this.$router.push('/login')
      })
    },
    handleUserSide(index) {
      if (index === 0) {
        this.sideShow = false
        this.maskShow = true
        this.editPwdShow = true
        this.$nextTick(() => {
          this.$refs.ruleForm.clearValidate()
          this.clearForm()
        })
      }
      if (index === 1) {
        this.handleDialog()
        this.$router.push({ path: '/sysuser' })
      }
    },
    handleSideItem(obj, index) {
      this.pageActive = index
      this.handleDialog()
      this.$router.replace({
        path: obj.path,
        query: {
          id: this.$route.query.id,
          projectName: this.$route.query.projectName,
        },
      })
    },
    handleDialog() {
      this.sideShow = false
      this.maskShow = false
      this.editPwdShow = false
    },
    handleSide() {
      this.sideShow = !this.sideShow
      this.maskShow = !this.maskShow
      if (this.editPwdShow) {
        this.editPwdShow = false
      }
    },
    handleBack() {
      this.handleDialog()
      this.$router.back()
    },
    clearForm() {
      this.ruleForm = {
        newPassword: '',
        oldPassword: '',
        confirmPassword: '',
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.side {
  // height: 100vh;
  // width: 100vw;
  .mask {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    z-index: 2021;
    background-color: rgba($color: #000000, $alpha: 0.6);
  }
  .editPwd {
    position: fixed;
    width: 576px;
    height: 675px;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 2023;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/user/icon_my_12.png');
    padding-left: 35px;
    z-index: 999999;
    .close {
      text-align: right;
      color: #fff;
      font-weight: 100;
      padding-right: 26px;
      font-size: 44px;
      height: 30px;
      margin-top: 30px;
      line-height: 26px;
      span {
        cursor: pointer;
      }
    }
    .edit-title {
      height: 53px;
      width: 226px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/user/bg_pu_me_sx03.png');
      display: flex;
      margin-left: 3px;
      align-items: center;
      padding-left: 12px;
      .icon {
        width: 54px;
        height: 52px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/user/icon_my_01.png');
      }
      span {
        font-size: 22px;
        font-weight: bold;
        margin-top: -5px;
        background: linear-gradient(0deg, #9dbae2 0%, #fff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }
    .user-msg {
      height: 98px;
      padding: 0 32px 0 45px;
      margin-top: 24px;
      border: 2px solid;
      border-image: linear-gradient(to right, rgba(#285ba7, 0) 0%, rgba(#285ba7, 1) 50%, rgba(#285ba7, 0));
      border-image-slice: 1;
      border-left: none;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .l {
        display: flex;
        align-items: center;
        .icon {
          height: 71px;
          width: 71px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/common/avatar.png');
          margin-right: 20px;
        }
        .account {
          .userName {
            font-size: 20px;
            color: #fff;
            font-weight: bold;
          }
          .accountNum {
            margin-top: 10px;
            color: #fff;
            font-size: 16px;
          }
        }
      }
      .logout {
        height: 46px;
        width: 105px;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        background-image: url('~@/assets/img/user/icon_my_03.png');
        cursor: pointer;
      }
    }
    ::v-deep .form {
      margin-top: 33px;
      padding: 0 45px 0 40px;
      .form-title {
        font-size: 22px;
        font-weight: bold;
        background: linear-gradient(0deg, #9dbae2 0%, #fff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        margin-bottom: 28px;
      }
      .el-form-item__label {
        color: #fff;
        height: 48px;
        font-size: 16px;
        min-width: 120px !important;
        display: inline-flex !important;
        align-items: center !important;
      }
      .el-form-item__content {
        display: inline-block;
        width: 335px;
      }
      .el-input__inner {
        background-color: transparent;
        height: 48px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        border: none;
        color: #bbe5fd;
        font-size: 16px;
        background-image: url('~@/assets/img/user/icon_my_04.png');
      }
    }
    .submit {
      height: 51px;
      width: 207px;
      line-height: 51px;
      text-align: center;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      margin-left: 164px;
      cursor: pointer;
      margin-top: 30px;
      background-image: url('~@/assets/img/user/icon_my_09.png');
      span {
        font-size: 18px;
        background: linear-gradient(0deg, #8fb9f7 0%, rgba(255, 255, 255, 0.87) 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }
  }
  .side-container {
    position: fixed;
    background-image: url('~@/assets/img/side/bg_sp0125.png');
    background-repeat: no-repeat;
    background-size: 100% 100%;
    top: 50%;
    left: -298px;
    width: 298px;
    height: 800px;
    margin-top: -400px;
    z-index: 2023;
    transition: all 0.3s;
    padding: 5px;
    .control {
      position: absolute;
      cursor: pointer;
      z-index: 2022;
    }
    .controlShow {
      right: -64px;
      height: 130px;
      width: 64px;
      top: 325px;
      background-image: url('~@/assets/img/side/icon_cpi06.png');
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-position: 0 0;
    }
    .controlHide {
      width: 27px;
      height: 55px;
      right: -23px;
      top: 363px;
      background-image: url('~@/assets/img/side/bg_sp0122.png');
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-position: 0 0;
    }
    .userMsg {
      margin-top: 18px;
      height: 76px;
      width: 258px;
      background: linear-gradient(to right, rgba(#3e6aa1, 0.8) 0%, rgba(#3e6aa1, 0));
      display: flex;
      align-items: center;
      justify-content: center;
      .userImg {
        height: 58px;
        width: 58px;
        min-height: 58px;
        min-width: 58px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/common/avatar.png');
        margin-right: 10px;
      }
      .userName {
        font-size: 22px;
        font-weight: bold;
        width: 130px;
        background: linear-gradient(0deg, rgba(130, 201, 255, 0.78) 0%, #fff);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }
    .sideList {
      height: 360px;
      width: 250px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/side/bg_sp0123.png');
      margin: 12px auto 0;
      overflow: hidden;
      .projectName {
        height: 63px;
        width: 210px;
        background: #06306e;
        text-align: center;
        margin: 20px auto 0;
        display: flex;
        align-items: center;
        & > div {
          width: 100%;
        }
        span {
          font-size: 18px;
          font-weight: bold;
          background: linear-gradient(0deg, #609be3 0%, #ffffff 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
      .sideList-item {
        font-style: italic;
        color: #acbfe3;
        font-size: 16px;
        text-align: center;
        margin: 18px auto;
        height: 46px;
        width: 202px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/side/bg_sp0120.png');
        line-height: 40px;
        cursor: pointer;
      }
      .sideActive {
        background-image: url('~@/assets/img/side/bg_sp019.png');
        color: #e5f0ff;
        font-weight: bold;
      }
    }
    .userPage {
      height: 212px;
      width: 250px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/side/bg_sp0124.png');
      margin: 0 auto;
      overflow: hidden;
      .userPage-title {
        height: 44px;
        width: 210px;
        background: #06306e;
        text-align: center;
        margin: 20px auto 0;
        display: flex;
        align-items: center;
        & > div {
          width: 100%;
        }
        span {
          font-size: 18px;
          font-weight: bold;
          background: linear-gradient(0deg, #609be3 0%, #ffffff 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
      .sideList-item {
        font-style: italic;
        color: #acbfe3;
        font-size: 16px;
        text-align: center;
        margin: 18px auto;
        height: 46px;
        width: 202px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/side/bg_sp0120.png');
        line-height: 40px;
        cursor: pointer;
      }
      .sideActive {
        background-image: url('~@/assets/img/side/bg_sp019.png');
        color: #e5f0ff;
        font-weight: bold;
      }
    }
    .back {
      width: 164px;
      height: 46px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/side/bg_sp0121.png');
      cursor: pointer;
      margin: 25px auto 0;
    }
  }
  .sideShow {
    transform: translateX(100%);
  }
}
</style>
