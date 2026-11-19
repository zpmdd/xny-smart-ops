package com.xny.system.ykc.config;

import com.xny.system.service.YkcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class StartupRunner implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private YkcService ykcService;
 
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("初始化云快充站点信息开始==============");
        try {
            ykcService.getYkcStationInfo();
        } catch (Exception e) {
            log.info("初始化云快充站点信息异常==============",e);
        }
        log.info("初始化快充站点信息结束==============");
    }
}