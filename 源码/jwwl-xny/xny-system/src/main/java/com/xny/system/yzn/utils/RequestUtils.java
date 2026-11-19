package com.xny.system.yzn.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.xny.system.yzn.model.dto.BaseDTO;
import com.xny.system.yzn.model.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class RequestUtils {
    private static final String url = System.getenv().getOrDefault("XNY_YZN_URL", "http://127.0.0.1:9");
    private static final String tenantId = System.getenv().getOrDefault("XNY_YZN_TENANT_ID", "");

    /**
     * 发送请求
     * @param dto
     * @param voClazz
     * @return
     */
    public static <T extends BaseDTO<V>,V extends BaseVO> V doSend(T dto, Class<V> voClazz){
        String flag = IdUtil.fastSimpleUUID();

        log.info(flag+"=======请求参数:"+JSONUtil.toJsonStr(dto));

        HttpResponse response = HttpRequest.post(url+dto.getInterface())
                .header("Tenantid",tenantId)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(dto))
                .execute();

        return parseResult(response,voClazz,flag);
    }

    /**
     * 发送请求
     * @param list
     * @param voClazz
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T extends BaseDTO<V>,V extends BaseVO> V doSend(List<T> list, Class<V> voClazz){
        String flag = IdUtil.fastSimpleUUID();

        log.info(flag+"=======请求参数:"+JSONUtil.toJsonStr(list));

        HttpResponse response = HttpRequest.post(url+list.get(0).getInterface())
                .header("tenantId",tenantId)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(list))
                .execute();

        return parseResult(response,voClazz,flag);
    }


    /**
     * 结果解析
     * @param clazz
     * @param response
     * @param <T>
     * @return
     */
    private static <T extends BaseVO> T parseResult(HttpResponse response, Class<T> clazz, String flag) {
        String content = response.body();
        log.info(flag+"=======返回参数:"+content);

        T result;
        try {
            result = JSONObject.parseObject(content, clazz);
            log.info(flag+"=======解析结果:"+JSONUtil.toJsonStr(result, JSONConfig.create().setIgnoreNullValue(false)));
        } catch (Exception e) {
            log.info(flag+"=======解析结果异常{}",e);
            result = JSONObject.parseObject("{\"code\": -1,\"msg\": \"参数解析异常\",\"success\": false}", clazz);
        }

        return result;
    }

}
