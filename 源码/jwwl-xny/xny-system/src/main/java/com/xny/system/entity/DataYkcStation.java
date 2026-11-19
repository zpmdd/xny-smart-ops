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
 * 云快充充电站数据
 * </p>
 *
 * @author jwwl
 * @since 2024-05-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_ykc_station")
@ApiModel(value = "DataYkcStation对象", description = "云快充充电站数据")
public class DataYkcStation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("站点id")
    @TableId(value = "station_id", type = IdType.ASSIGN_ID)
    private String stationId;

    @ApiModelProperty("充电站名称")
    private String stationName;

    @ApiModelProperty("运营商 ID")
    private String operatorId;

    @ApiModelProperty("设备所属运营平台组织机构代码")
    private String equipmentOwnerId;

    @ApiModelProperty("充电站国家代码")
    private String countryCode;

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
