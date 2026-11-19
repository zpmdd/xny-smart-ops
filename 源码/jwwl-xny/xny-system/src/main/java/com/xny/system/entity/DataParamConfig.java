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
 * 设备参数配置
 * </p>
 *
 * @author jwwl
 * @since 2024-06-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_param_config")
@ApiModel(value = "DataParamConfig对象", description = "设备参数配置")
public class DataParamConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("项目id")
    private Long projectId;

    @ApiModelProperty("图表id")
    private Long chartId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数key")
    private String paramKey;

    @ApiModelProperty("参数value")
    private String paramValue;

    @ApiModelProperty("参数单位")
    private String paramUnit;

    @ApiModelProperty("是否隐藏参数（0:否,1:是）")
    private Integer ifHide;

    @ApiModelProperty("是否启用(0:停用,1:启用)")
    private Integer ifEnable;

    @ApiModelProperty("数据类型（1:数据统计,2:图表）")
    private Integer dataType;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新人")
    private Long updateBy;;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
