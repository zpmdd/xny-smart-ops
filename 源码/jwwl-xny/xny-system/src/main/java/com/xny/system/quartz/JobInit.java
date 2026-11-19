package com.xny.system.quartz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.system.entity.QuartzJob;
import com.xny.system.service.IQuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class JobInit {
    @Resource
    private IQuartzJobService quartzJobService;

    @PostConstruct
    public void init(){
        log.info("=========初始化定时任务开始");
        List<QuartzJob> list = quartzJobService.list(new LambdaQueryWrapper<QuartzJob>()
                .eq(QuartzJob::getStatus, 0)
        );
        for (QuartzJob quartzJob : list) {
            quartzJobService.resumeJob(quartzJob);
        }
        log.info("=========初始化定时任务结束");
    }
}
