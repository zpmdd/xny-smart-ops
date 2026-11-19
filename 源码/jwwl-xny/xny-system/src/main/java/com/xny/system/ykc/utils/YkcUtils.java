package com.xny.system.ykc.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.RedisUtils;
import com.xny.system.ykc.config.YkcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class YkcUtils {
    private static YkcConfig ykcConfig;
    private static RedisUtils redisUtils;
    @Resource
    public void setYkcConfig(YkcConfig ykcConfig) {
        YkcUtils.ykcConfig = ykcConfig;
    }
    @Resource
    public void setRedisUtils(RedisUtils redisUtils) {
        YkcUtils.redisUtils = redisUtils;
    }

    /**
     * 从缓存获取token
     * @return
     */
    public static String getTokenFromRedis(){
        Object ykcToken = redisUtils.get("ykcToken");
        if(BeanUtil.isEmpty(ykcToken)){
            return getToken();
        }
        return String.valueOf(ykcToken);
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        JSONObject data = new JSONObject();
        data.putOnce("OperatorID",ykcConfig.getZnOperatorID());
        data.putOnce("OperatorSecret",ykcConfig.getYkcOperatorSecret());
        //获取参数
        JSONObject requestParam = getRequestParam(data);
        //发送请求
        JSONObject body = send(requestParam, "query_token");
        //验签
        boolean verify = verify(body);
        if(!verify){
            log.info("================token请求验签失败");
            throw new RuntimeException("token请求验签失败");
        }
        //解析结果
        try {
            String decrypt = AESUtil.Decrypt(JSONUtil.toJsonStr(body.getStr("Data")),ykcConfig.getYkcDataSecret(),ykcConfig.getYkcDataSecretIV());
            log.info("================token信息:"+decrypt);
            JSONObject oDecrypt = JSONUtil.parseObj(decrypt);
            String accessToken = oDecrypt.getStr("AccessToken");
            long tokenAvailableTime = Long.parseLong(oDecrypt.getStr("TokenAvailableTime"));
            redisUtils.set("ykcToken",accessToken,tokenAvailableTime);
            return accessToken;
        } catch (Exception e) {
            log.info("===============token解析异常",e);
            throw new RuntimeException("token解析异常");
        }
    }

    /**
     * 获取充电站信息
     * @return
     */
    public static String getStations(){
        JSONObject data = new JSONObject();
        data.putOnce("PageNo","1");
        data.putOnce("PageSize","10");
        //获取参数
        JSONObject requestParam = getRequestParam(data);
        //发送请求
        JSONObject body = send(requestParam, "query_stations_info");
        //验签
        boolean verify = verify(body);
        if(!verify){
            log.info("================token请求验签失败");
            throw new RuntimeException("token请求验签失败");
        }
        //解析结果
        try {
            String decrypt = AESUtil.Decrypt(JSONUtil.toJsonStr(body.getStr("Data")),ykcConfig.getYkcDataSecret(),ykcConfig.getYkcDataSecretIV());
            log.info("================充电站信息:"+decrypt);
            return decrypt;
        } catch (Exception e) {
            log.info("===============token解析异常",e);
            throw new RuntimeException("token解析异常");
        }
    }

    /**
     * 获取站点状态
     * @param stationIds
     * @return
     */
    public static String getStationsStatus(List<String> stationIds){
        JSONObject data = new JSONObject();
        data.putOnce("StationIDs",stationIds);
        //获取参数
        JSONObject requestParam = getRequestParam(data);
        //发送请求
        JSONObject body = send(requestParam, "query_station_status");
        //验签
        boolean verify = verify(body);
        if(!verify){
            log.info("================token请求验签失败");
            throw new RuntimeException("token请求验签失败");
        }
        //解析结果
        try {
            String decrypt = AESUtil.Decrypt(JSONUtil.toJsonStr(body.getStr("Data")),ykcConfig.getYkcDataSecret(),ykcConfig.getYkcDataSecretIV());
            log.info("================站点状态:"+decrypt);
            return decrypt;
        } catch (Exception e) {
            log.info("===============token解析异常",e);
            throw new RuntimeException("token解析异常");
        }
    }

    /**
     * 验签
     * @param body
     * @return
     */
    public static boolean verify(JSONObject body){
        String ret = body.getStr("Ret");
        if(StrUtil.isBlank(ret) || !ret.equals("0")){
            log.info("===============请求失败:"+ret);
            throw new RuntimeException("请求失败"+ret);
        }

        String result = body.getStr("Ret") +body.getStr("Msg") +body.getStr("Data");
        String sig = body.getStr("Sig");
        String sResult = HMacMD5.getHmacMd5Str(ykcConfig.getYkcSigSecret(),result);
        return sResult.equals(sig);
    }

    /**
     * 获取参数
     * @param data
     * @return
     */
    public static JSONObject getRequestParam(JSONObject data){
        JSONObject param = new JSONObject();
        String timeStamp = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        param.putOnce("OperatorID",ykcConfig.getZnOperatorID());
        param.putOnce("TimeStamp", timeStamp);
        param.putOnce("Seq",timeStamp.substring(timeStamp.length()-4));

        String sData = AESUtil.Encrypt(JSONUtil.toJsonStr(data),ykcConfig.getYkcDataSecret(),ykcConfig.getYkcDataSecretIV());
        param.putOnce("Data",sData);

        String sign = param.getStr("OperatorID") + param.getStr("Data") + param.getStr("TimeStamp") + param.getStr("Seq");
        String sSign = HMacMD5.getHmacMd5Str(ykcConfig.getYkcSigSecret(),sign);
        param.putOnce("Sig",sSign);
        return param;
    }

    /**
     * 发送请求
     * @param param
     * @param url
     * @return
     */
    public static JSONObject send(JSONObject param,String url){
        String sParam = JSONUtil.toJsonStr(param);

        log.info(url+"======请求参数:"+sParam);
        HttpRequest request = HttpRequest.post(ykcConfig.getYkcUrl() + url)
                .header("Content-Type", "application/json");

        if(!url.equals("query_token")){
            request.header("Authorization", "Bearer "+getTokenFromRedis());
        }

        String body = request
                .body(sParam)
                .execute()
                .body();
        log.info(url+"======返回参数:"+body);

        JSONObject oBody = JSONUtil.parseObj(body);
        String ret = oBody.getStr("Ret");
        if(StrUtil.isBlank(ret) || !ret.equals("0")){
           throw new RuntimeException(url + "======请求失败:"+ret);
        }
        return oBody;
    }

    /**
     * 获取推送接口响应参数
     * @param data
     * @return
     */
    public static JSONObject getPushResponse(JSONObject data){
        JSONObject result = new JSONObject();
        result.putOnce("Ret",0);
        result.putOnce("Msg","请求成功");

        String sData = AESUtil.Encrypt(JSONUtil.toJsonStr(data),ykcConfig.getZnDataSecret(),ykcConfig.getZnDataSecretIV());
        result.putOnce("Data",sData);

        String sign = result.getStr("Ret") +result.getStr("Msg") +result.getStr("Data");
        String sSign = HMacMD5.getHmacMd5Str(ykcConfig.getZnSigSecret(),sign);
        result.putOnce("Sig",sSign);
        return result;
    }

    /**
     * 推送接口参数验签并解密
     * @param body
     * @return
     */
    public static String pushVerifyAndDecrypt(JSONObject body,String url){
        log.info(url+"===========请求参数:"+JSONUtil.toJsonStr(body));

        String sign = body.getStr("OperatorID") + body.getStr("Data") + body.getStr("TimeStamp") + body.getStr("Seq");
        String sSign = HMacMD5.getHmacMd5Str(ykcConfig.getZnSigSecret(),sign);
        if(!sSign.equals(body.getStr("Sig"))){
            log.info(url+"===============验签失败");
            throw new RuntimeException("验签失败");
        }
        try {
            String sData = AESUtil.Decrypt(body.getStr("Data"),ykcConfig.getZnDataSecret(),ykcConfig.getZnDataSecretIV());
            log.info(url+"===============Data解密后:"+sData);
            return sData;
        } catch (Exception e) {
            log.info(url+"===============Data解析异常:",e);
            throw new RuntimeException("Data解析异常");
        }
    }

    /**
     * 推送接口token校验
     * @param request
     * @return
     */
    public static void pushTokenVerify(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(StrUtil.isBlank(token)){
            log.info("===============云快充推送接口token校验异常");
            throw new RuntimeException("云快充推送接口token校验异常");
        }

        if(!redisUtils.hasKey(token)){
            log.info("===============云快充推送接口token失效");
            throw new RuntimeException("云快充推送接口token失效");
        }
    }


}
