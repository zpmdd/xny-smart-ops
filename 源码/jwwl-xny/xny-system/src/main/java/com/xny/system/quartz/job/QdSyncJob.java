package com.xny.system.quartz.job;

import com.xny.system.service.QdService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class QdSyncJob implements Job {
    @Resource
    private QdService qdService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("同步奇点数据开始==============");
        qdService.getQdData();
        log.info("同步奇点数据结束==============");
    }
}
