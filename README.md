# 新能源智慧运维管理平台

面向新能源项目监测和运维的后端及四个 Web 前端。

| 模块 | 源码目录 | 开发环境 |
| --- | --- | --- |
| 后端 | [jwwl-xny](源码/jwwl-xny/) | JDK 8、Maven |
| 管理后台 | [xny-admin](源码/xny-admin/) | Node 20、Vue 3、Vite |
| 可视化大屏 | [xny-data-screen-code-web](源码/xny-data-screen-code-web/) | Node 16、Vue 2、Vue CLI |
| PAD | [xny-pad-code-web](源码/xny-pad-code-web/) | Node 16、Vue 2、Vue CLI |
| 移动端 Web | [xny-mobile-code-web](源码/xny-mobile-code-web/) | Node 16、Vue 2、Vue CLI |

## 本地开发

前端进入对应目录，安装锁定依赖并启动开发服务。

```sh
npm ci --legacy-peer-deps --ignore-scripts
npm run dev -- --host 127.0.0.1
```

管理后台用 `npm run build:pre` 构建。大屏、PAD 和手机用 `npm run build:prod`。通过 `.env.<mode>.local` 设置接口，管理后台使用 `VITE_APP_BASEURL`，其他端使用 `VUE_APP_BASE_API`。大屏另用 `VUE_APP_CHARGE_MANAGEMENT` 配置充电管理地址。

大屏的萤石视频功能使用维护方提供的 SDK。将已授权的 `ezuikit.js` 放在大屏模块的 `src/static/` 下，再构建该模块和配置后端视频集成。

在 `源码/jwwl-xny` 使用 JDK 8 编译。macOS 上显式指定 `javac` 可让项目使用的 Maven 编译插件正常处理 Lombok。

```sh
export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"
PATH="$JAVA_HOME/bin:$PATH" mvn -B -ntp -Dmaven.test.skip=true \
  -Dmaven.compiler.fork=true "-Dmaven.compiler.executable=$JAVA_HOME/bin/javac" package
```

后端入口为 `xny-system`。运行使用独立的 MySQL 和 Redis，表结构参考见 [数据库设计](数据库设计/数据库设计.sql)。本地数据源通过 `XNY_SYSTEM_JDBC_URL`、`XNY_DATA_JDBC_URL`、`XNY_DB_USER` 和 `XNY_DB_PASSWORD` 注入。按用例准备测试数据和时间分片。集成地址与凭据从各 `application-*.yml` 引用的运行时变量注入。

## 维护与发布

各模块共用本仓库的开发历史，已有生产系统以维护机实测的线上版本为基线。变更涉及共享接口时回归相应前端。发布需固定候选提交、产物 SHA-256、目标模块和可验证的回滚包，并遵循 [协作规则](AGENTS.md)。
