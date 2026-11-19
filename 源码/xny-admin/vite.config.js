import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

import { createHtmlPlugin } from "vite-plugin-html";

import postcssPluginPx2rem from "postcss-plugin-px2rem";
import path from "path";

import { setting } from "./src/setting.js";
const name = setting.title || ""; // page title

const px2remOptions = {
  rootValue: 19.2,
  unitPrecision: 5,
  exclude: false, //默认false，可以（reg）利用正则表达式排除某些文件夹的方法，例如/(node_module)/ 。如果想把前端UI框架内的px也转换成rem，请把此属性设为默认值
  mediaQuery: false, //（布尔值）允许在媒体查询中转换px
  minPixelValue: 0, //设置要替换的最小像素值(3px会被转rem)。 默认 0
};

// https://vitejs.dev/config/
export default defineConfig({
  // base: "./",
  plugins: [
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
    vue(),
    createHtmlPlugin({
      minify: true,
      entry: "src/main.js",
      template: "index.html",
      inject: {
        data: {
          //获取标题变量
          title: name,
        },
      },
    }),
  ],
  server: {
    host: "0.0.0.0",
    port: 8000,
  },
  build: {
    rollupOptions: {
      assetsDir: "static",
      output: {
        // 入口文件中JS输出地址
        entryFileNames: "static/js/[name]-[hash].js",
        // 对代码分割中产生的 chunk 自定义命名
        chunkFileNames: "static/chunk/[name]-[hash].js",
        // 资源出口路径(如：图片、css等)
        assetFileNames: function (assetInfo) {
          const name = assetInfo.name;
          if (/.css$/.test(name)) {
            return "static/css/[name]-[hash].[ext]";
          } else if (/.[jpe?g|png|gif|jpg]$/.test(name)) {
            return "static/images/[name]-[hash].[ext]";
          } else {
            return "static/[ext]/[name]-[hash].[ext]";
          }
        },
      },
    },
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  devServer: {
    open: true,
    overlay: {
      warnings: false,
      errors: true,
    },
  },
  css: {
    postcss: {
      plugins: [postcssPluginPx2rem(px2remOptions)],
    },
  },
});
