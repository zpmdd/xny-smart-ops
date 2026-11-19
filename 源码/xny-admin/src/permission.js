import router from "@/router";
import NProgress from "nprogress";
import "nprogress/nprogress.css";

NProgress.configure({ showSpinner: false });

const whiteList = ["/login"];

router.beforeResolve(async (to, from) => {
  NProgress.start();
  const hasToken = localStorage.getItem("token");
  if (hasToken) {
    if (to.path === "/login") {
      return false;
    }
  } else {
    if (whiteList.indexOf(to.path) == -1) {
      if (from.path == "/login") {
        return false;
      }else{
        return "/login";
      }
    }
  }
});

router.afterEach(() => {
  NProgress.done();
});
