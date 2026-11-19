<template>
  <div class="jzsgf-pvGlobalOverview view">
    <Side></Side>
    <MyHeader></MyHeader>
    <div class="view-container">
      <div class="query-view">
        <div class="l">
          <div class="create-account" @click="handleAddUser()">添加账号</div>
        </div>
        <div class="r">
          <input type="text" v-model="querySelect.nameOrAccount" @keydown.enter="handleSearch" class="search-input" placeholder="请输入账号/员工姓名" />
        </div>
      </div>
      <div class="table-view">
        <div class="table-head">
          <div class="cell">员工姓名</div>
          <div class="cell">账号</div>
          <div class="cell">性别</div>
          <div class="cell">当前职务</div>
          <div class="cell">账号状态</div>
          <div class="cell">操作人</div>
          <div class="cell">操作时间</div>
          <div class="cell">操作</div>
        </div>
        <div class="table-body">
          <div class="table-row" v-for="item in tableData" :key="item.id">
            <div class="cell">{{ item.userName }}</div>
            <div class="cell">{{ item.accountNumber }}</div>
            <div class="cell">{{ item.sex == 1 ? '男' : '女' }}</div>
            <div class="cell">{{ item.position }}</div>
            <div class="cell"><el-switch v-model="item.whetherBlockUp" active-color="#3A7D2D" inactive-color="#898383" :active-value="0" :inactive-value="1" @change="handleSwitch(item)"> </el-switch></div>
            <div class="cell">{{ item.updateName }}</div>
            <div class="cell">{{ item.updateTime }}</div>
            <div class="cell">
              <el-button type="text" style="color: #36eaff" @click="handleEdit(item)">编辑</el-button>
              <el-button type="text" style="color: #db5d5d" @click="handleDelete(item)">删除</el-button>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination-view">
        <el-pagination layout="prev, pager, next" :total="total" :current-page.sync="querySelect.pageNum" :page-size="querySelect.pageSize" @current-change="handleCurrentChange"> </el-pagination>
      </div>
    </div>
    <div class="user-form" v-if="formVisible">
      <div class="close"><span @click="handleDialog">&times;</span></div>
      <div class="edit-title">
        <div class="icon"></div>
        <span>{{ formType == 'edit' ? '编辑账号' : '添加账号' }}</span>
      </div>
      <div class="user-form-content">
        <div class="form">
          <el-form :inline="true" :model="userForm" class="demo-form-inline" label-position="left" :rules="rules" ref="userFormRef">
            <el-form-item label="员工姓名" prop="userName">
              <el-input v-model="userForm.userName" placeholder="请输入员工姓名" maxlength="10"></el-input>
            </el-form-item>
            <el-form-item label="账号" prop="accountNumber">
              <el-input v-model="userForm.accountNumber" placeholder="请输入账号" @input="(value) => (userForm.accountNumber = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="userForm.password" placeholder="请输入密码" show-password @input="(value) => (userForm.password = value.replace(/[^0-9A-Za-z]/g, ''))" maxlength="18"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <el-select v-model="userForm.sex" placeholder="请选择性别" popper-class="permission-select">
                <el-option label="女" :value="0"></el-option>
                <el-option label="男" :value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="当前职务">
              <el-input v-model="userForm.position" placeholder="请输入当前职务" maxlength="10"></el-input>
            </el-form-item>
          </el-form>
        </div>
        <TextTittle :label="'权限配置'"></TextTittle>
        <div class="permission">
          <div class="permission-list" v-for="(item, index) in permission" :key="index">
            <div class="permission-p">
              <el-checkbox v-model="item.havePerm" :class="{ checkAll: checkNum(item) === item.children.length }" :indeterminate="indeterminate(item)" @change="(params) => changeP(params, item)">{{ item.menuName }}</el-checkbox>
            </div>
            <div class="permission-c">
              <el-checkbox v-for="(item1, index1) in item.children" :key="index1" v-model="item1.havePerm" @change="(params) => changeC(params, item)">{{ item1.menuName }}</el-checkbox>
            </div>
          </div>
        </div>
      </div>
      <div class="footer-btn">
        <div class="cancel" @click="formVisible = false">取消</div>
        <div class="confirm" @click="handleSubmit">确定</div>
      </div>
    </div>
    <div class="mask" v-if="formVisible" @click.stop="formVisible = false"></div>
  </div>
</template>
<script>
const Side = () => import('@/components/Side')
import { sysUserList, operate, getPermsTree, sysUserAdd, sysUserUpdate } from '@/api/user'
export default {
  name: 'sysuser',
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
      querySelect: {
        nameOrAccount: '',
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      tableData: [],
      cellList: ['userName', 'accountNumber', 'sex', 'position', 'whetherBlockUp', 'updateName', 'updateTime'],
      formType: '',
      formVisible: false,
      currentUser: {},

      userForm: {
        accountNumber: '',
        password: '',
        position: '',
        sex: 0,
        userName: '',
      },
      permission: [],

      rules: {
        accountNumber: [{ required: true, validator: validacount, trigger: 'blur' }],
        password: [{ required: true, validator: validPsw, trigger: 'blur' }],
        userName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
        sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
        position: [{ required: true, message: '请输入当前职务', trigger: 'blur' }],
      },
    }
  },
  computed: {
    indeterminate() {
      return (obj) => {
        if (obj.children.length === 0) {
          return false
        }
        let checkNum = obj.children.filter((item) => item.havePerm).length
        return checkNum > 0 && checkNum < obj.children.length
      }
    },
    checkNum() {
      return (obj) => {
        if (obj.children.length === 0) {
          return false
        }
        return obj.children.filter((item) => item.havePerm).length
      }
    },
  },
  created() {
    this.handleSearch()
  },
  mounted() {},
  methods: {
    handleSubmit() {
      let arr = []
      checkPer(this.permission)
      function checkPer(list) {
        if (!list.length) return
        list.forEach((item) => {
          if (item.havePerm) {
            arr.push(item.id)
            checkPer(item.children)
          }
        })
      }
      if (this.formType === 'add') {
        let params = {
          ...this.userForm,
          ...{
            permsList: arr,
          },
        }
        sysUserAdd(params).then((res) => {
          this.query()
          this.handleDialog()
          if (res.message) {
            this.$message.success(res.message)
          }
        })
      }
      if (this.formType === 'edit') {
        let params = {
          ...this.userForm,
          ...{
            permsList: arr,
            id: this.currentUser.id,
          },
        }
        sysUserUpdate(params).then((res) => {
          this.query()
          this.handleDialog()
          if (res.message) {
            this.$message.success(res.message)
          }
        })
      }
    },
    changeP(status, obj) {
      if (obj.children.length === 0) return
      let checkNum = obj.children.filter((item) => item.havePerm).length
      this.handleChangeCheckStatus(obj.children, checkNum !== obj.children.length)
    },
    changeC(status, obj) {
      let checkNum = obj.children.filter((item) => item.havePerm).length
      obj.havePerm = checkNum > 0
    },
    handleChangeCheckStatus(list, status) {
      list.forEach((item) => {
        item.havePerm = status
      })
    },
    handleDialog() {
      this.formVisible = false
    },
    getPermsTree(params) {
      return new Promise((resolve, reject) => {
        getPermsTree(params).then((res) => {
          this.permission = res.data || []
          resolve()
        })
      })
    },
    handleAddUser() {
      this.formType = 'add'
      this.getPermsTree().then((res) => {
        this.formVisible = true
        this.$nextTick(() => {
          this.userForm = {
            accountNumber: '',
            password: '',
            position: '',
            sex: 0,
            userName: '',
          }
        })
      })
    },
    handleSwitch(obj) {
      let data = {
        operateType: obj.whetherBlockUp,
        userId: obj.id,
      }
      operate(data).then((res) => {
        if (res.message) {
          this.$message.success(res.message)
        }
        this.query()
      })
    },
    handleDelete(obj) {
      if (obj.whetherBlockUp === 0) {
        this.$message.error('请先禁用账号后进行删除操作！')
        return
      }
      this.$confirm('此操作将永久删除该用户, 是否继续?', ' ', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        let data = {
          operateType: 2,
          userId: obj.id,
        }
        operate(data).then((res) => {
          if (res.message) {
            this.$message.success(res.message)
          }
          this.query()
        })
      })
    },
    handleEdit(obj) {
      this.formType = 'edit'
      this.currentUser = obj
      let params = {
        userId: obj.id,
        ifExist: false,
      }
      this.getPermsTree(params).then((res) => {
        this.formVisible = true
        this.$nextTick(() => {
          this.userForm = {
            accountNumber: obj.accountNumber,
            password: obj.password,
            position: obj.position,
            sex: obj.sex,
            userName: obj.userName,
          }
        })
      })
    },
    handleCurrentChange(val) {
      this.querySelect.pageNum = val
      this.query()
    },
    handleSearch() {
      this.querySelect.pageNum = 1
      this.query()
    },
    query() {
      sysUserList(this.querySelect).then((res) => {
        this.tableData = res.data.list
        this.total = parseInt(res.data.total)
      })
    },
  },
  components: {
    Side,
  },
}
</script>
<style lang="scss">
.permission-select {
  z-index: 999999 !important;
  background-color: transparent;
  li:hover {
    background-color: transparent;
  }
  .el-select-dropdown__item.selected {
    background-color: transparent;
    color: #bbe5fd;
  }
  .el-select-dropdown__item.hover,
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
.el-message-box {
  background-color: transparent !important;
  background-image: url('~@/assets/img/user/bg_allocatiaaon00.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  border: none;
  .el-message-box__content {
    padding-left: 30px;
    padding-right: 30px;
    padding-top: 30px;
  }
  .el-button {
    background-color: transparent !important;
    border: none !important;
    // color: #db5d5d;
  }
  .el-button--primary {
    color: #36eaff;
  }
}
.jzsgf-pvGlobalOverview {
  height: 100%;
  .view-container {
    .query-view {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      .l {
        display: flex;
        align-items: center;
        .create-account {
          cursor: pointer;
          width: 102px;
          height: 44px;
          line-height: 44px;
          text-align: center;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/user/bg_allocation01.png');
          color: #fff;
          font-size: 13px;
        }
      }
      .r {
        display: flex;
        align-items: center;
        .search-input {
          height: 55px;
          width: 251px;
          background-color: transparent;
          color: #bbe5fd;
          font-size: 13px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/user/bg_allocation02.png');
          padding: 0 24px;
        }
      }
    }
    .table-view {
      height: 806px;
      margin-top: 24px;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-image: url('~@/assets/img/user/bg_allocation03.png');
      .table-head {
        display: flex;
        padding: 0 76px;
        height: 66px;
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-image: url('~@/assets/img/user/bg_allocation04.png');
        .cell {
          flex: 1;
          font-size: 20px;
          color: #afbed8;
          font-weight: bold;
          font-style: italic;
          line-height: 66px;
          padding: 0 20px;
        }
      }
      .table-body {
        margin-top: 16px;
        .table-row {
          display: flex;
          padding: 0 76px;
          height: 64px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
          background-image: url('~@/assets/img/user/bg_allocation05.png');
          margin-top: 8px;
          &:first-child {
            margin-top: 0;
          }
          .cell {
            flex: 1;
            font-size: 16px;
            color: #a1b8cd;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            line-height: 64px;
            padding: 0 20px;
          }
        }
      }
    }
    .pagination-view {
      text-align: right;
      ul {
        li {
          background: transparent !important;
          color: #a1b8cd;
        }
      }
      .el-pagination button:disabled {
        background: transparent !important;
        color: #a1b8cd;
      }
      .el-pagination .btn-next,
      .el-pagination .btn-prev {
        background: transparent !important;
        color: #a1b8cd;
      }
      .active {
        color: #36eaff;
      }
    }
  }
  .user-form {
    position: absolute;
    z-index: 2101;
    height: 746px;
    width: 817px;
    background-size: 100% 100%;
    background-repeat: no-repeat;
    background-image: url('~@/assets/img/user/bg_allocation06.png');
    top: 132px;
    left: 551px;
    padding: 0 20px 0 54px;
    .close {
      text-align: right;
      color: #fff;
      font-weight: 100;
      padding-right: 16px;
      font-size: 44px;
      height: 30px;
      margin-top: 30px;
      line-height: 26px;
      span {
        cursor: pointer;
      }
    }
    .edit-title {
      color: #ecf2fa;
      font-size: 22px;
      font-weight: bold;
      padding-left: 31px;
      height: 73px;
      line-height: 73px;
      margin-top: -19px;
    }
    .user-form-content {
      height: 500px;
      overflow: auto;
      padding-left: 30px;
      margin-top: 20px;
      &::-webkit-scrollbar {
        width: 6px;
        display: block;
        border-radius: 3px;
        background: rgba(255, 255, 255, 0.1);
      }
      &::-webkit-scrollbar-thumb {
        width: 6px;
        border-radius: 3px;
        background: rgba(255, 255, 255, 0.22);
      }
      .form {
        .el-form-item {
          .el-form-item__label {
            color: #bbe5fd;
            font-size: 16px;
            width: 100px;
            padding: 0 10px;
          }
          input {
            background-color: transparent;
            border: none;
            color: #999999;
            font-size: 13px;
            width: 230px;
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-image: url('~@/assets/img/user/bg_allocation07.png');
          }
        }
      }
      .permission {
        .permission-list {
          margin-top: 30px;
          .el-checkbox__input {
            vertical-align: baseline;
          }
          .permission-p {
            .el-checkbox__label {
              color: #cde3f6;
              font-size: 20px;
            }
          }
          .permission-c {
            margin-top: 24px;
            .el-checkbox__label {
              color: #cde3f6;
              font-size: 16px;
            }
          }
          .el-checkbox__inner {
            border-radius: 50%;
            background-color: transparent;
            border-color: #20ddff;
          }
          .el-checkbox__input.is-checked .el-checkbox__inner,
          .el-checkbox__input.is-indeterminate .el-checkbox__inner {
            border-color: #20ddff;
          }
          .el-checkbox__input.is-checked .el-checkbox__inner::after {
            content: '';
            border: none;
            height: 6px;
            width: 6px;
            border-radius: 50%;
            background-color: #20ddff;
            top: 3px;
            left: 3px;
          }
          .is-indeterminate {
            background-color: #20ddff;
            border-radius: 50%;
            height: 14px;
            width: 14px;
          }
          .checkAll {
            .el-checkbox__inner {
              border-radius: 50%;
              background-color: transparent;
              border-color: #20ddff;
              position: relative;
              &::after {
                content: '';
                border: none;
                height: 6px;
                width: 6px;
                border-radius: 50%;
                background-color: #20ddff;
                top: 3px;
                left: 3px;
                position: absolute;
                transform: rotate(45deg) scaleY(1);
              }
            }
          }
        }
      }
    }
    .footer-btn {
      display: flex;
      padding-right: 30px;
      justify-content: flex-end;
      margin-top: 24px;
      div {
        width: 100px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-size: 13px;
        cursor: pointer;
        border-radius: 4px;
        margin-left: 20px;
      }
      .cancel {
        color: #a2bdff;
        border: 1px solid #518eff;
      }
      .confirm {
        color: #ffffff;
        border: 1px solid #6a9fff;
        background: linear-gradient(72deg, #1e35e3 0%, #4a82f5 100%);
      }
    }
  }
  .mask {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    z-index: 2100;
    background-color: rgba($color: #000000, $alpha: 0.6);
  }
}
</style>
