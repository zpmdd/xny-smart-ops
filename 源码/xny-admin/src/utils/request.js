import axios from "axios";
import { useToken } from "@/stores/user.js";
import { storeToRefs } from "pinia";

const userStore = useToken();

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASEURL, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 50000, // request timeout
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
  },
});
const token = storeToRefs(userStore).token;
// request interceptor
service.interceptors.request.use(
  (config) => {
    config.headers["ctype"] = "manage";
    if (token.value) {
      config.headers["xny-token"] = token.value;
    }
    config.data = config.data === undefined ? {} : config.data;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    // if the custom code is not 20000, it is judged as an error.
    if (res.code === 1) {
      ElMessage({
        message: res.message || "Error",
        type: "error",
        duration: 5 * 1000,
      });
      return Promise.reject(new Error(res.message || "Error"));
    } else if (res.code == 2) {
      ElMessage({
        message: res.message || "Error",
        type: "error",
        duration: 5 * 1000,
      });
      userStore.clearToken();
      return Promise.reject(new Error(res.message || "Error"));
    } else {
      return res;
    }
  },
  (error) => {
    ElMessage({
      message: error.message,
      type: "error",
      duration: 5 * 1000,
    });
    return Promise.reject(error);
  }
);

export default service;
