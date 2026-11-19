import axios from "@/utils/request";

// 消息轮播
export const getDataStatisticsByFront = (data) => {
  return axios({
    url: "/buDataStatistics/getDataStatisticsByFront",
    method: "post",
    data,
  });
};
