package com.xny.system.quartz.job;

import com.xny.system.service.IQuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 带参数的简单的定时任务
 * 
 * @Author nzc
 */
@Component
@Slf4j
public class SampleParamJob implements Job {

	@Resource
	private IQuartzJobService quartzJobService;

	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		log.info(String.format("welcome %s! Ning zaichun 带参数定时任务 SampleParamJob !   时间:" + LocalDateTime.now(), this.parameter));
		quartzJobService.test(this.parameter);
	}
}
