package com.xny.system.quartz.job;

import com.xny.system.service.YkcService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class YkcSyncJob  implements Job {
    @Resource
    private YkcService ykcService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext){
        log.info("同步云快充站点信息开始==============");
        ykcService.getYkcStationInfo();
        log.info("同步云快充站点信息结束==============");
    }
}
