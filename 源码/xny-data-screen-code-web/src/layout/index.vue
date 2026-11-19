<template>
  <div class="app-wrapper">
    <!-- <Headers></Headers> -->
    <Main></Main>
  </div>
</template>
<script>
import { Main } from './components'
export default {
  data() {
    return {}
  },
  computed: {
    sideList: () => {
      return
    },
  },
  mounted() {
    // console.log(this.$router.options.routes);
  },
  components: { Main },
}
</script>
<style>
.app-wrapper {
  height: 100%;
}
</style>
