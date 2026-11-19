import axios from "@/utils/request";

// 创建任务
export const taskAdd = (data) => {
  return axios({
    url: "/buTask/add",
    method: "post",
    data,
  });
};

// 获取任务列表
export const taskList = (data) => {
  return axios({
    url: "/buTask/list",
    method: "post",
    data,
  });
};

// 工程数据详情
export const engineeringDetail = (params) => {
  return axios({
    url: "/buTask/engineeringDetail",
    method: "get",
    params,
  });
};

// 已完成统计
export const getFinishTableDataStatisticsByFront = (data) => {
  return axios({
    url: "/buDataStatistics/getFinishTableDataStatisticsByFront",
    method: "post",
    data,
  });
};

// 方案变量值
export const listBuTaskResultVariable = (data) => {
  return axios({
    url: "/result/listBuTaskResultVariable",
    method: "post",
    data,
  });
};
// 方案电负荷满足情况
export const listElectricalLoad = (data) => {
  return axios({
    url: "/result/listElectricalLoad",
    method: "post",
    data,
  });
};
// 方案热负荷满足情况
export const listBuTaskResultThermalLoad = (data) => {
  return axios({
    url: "/result/listBuTaskResultThermalLoad",
    method: "post",
    data,
  });
};
// 方案节能减排指标值
export const listEnergyConservation = (data) => {
  return axios({
    url: "/result/listEnergyConservation",
    method: "post",
    data,
  });
};
// 方案设备容量情况
export const listEquipmentCapacityScheme = (data) => {
  return axios({
    url: "/result/listEquipmentCapacityScheme",
    method: "post",
    data,
  });
};
// 方案各指标值
export const listAllRound = (data) => {
  return axios({
    url: "/result/listAllRound",
    method: "post",
    data,
  });
};
// 方案电负荷情况图
export const listBuTaskResultElectricalLoadCharts = (data) => {
  return axios({
    url: "/result/listBuTaskResultElectricalLoadCharts",
    method: "post",
    data,
  });
};
// 方案热负荷情况图
export const listBuTaskResultThermalLoadCharts = (data) => {
  return axios({
    url: "/result/listBuTaskResultThermalLoadCharts",
    method: "post",
    data,
  });
};

// 方案电负荷满足情况叠加柱状图
export const listBuTaskResultElectricalLoadChartsColumnar = (data) => {
  return axios({
    url: "/result/listBuTaskResultElectricalLoadChartsColumnar",
    method: "post",
    data,
  });
};

// 方案热负荷满足情况叠加柱状图
export const listBuTaskResultThermalLoadChartsColumnar = (data) => {
  return axios({
    url: "/result/listBuTaskResultThermalLoadChartsColumnar",
    method: "post",
    data,
  });
};