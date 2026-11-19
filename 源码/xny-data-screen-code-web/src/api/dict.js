import axios from "@/utils/request";

// 字典数据
export const sysDict = (data) => {
  return axios({
    url: "/sysDict/listByType",
    method: "post",
    data,
  });
};

