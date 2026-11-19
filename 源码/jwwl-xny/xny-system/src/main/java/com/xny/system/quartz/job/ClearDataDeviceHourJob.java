package com.xny.system.quartz.job;

import com.xny.system.mapper.QuartzJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
@Slf4j
public class ClearDataDeviceHourJob implements Job {
    @Resource
    private QuartzJobMapper quartzJobMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        quartzJobMapper.clearDataDeviceHour();
    }
}
