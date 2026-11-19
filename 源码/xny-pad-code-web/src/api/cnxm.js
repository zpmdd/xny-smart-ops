import axios from "@/utils/request";

// 储能设备监测
export const getDeviceMonitor = (data) => {
  return axios({
    url: "/storage/deviceMonitor",
    method: "post",
    data,
  });
};
// 储能电池监测
export const getBatteryMonitor = (data) => {
  return axios({
    url: "/storage/batteryMonitor",
    method: "post",
    data,
  });
};
// 储能设备监测新
export const newDeviceMonitor = (data) => {
  return axios({
    url: '/storage/newDeviceMonitor',
    method: 'post',
    data,
  })
}