package com.xny.framework.des;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class ESQSToStringSerializer implements ObjectSerializer {
    public static final ESQSToStringSerializer instance = new ESQSToStringSerializer();

    public ESQSToStringSerializer() {
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
        }else {
            String strVal = object.toString();
            out.writeString(strVal);
        }
    }
}
