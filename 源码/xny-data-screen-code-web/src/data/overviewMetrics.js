import { readSnapshot } from './overview.mjs'

export default {
  data() { return readSnapshot().overview },
  mounted() { this.$bus.$on('refreshData', this.refreshMetrics) },
  beforeDestroy() { this.$bus.$off('refreshData', this.refreshMetrics) },
  methods: {
    refreshMetrics() { Object.assign(this, readSnapshot().overview) },
  },
}
