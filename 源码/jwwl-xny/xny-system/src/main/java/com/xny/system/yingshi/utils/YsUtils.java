package com.xny.system.yingshi.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.RedisUtils;
import com.xny.system.yingshi.config.YsConfig;
import com.xny.system.yingshi.model.vo.PlayUrlVO;
import com.xny.system.yingshi.model.vo.TokenVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class YsUtils {
    private static YsConfig ysConfig;
    private static RedisUtils redisUtils;

    @Resource
    public void setYsConfig(YsConfig ysConfig) {
        YsUtils.ysConfig = ysConfig;
    }

    @Resource
    public void setRedisUtils(RedisUtils redisUtils) {
        YsUtils.redisUtils = redisUtils;
    }

    /**
     * 获取token
     * @return
     */
    public static String getToken(){
        Object ysToken = redisUtils.get("ysToken");
        if(null != ysToken){
            return String.valueOf(ysToken);
        }

        Map<String,Object> param = new HashMap<>();
        param.put("appKey",ysConfig.getAppKey());
        param.put("appSecret",ysConfig.getSecret());
        String gettoken = sendPost(param, "token/get");

        TokenVO tokenVO = JSONUtil.toBean(gettoken, TokenVO.class);
        String token = tokenVO.getAccessToken();
        redisUtils.set("ysToken",token,(Long.parseLong(tokenVO.getExpireTime()) - new Date().getTime())/1000-3600);

        return token;
    }

    /**
     * 获取播放地址
     * @return
     */
    public static String getPlayUrl(){
        Map<String,Object> param = new HashMap<>();
        param.put("accessToken",getToken());
        param.put("deviceSerial",ysConfig.getDeviceSerial());
        String getPlayUrl = sendPost(param, "v2/live/address/get");
        PlayUrlVO playUrlVO = JSONUtil.toBean(getPlayUrl, PlayUrlVO.class);
        String result = playUrlVO.getUrl().replace("ezopen://","ezopen://NVVSDP@");
        return result;
    }

    /**
     * 发送请求
     * @param param
     * @param url
     * @return
     */
    private static String sendPost(Map<String,Object> param, String url) {
        String sParam = JSONUtil.toJsonStr(param);
        log.info(url+"======请求参数:"+sParam);

        String post = HttpUtil.post(ysConfig.getYsUrl() + url, param);
        log.info(url+"======返回参数:"+post);

        JSONObject oBody = JSONUtil.parseObj(post);
        String code = oBody.getStr("code");
        if(StrUtil.isBlank(code) || !code.equals("200")){
            throw new RuntimeException(url + "======请求失败:"+ oBody.getStr("msg"));
        }
        return oBody.getStr("data");
    }

}
