package com.xny.system.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * 云快充设备数据
 * </p>
 *
 * @author jwwl
 * @since 2024-05-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_ykc_equipment")
@ApiModel(value = "DataYkcEquipment对象", description = "云快充设备数据")
public class DataYkcEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备id")
    @TableId(value = "connector_id", type = IdType.ASSIGN_ID)
    private String connectorID;

    @ApiModelProperty("站点id")
    private String stationId;

    @ApiModelProperty("设备状态(0:空闲,1:使用中,2:故障)")
    private Integer status;

    @ApiModelProperty("电流(A)")
    private BigDecimal currentA;

    @ApiModelProperty("电压(V)")
    private BigDecimal voltageA;

    @ApiModelProperty("充电开始时间")
    private Date startTime;

    @ApiModelProperty("充电结束时间")
    private Date endTime;

    @ApiModelProperty("累计总金额(元)")
    private BigDecimal totalMoney;

    @ApiModelProperty("创建人")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    //@TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新人")
    private Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
