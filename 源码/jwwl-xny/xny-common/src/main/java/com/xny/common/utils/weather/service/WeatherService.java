package com.xny.common.utils.weather.service;

import com.xny.common.utils.weather.entity.WeatherData;

/**
 * 获取天气接口
 * @author qianwei
 */
public interface WeatherService {

    /**
     * 获取基础天气数据
     * @return WeatherData
     */
    WeatherData getWeatherData(String cityCode);

}
