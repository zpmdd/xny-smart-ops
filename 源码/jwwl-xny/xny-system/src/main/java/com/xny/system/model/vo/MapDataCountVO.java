package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MapDataCountVO {
    @ApiModelProperty("项目总数")
    private ItemVO totalItems;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("运行状况")
    private ItemVO runStatus;

    @ApiModelProperty("告警数据")
    private ItemVO alarmData;
}
