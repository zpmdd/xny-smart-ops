package com.xny.system.quartz.job;

import com.xny.system.service.HfqxService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class HfqxSyncJob implements Job {
    @Resource
    private HfqxService hfqxService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("同步红枫设备信息开始==============");
        hfqxService.syncHfEquipmentInfo();
        log.info("同步红枫设备信息结束==============");

        log.info("同步红枫设备状态信息开始==============");
        hfqxService.syncHfEquipmentStatusInfo();
        log.info("同步红枫设备状态信息结束==============");
    }
}
