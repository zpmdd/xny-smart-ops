package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceMonitorVO {
    @ApiModelProperty("储能信息监测")
    private ItemVO storageInfoMonitor;

    @ApiModelProperty("储能状态监测")
    private ItemVO storageStatusMonitor;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;
}
