package com.xny.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务管理表
 * </p>
 *
 * @author jwwl
 * @since 2023-12-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_quartz_job")
@ApiModel(value = "SysQuartzJob对象", description = "定时任务管理表")
public class QuartzJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("任务类名")
    private String jobClassName;

    @ApiModelProperty("cron表达式")
    private String cronExpression;

    @ApiModelProperty("参数")
    private String parameter;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("状态(0正常,-1停止)")
    private Integer status;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
