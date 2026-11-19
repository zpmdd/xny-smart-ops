package com.xny.framework.des;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSONObject;
import com.xny.common.constant.CommonConstants;
import com.xny.common.utils.CustomSM2Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 请求参数解密
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<?
            extends HttpMessageConverter<?>> converterType) throws IOException {
        //这里的流只能读一次. 后面无法读取了
        String inputStr = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);

        if(CustomSM2Utils.getOpenStatus()){
            JSONObject inputJson = JSONObject.parseObject(inputStr);

            Object obj = inputJson.get(CommonConstants.ENCRYPT_DATA_KEY);
            if(BeanUtil.isNotEmpty(obj)){
                String param = String.valueOf(obj);
                log.info("===入参解密前："+param);
                inputStr = CustomSM2Utils.decrypt2Data(param);
                log.info("===入参解密后："+inputStr);
            }
        }

        //将解密之后的body数据重新封装为HttpInputMessage作为当前方法的返回值
        InputStream inputStream = IoUtil.toStream(inputStr.getBytes(StandardCharsets.UTF_8));

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return inputStream;
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };

    }
}



