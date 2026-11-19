package com.xny.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xny.system.entity.QuartzJob;
import org.quartz.SchedulerException;

/**
 * @Description: 定时任务在线管理
 * @Author: nzc
 */
public interface IQuartzJobService extends IService<QuartzJob> {

	boolean saveAndScheduleJob(QuartzJob quartzJob);

	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

	boolean deleteAndStopJob(QuartzJob quartzJob);

	boolean resumeJob(QuartzJob quartzJob);

	void test(String param);
}
