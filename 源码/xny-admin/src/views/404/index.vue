<template>
  <div class="not-found">
    <h1>404</h1>
    <p>抱歉，您访问的页面不存在。</p>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<style scoped>
.not-found {
  text-align: center;
  padding-top: 100px;
}

.not-found h1 {
  font-size: 100px;
  margin-bottom: 20px;
}

.not-found p {
  font-size: 24px;
}
</style>
