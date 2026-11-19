import axios from "axios";
import { useToken } from "@/stores/user.js";
import { storeToRefs } from "pinia";
import * as FileSaver from "file-saver";
const userStore = useToken();
const token = storeToRefs(userStore).token;
export const exportData = (url, param) => {
  let config = {
    responseType: "blob",
    headers: {
      ctype: "manage",
      "xny-token": token.value,
    },
  };
  axios({
    url: `${import.meta.env.VITE_APP_BASEURL}${url}`,
    method: "POST",
    data: param,
    ...config,
  }).then((response) => {
    const contentDisposition = decodeURI(
      response.headers["content-disposition"]
    );
    const filename = contentDisposition.split("=")[1];
    const blob = new Blob([response.data], {
      type: "application/octet-stream",
    });
    FileSaver.saveAs(blob, filename); // 使用FileSaver.js保存文件
  });
};
