import axios from "@/utils/request";

// 获取场景
export const sceneList = (data) => {
  return axios({
    url: "/buScener/list",
    method: "POST",
    data,
  });
};

// 获取设备
export const equipment = (data) => {
  return axios({
    url: "/buScener/equipmentDetail",
    method: "POST",
    data,
  });
};

// 场景状态

export const sceneStatus = (data) => {
  return axios({
    url: "/buScener/enable",
    method: "POST",
    data,
  });
};

// 场景详情

export const sceneDetail = (params) => {
  return axios({
    url: "/buScener/detail",
    method: "get",
    params,
  });
};
// 删除场景
export const sceneDel = (data) => {
  return axios({
    url: "/buScener/delete",
    method: "POST",
    data,
  });
};

// 添加场景
export const sceneAdd = (data) => {
  return axios({
    url: "/buScener/add",
    method: "POST",
    data,
  });
};

// 设备编辑
export const sceneEquipmentEdit = (data) => {
  return axios({
    url: "/buScener/equipmentEdit",
    method: "POST",
    data,
  });
};

// 工程数据详情
export const engineeringDetail = (data) => {
  return axios({
    url: "/buScener/engineeringDetail",
    method: "POST",
    data,
  });
};

// 工程数据编辑
export const engineeringEdit = (data) => {
  return axios({
    url: "/buScener/engineeringEdit",
    method: "POST",
    data,
  });
};

//   编辑场景
export const scencedit = (data) => {
  return axios({
    url: "/buScener/edit",
    method: "POST",
    data,
  });
};
