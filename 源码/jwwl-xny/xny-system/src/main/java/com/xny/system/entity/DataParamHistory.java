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
 * 参数数据信息表-历史数据
 * </p>
 *
 * @author jwwl
 * @since 2023-12-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_param_history")
@ApiModel(value = "DataParamHistory对象", description = "参数数据信息表-历史数据")
public class DataParamHistory implements Serializable {

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

    //@TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    //@TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新人")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
