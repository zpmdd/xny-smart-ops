package com.xny.system.quartz.job;

import com.xny.system.model.dto.RealTimeDataDTO;
import com.xny.system.service.DataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DataSyncJob  implements Job {
    @Resource
    private DataSyncService dataSyncService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dataSyncService.realTimeData(new RealTimeDataDTO());
    }
}
