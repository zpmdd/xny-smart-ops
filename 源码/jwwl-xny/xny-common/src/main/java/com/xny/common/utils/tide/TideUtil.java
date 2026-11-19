package com.xny.common.utils.tide;

import com.xny.common.constant.RedisKeyConst;
import com.xny.common.utils.RedisUtils;
import com.xny.common.utils.tide.entity.TideData;
import com.xny.common.utils.tide.service.TideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * 潮汐数据工具类
 *
 * @author zhuqianwei
 */
@Slf4j
@Service
public class TideUtil {

    @Autowired
    TideService tideService;

    @Autowired
    private RedisUtils redisUtils;

    @Value("${third-party.tide.timeout}")
    private Long timeout;
    public TideData getData(){
        String redisKey = RedisKeyConst.TIDE_KEY;
        TideData returnData;
        if (redisUtils.get(RedisKeyConst.TIDE_KEY) != null) {
            returnData = (TideData) redisUtils.get(redisKey);
            log.info("info:海域数据调用成功，获取方式：缓存，海洋参数：{}",returnData.toString());
        } else {
            returnData = tideService.getTideData();
            redisUtils.set(redisKey,returnData,timeout);
            log.info("info:海域数据调用成功，获取方式：查询第三方，海洋参数：{}",returnData.toString());
        }
        return returnData;
    }

    /**
     * 每天1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteRedisKey(){
        redisUtils.del(RedisKeyConst.TIDE_KEY);
    }
}
