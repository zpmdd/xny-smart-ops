import axios from "@/utils/request";

/**
 * 风电项目
 */

// 获取全局总览
export const windGlobalView = (data) => {
  return axios({
    url: "/windPower/windGlobalView",
    method: "post",
    data,
  });
};

// 获取风电监测
export const blowerMonitor = (data) => {
  return axios({
    url: "/windPower/blowerMonitor",
    method: "post",
    data,
  });
};

