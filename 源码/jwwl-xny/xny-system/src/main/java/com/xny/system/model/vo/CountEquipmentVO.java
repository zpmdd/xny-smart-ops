package com.xny.system.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountEquipmentVO {
    @ApiModelProperty("设备总数")
    private Integer totalCount;

    @ApiModelProperty("在线设备总数")
    private Integer onlineCount;

    @ApiModelProperty("离线设备总数")
    private Integer offlineCount;
}
