package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemArchitectureVO {
    @ApiModelProperty("逆变器设备数据")
    private ItemVO inverterDeviceData;

    @ApiModelProperty("储能设备数据")
    private ItemVO storageDeviceData;

    @ApiModelProperty("设备电数据")
    private ItemVO electricData;

    @ApiModelProperty("逆变器实时数据")
    private List<List<DeviceRealTimeVO>> realTimeData;

    @ApiModelProperty("储能设备状态")
    private String storageStatus;
}
