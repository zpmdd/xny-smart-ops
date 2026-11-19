package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListEquipmentVO {
    @ApiModelProperty("设备编号")
    private String deviceNumber;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("长时间离线天数")
    private String offlineDays;
}
