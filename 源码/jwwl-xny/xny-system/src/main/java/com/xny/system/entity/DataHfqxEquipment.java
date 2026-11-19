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
 * 红枫清洗设备数据
 * </p>
 *
 * @author jwwl
 * @since 2024-05-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("data_hfqx_equipment")
@ApiModel(value = "DataHfqxEquipment对象", description = "红枫清洗设备数据")
public class DataHfqxEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备编号")
    @TableId(value = "device_number", type = IdType.ASSIGN_ID)
    private String deviceNumber;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("工作模式")
    private String workMode;

    @ApiModelProperty("工作状态")
    private String workStatus;

    @ApiModelProperty("离线状态(0:离线,1:在线)")
    private Integer offlineStatus;

    @ApiModelProperty("长时间离线天数")
    private String offlineDays;

    @ApiModelProperty("起点")
    private String startAddress;

    @ApiModelProperty("终点")
    private String endAddress;

    @ApiModelProperty("实时位置")
    private String realTimeAddress;

    @ApiModelProperty("是否在起点(0:否,1:是)")
    private Integer isStart;

    @ApiModelProperty("是否在终点(0:否,1:是)")
    private Integer isEnd;

    @ApiModelProperty("启动时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

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

    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
