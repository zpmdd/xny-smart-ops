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

// 获取用户列表
export const sysUserList = (data) => {
  return axios({
    url: "/sysUser/list",
    method: "post",
    data,
  });
};

// 停用启用删除用户
export const operate = (data) => {
  return axios({
    url: "/sysUser/operate",
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

// 添加用户
export const sysUserAdd = (data) => {
  return axios({
    url: "/sysUser/add",
    method: "post",
    data,
  });
};

// 修改用户
export const sysUserUpdate = (data) => {
  return axios({
    url: "/sysUser/update",
    method: "post",
    data,
  });
};
