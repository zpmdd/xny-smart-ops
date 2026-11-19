const state = {
  status: false,
  hammer: false,
  windmillStatus: false,
  water: false,
  swiperSide: 0,
  jzsgfBG: false,
  fbsgfBG: false,
  leftTip: false,
}

const mutations = {
  CHANGE_STATUS: (state, log) => {
    state.status = !state.status
  },
  CHANGE_HAMMER: (state, flag) => {
    state.hammer = flag
  },
  windmill: (state, windmillStatus) => {
    state.windmillStatus = windmillStatus
  },
  water: (state, water) => {
    state.water = water
  },
  swiperSide: (state, swiperSide) => {
    state.swiperSide = swiperSide
  },
  jzsgfBG: (state, jzsgfBG) => {
    state.jzsgfBG = jzsgfBG
  },
  fbsgfBG: (state, fbsgfBG) => {
    state.fbsgfBG = fbsgfBG
  },
  leftTip: (state, leftTip) => {
    state.leftTip = leftTip
  },
}

const actions = {
  A_QUERY_COMPLETED({ commit }) {
    commit('CHANGE_STATUS')
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
