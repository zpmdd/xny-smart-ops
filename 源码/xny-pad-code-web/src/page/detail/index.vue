<template>
  <div class="detail" :class="{ windmill: fdxm && windmillStatus, fbsgfBG }">
    <Header></Header>
    <div class="detail-content">
      <router-view></router-view>
    </div>
  </div>
</template>
<script>
import Header from '@/components/Header'
import { mapState } from 'vuex'
export default {
  name: 'detail',
  data() {
    return {
      fdxm: false,
      play: true,
      currentTime: 0,
    }
  },
  computed: {
    ...mapState({
      windmillStatus: (state) => state.status.windmillStatus,
      fbsgfBG: (state) => state.status.fbsgfBG,
    }),
  },
  watch: {
    $route: {
      handler() {
        this.fdxm = this.$route.path.includes('fdxm')
      },
      immediate: true,
    },
  },
  mounted() {},
  methods: {},
  components: {
    Header,
  },
}
</script>
<style lang="scss" scoped>
.detail {
  height: 1600px;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-image: url('~@/assets/img/bg_bg.png');

  .video {
    height: 1600px;
    width: 100vw;
    object-fit: fill;
  }
  .detail-content {
    height: calc(1600px - 198px);
    padding: 0 123px;
  }
}
.windmill {
  background-color: #000;
  background-image: url('~@/assets/img/fdxm/GIF.gif');
}
.fbsgfBG {
  background-color: #000;
  background-image: url('~@/assets/img/fbsgf/ipadicon_power003.png');
}
</style>
