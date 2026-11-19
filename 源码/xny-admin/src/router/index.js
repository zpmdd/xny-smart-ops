import { createRouter, createWebHashHistory } from "vue-router";

import Layout from "@/layout/index.vue";

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: Layout,
      redirect: "/config",
      children: [
        {
          path: "config",
          component: () => import("@/views/config/index.vue"),
        },
      ],
    },
    {
      path: "/login",
      name: "login",
      component: () => import("@/views/login/index.vue"),
    },
    {
      path: "/:catchAll(.*)",
      component: () => import("@/views/404/index.vue"),
    },
  ],
});

export default router;
