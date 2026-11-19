import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const hasToken = localStorage.getItem('user-token-pc')

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      try {
        const roles = await store.dispatch('user/getInfo')
        await store.dispatch('permission/generateRoutes', roles)

        if (to.meta?.roles) {
          if (roles.some((role) => to.meta?.roles.includes(role))) {
            next()
          } else {
            next('/404')
          }
        }else{
          next()
        }
      } catch (error) {
        await store.dispatch('user/resetToken')
        next(`/login`)
        NProgress.done()
      }
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
