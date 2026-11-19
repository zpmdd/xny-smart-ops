package com.xny.common.utils.weather;

import com.xny.common.constant.RedisKeyConst;
import com.xny.common.utils.RedisUtils;
import com.xny.common.utils.weather.entity.WeatherData;
import com.xny.common.utils.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * 气象数据获取工具类
 *
 * @author qianwei
 */
@Slf4j
@Service
public class WeatherUtil {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private WeatherService weatherService;

    @Value("${third-party.weather.timeout}")
    private Long timeout;

    /**
     * 获取天气数据
     *
     * @param powerGeneration 发电量 kw
     * @return WeatherData
     * 注意该方法存在缓存，缓存配置 third-party.weather.timeout
     */
    public WeatherData getData(Long powerGeneration,String cityCode) {
        String redisKey = RedisKeyConst.WEATHER_KEY + cityCode;
        WeatherData returnData;
        if (redisUtils.get(redisKey) != null) {
            returnData = (WeatherData) redisUtils.get(redisKey);
            log.info("info:天气调用成功，获取方式：缓存，天气参数：{}", returnData.toString());
        } else {
            returnData = weatherService.getWeatherData(cityCode);
            powerGeneration = powerGeneration == null ? 0 : powerGeneration;
            // 1000w = 17000lm
            // lx = lm / 面积
            returnData.setLightIntensity(getLightIntensity(powerGeneration).toString());
            redisUtils.set(redisKey, returnData, timeout);
            log.info("info:天气调用成功，获取方式：查询第三方，天气参数：{}", returnData.toString());
        }
        return returnData;
    }

    /**
     * 根据发电量获取光照强度
     *
     * @param powerGeneration
     * @return
     */
    public Double getLightIntensity(Long powerGeneration) {
        if (powerGeneration == null) {
            return 1534.00;
        } else {
            return new BigDecimal(powerGeneration * 17000 / 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

    }

}
