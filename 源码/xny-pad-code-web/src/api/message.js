import axios from "@/utils/request";

// 消息轮播
export const carouselAndMessage = (data) => {
  return axios({
    url: "/buDataStatistics/carouselAndMessage",
    method: "post",
    data,
  });
};

// 添加公告消息
export const carouselAndMessageAdd = (params) => {
  return axios({
    url: "/sysMessage/addNotice",
    method: "get",
    params,
  });
};
