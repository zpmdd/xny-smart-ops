import axios from "@/utils/request";

export const sysUser = (data) => {
  return axios({
    url: "/login/sysUser",
    method: "post",
    data,
  });
};

export const selectList = (data) => {
  return axios({
    url: "/backManage/selectList",
    method: "post",
    data,
  });
};

export const pageDataChart = (data) => {
  return axios({
    url: "/backManage/pageDataChart",
    method: "post",
    data,
  });
};

export const pageDataCount = (data) => {
  return axios({
    url: "/backManage/pageDataCount",
    method: "post",
    data,
  });
};

export const updateDataCount = (data) => {
  return axios({
    url: "/backManage/updateDataCount",
    method: "post",
    data,
  });
};

export const exportDataChart = (data) => {
  return axios({
    url: "/backManage/exportDataChart",
    method: "post",
    data,
  });
};

export const exportDataCount = (data) => {
  return axios({
    url: "/backManage/exportDataCount",
    method: "post",
    data,
  });
};
