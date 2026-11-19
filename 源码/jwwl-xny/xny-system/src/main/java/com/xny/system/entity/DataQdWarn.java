package com.xny.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 奇点告警数据
 * </p>
 *
 * @author jwwl
 * @since 2024-07-30
 */
@Getter
@Setter
//@Accessors(chain = true)
@TableName("data_qd_warn")
@ApiModel(value = "DataQdWarn对象", description = "奇点告警数据")
public class DataQdWarn implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    @ApiModelProperty("电站下告警设备uuid")
    private String uuid;

    @ApiModelProperty("告警设备类型，如：station、elink、eblock、meter")
    private String type;

    @ApiModelProperty("告警具体设备如：eblock、PCS名称")
    private String name;

    @ApiModelProperty("告警属于那个电站")
    private String stationId;

    @ApiModelProperty("告警触发的时间")
    private Date startTime;

    @ApiModelProperty("告警关闭的时间")
    private Date endTime;

    @ApiModelProperty("告警类型（1:离线告警,2:非计划运行告警）")
    private Integer warnType;

    @ApiModelProperty("创建人")
    private Long createBy;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
