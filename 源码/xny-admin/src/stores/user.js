import { ref } from "vue";
import { defineStore } from "pinia";
import router from "@/router";

export const useToken = defineStore("user", () => {
  const token = ref("");
  const userName = ref("");

  token.value = localStorage.getItem("token") || "";
  userName.value = localStorage.getItem("userName") || "";

  const setToken = (newToken) => {
    token.value = newToken;
    localStorage.setItem("token", newToken);
  };
  const clearToken = () => {
    token.value = "";
    localStorage.removeItem("token");
    router.push("/login");
  };

  const setUserName = (newUserName) => {
    userName.value = newUserName;
    localStorage.setItem("userName", newUserName);
  };

  return { token, setToken, clearToken, userName, setUserName };
});

//  default useToken;
