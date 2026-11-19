package com.xny.framework.des;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

@Slf4j
public class ESQSTimeSerializer implements ObjectSerializer{
    private final String pattern;

    public ESQSTimeSerializer(String pattern) {
        this.pattern = pattern;
    }

    @SneakyThrows
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName,
                      Type fieldType, int features) throws IOException {

        if (object == null) {
            serializer.out.writeNull();
            return;
        }

        final Date date = (Date) object;

        if(null != serializer.getContext() && null != serializer.getContext().object && null != fieldName){
            try {
                Class<?> clazz = serializer.getContext().object.getClass();
                Field declaredField = clazz.getDeclaredField(fieldName.toString());
                JsonFormat annotation = declaredField.getAnnotation(JsonFormat.class);

                if(null != annotation && StrUtil.isNotBlank(annotation.pattern())){
                    serializer.write(DateUtil.format(date,annotation.pattern()));
                    return;
                }
            } catch (Exception e) {
                log.info(fieldName+"获取失败",e);
                serializer.write(DateUtil.format(date,pattern));
                return;
            }
        }

        serializer.write(DateUtil.format(date,pattern));
    }
}

