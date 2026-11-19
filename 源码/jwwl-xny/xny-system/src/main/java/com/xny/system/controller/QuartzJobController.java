package com.xny.system.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.model.dto.ListQuartzJobDTO;
import com.xny.system.quartz.JobConstant;
import com.xny.system.entity.QuartzJob;
import com.xny.system.service.IQuartzJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


/**
 * @Description: 定时任务在线管理
 * @author nzc
 */
@RestController
@RequestMapping("/quartzJob")
@Slf4j
@Api(tags = "定时任务管理")
public class QuartzJobController {

	@Resource
	private IQuartzJobService quartzJobService;
	
	@Resource
	private Scheduler scheduler;

	/**
	 * 分页列表查询
	 * @return
	 */
	@ApiOperation(value = "分页列表查询")
	@PostMapping(value = "/list")
	public R<PageInfo<QuartzJob>> queryPageList(@RequestBody ListQuartzJobDTO dto) {
		PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
		List<QuartzJob> list = quartzJobService.list(new LambdaQueryWrapper<QuartzJob>()
				.like(StrUtil.isNotBlank(dto.getDescription()),QuartzJob::getDescription,dto.getDescription())
				.like(StrUtil.isNotBlank(dto.getJobClassName()),QuartzJob::getJobClassName,dto.getJobClassName())
		);
		return R.ok(new PageInfo<>(list));
	}

	/**
	 * 添加定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@ApiOperation(value = "添加定时任务")
	@PostMapping(value = "/add")
	public R<String> add(@RequestBody QuartzJob quartzJob) {
		List<QuartzJob> list = quartzJobService.list(new LambdaQueryWrapper<QuartzJob>()
				.eq(QuartzJob::getJobClassName, quartzJob.getJobClassName())
		);
		if (list != null && list.size() > 0) {
			return R.failed("该定时任务类名已存在");
		}
		quartzJobService.saveAndScheduleJob(quartzJob);
		return R.ok("创建定时任务成功");
	}

	/**
	 * 更新定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@ApiOperation(value = "更新定时任务")
	@PostMapping(value = "/edit")
	public R<String> eidt(@RequestBody QuartzJob quartzJob) {
		try {
			quartzJobService.editAndScheduleJob(quartzJob);
		} catch (SchedulerException e) {
			log.error(e.getMessage(),e);
			return R.failed("更新定时任务失败!");
		}
	    return R.ok("更新定时任务成功!");
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "通过id删除")
	@GetMapping(value = "/delete")
	public R<String> delete(@RequestParam(name = "id") String id) {
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			return R.failed("未找到对应实体");
		}
		quartzJobService.deleteAndStopJob(quartzJob);
        return R.ok("删除成功!");

	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "批量删除")
	@GetMapping(value = "/deleteBatch")
	public R<String> deleteBatch(@RequestParam(name = "ids") String ids) {
		if (ids == null || "".equals(ids.trim())) {
			return R.failed("参数不识别！");
		}
		for (String id : StrUtil.split(ids,",")) {
			QuartzJob job = quartzJobService.getById(id);
			quartzJobService.deleteAndStopJob(job);
		}
        return R.ok("删除定时任务成功!");
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "暂停定时任务")
	@GetMapping(value = "/pause")
	public R<String> pauseJob(@RequestParam(name = "id") String id) {
		QuartzJob job;
		try {
			job = quartzJobService.getById(id);
			if (job == null) {
				return R.failed("定时任务不存在！");
			}
			scheduler.pauseJob(JobKey.jobKey(job.getJobClassName().trim()));
		} catch (SchedulerException e) {
			throw new RuntimeException("暂停定时任务失败");
		}
		job.setStatus(JobConstant.STATUS_DISABLE);
		quartzJobService.updateById(job);
		return R.ok("暂停定时任务成功");
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "恢复定时任务")
	@GetMapping(value = "/resume")
	public R<String> resumeJob(@RequestParam(name = "id") String id) {
		QuartzJob job = quartzJobService.getById(id);
		if (job == null) {
			return R.failed("定时任务不存在！");
		}
		quartzJobService.resumeJob(job);
		//scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
		return R.ok("恢复定时任务成功");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "通过id查询")
	@GetMapping(value = "/queryById")
	public R<?> queryById(@RequestParam(name = "id") String id) {
		QuartzJob quartzJob = quartzJobService.getById(id);
        return R.ok(quartzJob);
	}

}
