const state = {
  status: false,
  windmillStatus: false,
  fbsgfBG: false,
}

const mutations = {
  CHANGE_STATUS: (state, log) => {
    state.status = !state.status
  },
  windmill: (state, windmillStatus) => {
    state.windmillStatus = windmillStatus
  },
  fbsgfBG: (state, fbsgfBG) => {
    state.fbsgfBG = fbsgfBG
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
