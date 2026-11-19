import axios from "@/utils/request";

// 登录
export const login = (data) => {
  return axios({
    url: "/login/sysUser",
    method: "post",
    data,
  });
};

// 修改密码
export const editPassword = (data) => {
  return axios({
    url: "/sysUser/editPassword",
    method: "post",
    data,
  });
};



// 权限树
export const getPermsTree = (data) => {
  return axios({
    url: "/sysUser/getPermsTree",
    method: "post",
    data,
  });
};







// 登出
export const logout = () => {
  return axios({
    url: "/login/logout",
    method: "post",
  });
};

// 获取权限
export const getInfo = (data) => {
  return axios({
    url: "/sysUser/permList",
    method: "post",
    data,
  });
};

// 用户详情
export const getInfoDetail = (params) => {
  return axios({
    url: "/buUser/detail",
    method: "get",
    params,
  });
};