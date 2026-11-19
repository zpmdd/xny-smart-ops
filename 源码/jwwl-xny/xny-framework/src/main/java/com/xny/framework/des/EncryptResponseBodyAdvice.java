package com.xny.framework.des;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xny.common.utils.CustomSM2Utils;
import com.xny.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 返回参数加密
 */
@Slf4j
@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        //获取request, 你可能用得到
        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();

        String requestURL = httpServletRequest.getRequestURI();
        if(CustomSM2Utils.ignore(requestURL)  || !CustomSM2Utils.getOpenStatus()){
            return body;
        }

        if(body instanceof R){
            R r = (R)body;
            Object data = r.getData();
            if(null != data){
                SerializeConfig serializeConfig = new SerializeConfig();
                serializeConfig.put(Long.class, ESQSToStringSerializer.instance);
                serializeConfig.put(Long.TYPE, ESQSToStringSerializer.instance);
                serializeConfig.put(Date.class, new ESQSTimeSerializer("yyyy-MM-dd HH:mm:ss"));
                String dataStr = JSONObject.toJSONString(data, serializeConfig, SerializerFeature.WriteMapNullValue);
                r.setData(CustomSM2Utils.encrypt2Data(dataStr));
            }
            return r;
        }

        return body;
    }

}


