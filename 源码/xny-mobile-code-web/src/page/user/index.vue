<template>
  <div class="user-page page">
    <MHeader text="我的" backBtn></MHeader>
    <div class="container">
      <div class="user-msg">
        <div class="l">
          <div class="avatar"></div>
          <div class="msg">
            <div class="userName">{{ userName }}</div>
            <div class="accountNumber">{{ account }}</div>
          </div>
        </div>
        <div class="r">
          <div class="logout" @click="handleLogout">
            <div class="logout-icon"></div>
            <span>退出登录</span>
          </div>
        </div>
      </div>
      <div class="form">
        <div class="form-title">修改密码</div>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="demo-ruleForm">
          <el-form-item label="" prop="oldPassword">
            <el-input v-model="ruleForm.oldPassword" placeholder="请输入旧密码" show-password @input="(value) => (ruleForm.oldPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
          </el-form-item>
          <el-form-item label="" prop="newPassword">
            <el-input v-model="ruleForm.newPassword" placeholder="请输入新密码" show-password @input="(value) => (ruleForm.newPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
          </el-form-item>
          <el-form-item label="" prop="confirmPassword">
            <el-input v-model="ruleForm.confirmPassword" placeholder="请确认新密码" show-password @input="(value) => (ruleForm.confirmPassword = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
          </el-form-item>
          <el-button class="submit-btn" type="primary" size="default" @click="handleSubmit">提交</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'
import { editPassword } from '@/api/user'
export default {
  name: 'user',
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
    }),
  },
  created() {
    this.$nextTick(() => {
      this.$refs.ruleForm.clearValidate()
      this.clearForm()
    })
  },
  mounted() {},
  methods: {
    clearForm() {
      this.ruleForm = {
        newPassword: '',
        oldPassword: '',
        confirmPassword: '',
      }
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
  },
}
</script>
<style lang="scss">
.user-page {
  .form {
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
</style>
<style lang="scss" scoped>
.user-page {
  background-repeat: no-repeat;
  background-image: url('~@/assets/img/user/mine.png');
  background-size: 100% 730px;
  .page-header {
    background-color: transparent;
  }
  .user-msg {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 70px;
    .l {
      display: flex;
      align-items: flex-start;
      .avatar {
        width: 118px;
        height: 118px;
        border-radius: 50%;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/user/avatar.png');
      }
      .msg {
        margin-left: 35px;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        width: 300px;
        word-break: break-all;
        min-height: 118px;
        .userName {
          color: #333333;
          font-size: 34px;
          font-weight: bold;
        }
        .accountNumber {
          font-size: 26px;
          color: #666666;
        }
      }
    }
    .r {
      display: flex;
      .logout {
        display: flex;
        align-items: center;
        justify-content: center;
        background: #ffffff;
        box-shadow: 0px 5px 5px 0px rgba(0, 14, 50, 0.03);
        border-radius: 10px;
        width: 186px;
        height: 60px;
        font-size: 26px;
        color: #333333;
        .logout-icon {
          width: 19px;
          height: 22px;
          background-size: 100% 100%;
          background-image: url('~@/assets/img/user/quit_icon.png');
          background-repeat: no-repeat;
          margin-right: 14px;
        }
      }
    }
  }
  .form {
    padding: 40px 20px 30px;
    background-color: #fff;
    border-radius: 10px;
    margin-top: 52px;
    .form-title {
      color: #292929;
      font-size: 34px;
      font-weight: bold;
    }
    .submit-btn {
      width: 100%;
      height: 80px;
      background: #3775f2;
      border-radius: 10px;
      margin-top: 36px;
      font-size: 26px;
    }
  }
}
</style>
