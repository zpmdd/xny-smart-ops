import "./assets/main.scss";

import { createApp } from "vue";
import { createPinia } from "pinia";

import "@/assets/font/font.scss";

import App from "./App.vue";
import router from "./router";

import "./permission.js";

const app = createApp(App);

// 挂在全局变量解构获取
import { initChart } from "@/utils/echarts";
import { bus } from "@/utils/vueBus";
import { vailtAccount, vailtPassword } from "@/utils/vaild.js";
import * as echarts from "echarts";
app.config.globalProperties.$initChart = initChart;
app.config.globalProperties.$bus = bus;
app.config.globalProperties.$echarts = echarts;
app.config.globalProperties.$vailtAccount = vailtAccount;
app.config.globalProperties.$vailtPassword = vailtPassword;
app.provide("globalProperties", app.config.globalProperties);
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
import ElementPlus from "element-plus";
import zhCn from "element-plus/es/locale/lang/zh-cn";

app.use(ElementPlus, {
  locale: zhCn,
});
app.use(createPinia());
app.use(router);

app.mount("#app");
