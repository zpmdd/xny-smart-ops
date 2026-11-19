

### 技术说明

| 技术        | 说明                  | 官网                           |
| ---------- | --------------------- | ------------------------------|
| Vue        | 前端框架               | https://vuejs.org/              |
| Vue-router | 路由框架               | https://router.vuejs.org/      |
| Vuex       | 全局状态管理框架        | https://vuex.vuejs.org/        |
| Element    | 前端UI框架             | https://element.eleme.io/      |
| Axios      | 前端HTTP框架           | https://github.com/axios/axios |
| vue-ls     | 前端存储               | https://www.npmjs.com/package/vue-ls |
### 项目结构
| 目录        | 说明                   |
| ---------- | --------------------- |
| api        | 存放页面请求的方法与url                |
| assets | 存放静态资源图片与scss                |
| components       | 放组件首字母大写例如:HelloWorld        |
| config    |   index.js 在本地运行时切换环境使用              |
|     |   proxyConfig.js 代理相关是否开启本地跨域              |
|   page  | 页面文件夹           |
| router     | 路由存放               |
|      | routes文件夹下模块化加载 命名以 xxx.router.js方式               |
|      | hooks存放beforeEach钩子函数 功能划分               |
|  store    | vuex文件夹               |
|      | modules文件夹 按功能模块化处理               |
|      | action-types所有操作方法,常量统一管理               |
|   utils   | 工具方法              |
### 安装依赖
  npm i 
### 开发环境运行
npm run dev   
开发环境运行不同环境,在config>index.js中修改local的值

### 生产环境打包
npm run build:prod

### 测试环境打包
npm run build:test

### 接口域名配置
  - 在config>index.js中配置不同的环境   
  ```
  const baseUrlObject = {
    'prod': '', // 生产环境
    'test': ''  // 测试环境 
  }
  ```
    
### 请求 / 响应全局处理
  - 文件位置：
    src/api/request.js
  - 请求：
    全局设置token请求头
    ```
      const token = Vue.ls.get(ACCESS_TOKEN)
      if (token) {
        config.headers['Authorization'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
      }
      return config
    ```
    全局抛出请求错误
    ```javascript
      return Promise.reject(error)
    ```
  - 响应
    全局统一处理接口错误


### 请求取消拦截
    ```
      var CancelToken = axios.CancelToken;
      var source = CancelToken.source();
      axios.get('/user/12345', {
        cancelToken: source.token
      }).catch(function (thrown) {
        if (axios.isCancel(thrown)) {
          console.log('Request canceled', thrown.message);
        } else {
          // handle error
        }
      });
      source.cancel('Operation canceled by the user.');
    ```

### 三页大屏

使用 Node 16 运行 `node tests/overview.mjs` 校验电量、功率和跨日结算。`npm run build:overview` 生成可单独用静态 HTTP 服务打开的三页版本。`npm run build:prod` 使用原系统登录和权限。现场航拍图由维护方放在 `src/assets/private/weijiazhuang.jpg`，开发副本缺少该文件时沿用原光伏背景。

维护已有站点时，先保存线上原始大屏目录。运行 `python3 scripts/compose-screen-release.py 原始大屏目录 dist 新目录`，三个目标页及共用的集中式光伏总览使用同一构建，原场站继续读取原接口，其余路由继续加载原有资源。后续重复发布仍以留存的原始 Vue CLI 入口作为第一个参数。
