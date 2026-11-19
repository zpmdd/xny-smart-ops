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
 * 设备参数模拟量
 * </p>
 *
 * @author jwwl
 * @since 2024-01-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_param_simulate")
@ApiModel(value = "SysParamSimulate对象", description = "设备参数模拟量")
public class SysParamSimulate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("数据来源（1:云综能,2:云快充,3:南瑞,4.模拟数据）")
    private Integer dataSource;

    @ApiModelProperty("云综能设备id")
    private Long yznDeviceId;

    @ApiModelProperty("云综能参数id")
    private Long yznParamId;

    @ApiModelProperty("云综能测点id")
    private Long yznMeasureId;

    @ApiModelProperty("参数基础值")
    private Double paramBaseValue;

    @ApiModelProperty("参数值浮动上限")
    private Double paramUpperLimit;

    @ApiModelProperty("参数值浮动下限")
    private Double paramLowerLimit;

    @ApiModelProperty("参数值正常值（状态量）")
    private Double paramNormalValue;

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
