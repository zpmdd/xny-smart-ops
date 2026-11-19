const state = {
  status: false,
  hammer: false,
}

const mutations = {
  CHANGE_STATUS: (state, log) => {
    state.status = !state.status
  },
  CHANGE_HAMMER: (state, flag) => {
    state.hammer = flag
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
