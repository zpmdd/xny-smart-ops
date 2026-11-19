package com.xny.system.quartz.job;

import cn.hutool.core.date.DateUtil;
import com.xny.common.config.XNYConfig;
import com.xny.system.mapper.QuartzJobMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Component
@Slf4j
public class CreateShardingTablesJob implements Job {
    private final static String TABLE_NAME = "data_param_history";

    @Resource
    private QuartzJobMapper quartzJobMapper;

    @Resource
    private XNYConfig xnyConfig;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("======开始执行数据表sys_value_history创建任务");
        this.createShardingTable();
        log.info("======执行数据表sys_value_history创建任务结束");
    }

    @PostConstruct
    public void tableInit(){
        this.createShardingTable();
    }

    /**
     * 创建shardingTable
     */
    private void createShardingTable() {
        Date date = new Date();
        String tableName = xnyConfig.getHistoryDataBase() + "." + TABLE_NAME + "_" +DateUtil.format(date, "yyyyMMdd");
        quartzJobMapper.createTableByName(tableName);

        String tableName2 = xnyConfig.getHistoryDataBase() + "." + TABLE_NAME + "_" +DateUtil.format(DateUtil.offsetDay(date,1), "yyyyMMdd");
        quartzJobMapper.createTableByName(tableName2);
    }

}
