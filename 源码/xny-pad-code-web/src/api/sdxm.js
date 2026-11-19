import axios from "@/utils/request";

/**
 * 水电项目
 */

// 获取全局总览
export const waterGlobalView = (data) => {
  return axios({
    url: "/waterPower/waterGlobalView",
    method: "post",
    data,
  });
};
