package com.xny.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备数据信息表-实时数据
 * </p>
 *
 * @author jwwl
 * @since 2023-12-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_device_real_time")
@ApiModel(value = "DataDeviceRealTime对象", description = "设备数据信息表-实时数据")
public class DataDeviceRealTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数值")
    private Double paramValue;

    @ApiModelProperty("参数值记录时间")
    private Date paramTime;

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
