package com.xny.system.qd.utils;
import com.google.common.collect.Lists;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.RedisUtils;
import com.xny.system.model.vo.*;
import com.xny.system.qd.config.QdConfig;
import com.xny.system.qd.model.vo.*;
import com.xny.system.qd.model.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class QdUtils {
    private static String stationId = null;
    private static QdConfig qdConfig;
    private static RedisUtils redisUtils;

    @Resource
    public void setQdConfig(QdConfig qdConfig) {
        QdUtils.qdConfig = qdConfig;
    }

    @Resource
    public void setRedisUtils(RedisUtils redisUtils) {
        QdUtils.redisUtils = redisUtils;
    }

    /**
     * 获取token
     * @return
     */
    private static String getToken(){
        Object qdToken = redisUtils.get("qdToken");
        if(null != qdToken){
            return String.valueOf(qdToken);
        }

        Map<String,String> param = new HashMap<>();
        param.put("platform",qdConfig.getPlatform());
        param.put("platformsecret",qdConfig.getPlatformSecret());
        String gettoken = sendGet(param, "gettoken");

        TokenVO tokenVO = JSONUtil.toBean(gettoken, TokenVO.class);
        String token = tokenVO.getAccess_token();
        redisUtils.set("qdToken",token,Long.parseLong(tokenVO.getExpires_in())*60);

        return token;
    }


    /**
     * 获取所有项⽬列表
     * @return StationsVO
     */
    private static String getStations(){
        if(StrUtil.isNotBlank(stationId)){
            return stationId;
        }

        Map<String,String> param = new HashMap<>();
        param.put("province","陕西省");
        param.put("city","西安市");
        String getStations = sendGet(param, "getStations");

        StationsVO stationsVO = JSONUtil.toBean(getStations, StationsVO.class);
        stationId = stationsVO.getRows().get(0).getStr("stationId");
        return stationId;
    }

    /**
     * 获取主监控汇总信息
     * @return MainTotalVO
     */
    public static MainTotalVO getMainTotal(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String getMainTotal = sendGet(param, "getMainTotal");
        return JSONUtil.toBean(getMainTotal, MainTotalVO.class);
    }

    /**
     * 可充放电量查询
     * @return
     */
    public static EnergyCapabilityVO getEnergyCapability(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String getEnergyCapability = sendGet(param, "getEnergyCapability");

        return JSONUtil.toBean(getEnergyCapability, EnergyCapabilityVO.class);
    }


    /**
     * 获取储能系统实时功率信息
     * @return
     */
    public static DevicetransientStatusVO getDevicetransientStatus(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String getDevicetransientStatus = sendGet(param, "getDevicetransientStatus");
        return JSONUtil.toBean(getDevicetransientStatus, DevicetransientStatusVO.class);
    }


    /**
     * 储能系统累计量信息
     */
    public static DeviceStatusVO getDeviceStatus(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String getDeviceStatus = sendGet(param, "getDeviceStatus");
        return JSONUtil.toBean(getDeviceStatus, DeviceStatusVO.class);
    }


    /**
     * 能量块汇总信息
     */
    public static List<GeteBlockTotalVO> geteBlockTotal(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String geteBlockTotal = sendGet(param, "geteBlockTotal");
        return JSONUtil.toList(geteBlockTotal, GeteBlockTotalVO.class);
    }

    /**
     * 能量链汇总信息
     */
    public static List<GeteLinkTotalVO> geteLinkTotal(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String geteLinkTotal = sendGet(param, "geteLinkTotal");
        return JSONUtil.toList(geteLinkTotal, GeteLinkTotalVO.class);
    }


    /**
     * 告警信息
     * @return
     */
    public static GetWarningsVO getWarnings(){
        Map<String,String> param = new HashMap<>();
        param.put("stationId",getStations());
        String getWarnings = sendGet(param, "getWarnings");

        String offlineAlert = JSONUtil.parseObj(getWarnings).getJSONObject("offlineAlert").getStr("rows");
        String unplannedOperation = JSONUtil.parseObj(getWarnings).getJSONObject("unplannedOperation").getStr("rows");

        GetWarningsVO getWarningsVO = new GetWarningsVO();
        getWarningsVO.setOfflineAlert(JSONUtil.toList(offlineAlert,OfflineAlertVO.class));
        getWarningsVO.setUnplannedOperation(JSONUtil.toList(unplannedOperation,UnplannedOperationVO.class));
        return getWarningsVO;
    }



    /**
     * 发送请求
     * @param param
     * @param url
     * @return
     */
    private static String sendGet(Map<String,String> param, String url) {
        String sParam = JSONUtil.toJsonStr(param);
        log.info(url+"======请求参数:"+sParam);

        HttpRequest request = HttpRequest.get(getUrlParams(param, url));
        if(!url.equals("gettoken")){
            request.header("Authorization",getToken());
        }

        String body = request.execute().body();

        log.info(url+"======返回参数:"+body);

        JSONObject oBody = JSONUtil.parseObj(body);
        String code = oBody.getStr("code");
        if(StrUtil.isBlank(code) || !code.equals("200")){
            if(code.equals("401")){
                getToken();
            }
            throw new RuntimeException(url + "======请求失败:"+ oBody.getStr("msg"));
        }
        return oBody.getStr("data");
    }

    /**
     *
     * @param param
     * @return
     */
    private static String getUrlParams(Map<String,String> param,String url){
        url = qdConfig.getQdUrl()+url;

        if(CollectionUtil.isEmpty(param)){
            return url;
        }

        StringBuilder sbd = new StringBuilder();
        Set<Map.Entry<String, String>> entries = param.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            sbd.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String urlParam = sbd.toString();
        return url + "?" +urlParam.substring(0,urlParam.lastIndexOf("&"));
    }

}
