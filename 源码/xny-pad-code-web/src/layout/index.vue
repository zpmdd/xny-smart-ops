<template>
  <div class="app-wrapper">
    <Main></Main>
  </div>
</template>
<script>
import { Main, Header } from "./components";
export default {
  data() {
    return {};
  },
  computed: {
    sideList: () => {
      return;
    },
  },
  mounted() {
    // console.log(this.$router.options.routes);
  },
  components: { Main, Header },
};
</script>
<style>
.app-wrapper {
  height: 100%;
}
</style>