package com.xny.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.entity.QuartzJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 定时任务在线管理
 * @Author: nzc
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
    void createTableByName(@Param(value = "tableName") String tableName);

    void clearDataDeviceHour();
}
