package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class NewDeviceMonitorVO {
    @ApiModelProperty("储能信息监测")
    private ItemVO storageInfoMonitor;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("运行监测")
    private ItemVO runningMonitor;

    @ApiModelProperty("告警信息列表")
    private List<List<String>> warnList;
}
