package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RealtimeDetailVO {
    @ApiModelProperty(value = "厂站id")
    private Integer stationId;

    @ApiModelProperty(value = "通道id")
    private Integer channelId;

    @ApiModelProperty(value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty(value = "量测点ID")
    private Integer measureId;

    @ApiModelProperty(value = "实时数据值")
    private Double value;

    @ApiModelProperty(value = "系统参数ID")
    private Integer sysParamId;

    @ApiModelProperty(value = "系统设备ID")
    private Integer sysDeviceId;

    @ApiModelProperty(value = "系统参数正常值")
    private Double sysNormalValue;

    @ApiModelProperty(value = "系统时间")
    private Date sysDate;
}
