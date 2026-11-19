const state = {
  dataStatistics: 1,
}

const mutations = {
  dataStatistics: (state, dataStatistics) => {
    state.dataStatistics = dataStatistics
  },
}

const actions = {}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
