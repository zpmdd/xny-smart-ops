package com.xny.common.constant;


public interface CommonConstants {
    //返回成功标记
    Integer SUCCESS = 0;
    //返回失败标记
    Integer FAIL = 1;

    //客户端类型标识
    String CTYPE = "ctype";
    //token名称
    String TOKEN_NAME = "xny-token";

    //加密内容标识
    String ENCRYPT_DATA_KEY = "XnyData";

    //南瑞发电量偏差系数
    double todayFactor = 0;
    double monthFactor = 0;
    double totalFactor = 0;

}
