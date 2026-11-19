

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