package com.xny.common.utils.weather.entity;

import com.xny.common.utils.weather.enump.WeatherCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeatherData {

    private WeatherCodeEnum code;

    private String lightIntensity; // 光照强度

    private String temperature; // 温度 xxx.xx°C

    private String pressure; // 气压 xxxhPa

    private String humidity; // 湿度 xx.xx%

    private String windScale; // 风速 xxxx级

    private String windDirection; // 风向




}
