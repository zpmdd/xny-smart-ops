package com.xny.common.utils.weather.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.HttpClientUtils;
import com.xny.common.utils.weather.enump.WeatherCodeEnum;
import com.xny.common.utils.weather.entity.WeatherData;
import com.xny.common.utils.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 中国气象局天气接口实现
 * @author qianwei
 */
@Service
public class ChinaMeteorologicalAdministrationServiceImpl implements WeatherService {

    @Value("${third-party.weather.cma-url}")
    private String weatherUrl;

    @Value("${third-party.weather.xian}")
    private String cityCodeXian;

    @Override
    public WeatherData getWeatherData(String cityCode) {
        WeatherData weatherData = new WeatherData();
        try{
            if(cityCode == null || "".equals(cityCode)){
                cityCode = cityCodeXian;
            }
            String result = HttpClientUtils.get(weatherUrl + cityCode);
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String code = jsonObject.get("code").toString();
            if("0".equals(code)){
                weatherData.setCode(WeatherCodeEnum.SUCCESS);
                jsonObject = jsonObject.getJSONObject("data").getJSONObject("now");
                weatherData.setLightIntensity("");
                weatherData.setTemperature(jsonObject.getStr("temperature"));
                weatherData.setPressure(jsonObject.getStr("pressure"));
                weatherData.setHumidity(jsonObject.getStr("humidity"));
                weatherData.setWindScale(jsonObject.getStr("windScale"));
                weatherData.setWindDirection(jsonObject.getStr("windDirection"));
            }else{
                weatherData.setCode(WeatherCodeEnum.ERROR);
            }
        }catch (Exception e){
            weatherData.setCode(WeatherCodeEnum.ERROR);
        }
        return weatherData;
    }
}
