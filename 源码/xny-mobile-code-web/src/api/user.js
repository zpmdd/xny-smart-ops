import axios from "@/utils/request";

// 登录
export const login = (data) => {
  return axios({
    url: "/login/sysUser",
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

// 修改密码
export const editPassword = (data) => {
  return axios({
    url: "/sysUser/editPassword",
    method: "post",
    data,
  });
};
