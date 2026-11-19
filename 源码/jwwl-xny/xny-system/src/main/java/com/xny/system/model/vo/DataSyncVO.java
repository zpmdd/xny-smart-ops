package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSyncVO {
    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("参数id")
    private Integer paramId;

    @ApiModelProperty("通道id")
    private Integer yznChannelId;

    @ApiModelProperty("场站id")
    private Integer yznStationId;

    @ApiModelProperty("设备id")
    private Integer yznDeviceId;

    @ApiModelProperty("参数id")
    private Integer yznParamId;

    @ApiModelProperty("测点id")
    private Integer yznMeasureId;

    @ApiModelProperty("参数基础值")
    private Double paramBaseValue;

    @ApiModelProperty("参数值浮动上限")
    private Double paramUpperLimit;

    @ApiModelProperty("参数值浮动下限")
    private Double paramLowerLimit;

    @ApiModelProperty("参数值正常值（状态量）")
    private Double paramNormalValue;
}
