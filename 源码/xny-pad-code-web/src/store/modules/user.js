import { login, logout, getInfo, getInfoDetail } from '@/api/user'
import { getToken, setToken, removeToken, getLoginId, setLoginId, removeLoginId, getName, setName, removeName, getAccount, setAccount, removeAccount } from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  userName: getName(),
  loginId: getLoginId(),
  account: getAccount(),
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_NAME: (state, userName) => {
    state.token = token
  },
  SET_LOGINID: (state, id) => {
    state.loginId = id
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_ACCOUNT: (state, account) => {
    state.account = account
  },
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { accountNumber, password } = userInfo
    return new Promise((resolve, reject) => {
      login({
        accountNumber: accountNumber.trim(),
        password: password,
      })
        .then((response) => {
          const { data } = response
          commit('SET_TOKEN', data.tokenValue)
          commit('SET_LOGINID', data.loginId)
          commit('SET_NAME', data.userName)
          commit('SET_ACCOUNT', accountNumber)
          setToken(data.tokenValue)
          setLoginId(data.loginId)
          setName(data.userName)
          setAccount(accountNumber)
          resolve()
        })
        .catch((error) => {
          reject(error)
        })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      let info = { loginId: state.loginId, userName: state.name }
      commit('SET_INFO', info)
      resolve(info)
      // getInfoDetail(params)
      //   .then((response) => {
      //     const { data } = response
      //     commit('SET_INFO', data)
      //     resolve(data)
      //   })
      //   .catch((error) => {
      //     reject(error)
      //   })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_LOGINID', '')
      commit('SET_NAME', '')
      commit('SET_ACCOUNT', '')
      removeToken()
      removeAccount()
      removeLoginId()
      removeName()
      resolve()
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise((resolve) => {
      commit('SET_TOKEN', '')
      commit('SET_LOGINID', '')
      commit('SET_NAME', '')
      commit('SET_ACCOUNT', '')
      removeToken()
      removeAccount()
      removeLoginId()
      removeName()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, {
      root: true,
    })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
