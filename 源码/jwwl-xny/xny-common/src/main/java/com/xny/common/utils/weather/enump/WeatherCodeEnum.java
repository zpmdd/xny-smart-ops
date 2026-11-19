package com.xny.common.utils.weather.enump;

/**
 * 天气返回枚举
 * @author zhuqianwei
 */
public enum WeatherCodeEnum {
    SUCCESS(1,"SUCCESS"),ERROR(0,"ERROR");
    private Integer code;
    private String msg;
    WeatherCodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
