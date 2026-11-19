import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import Cookies from 'js-cookie'
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const hasToken = localStorage.getItem('user-token')

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      // await store.dispatch('user/getInfo')
      // await store.dispatch('user/getSysDict')
      next()
      // const roles = await store.dispatch("user/getInfo");
      // if (!roles) {
      //   await store.dispatch("user/resetToken");
      //     next(`/login`);
      // } else {
      //   const whetherBlockUp = roles.whetherBlockUp == 0;
      //   if (whetherBlockUp) {
      //     next();
      //   } else {
      //     Message.error("当前账号已停用");
      //     await store.dispatch("user/resetToken");
      //     next(`/login`);
      //     NProgress.done();
      //   }
      // }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
