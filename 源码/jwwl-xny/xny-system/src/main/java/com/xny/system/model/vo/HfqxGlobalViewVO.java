package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HfqxGlobalViewVO {
    @ApiModelProperty("数据一览")
    private ItemVO dataView;

    @ApiModelProperty("长时间离线设备信息")
    private ItemVO ltOffLineEquipmentData;

    @ApiModelProperty("在线设备信息")
    private ItemVO onlineEquipmentData;
}
