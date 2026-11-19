package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MapChcChargeVO  extends MapDataCountVO{
    @ApiModelProperty("总收益")
    private ItemVO totalRevenue;

    @ApiModelProperty("总充电量")
    private ItemVO totalCharge;

    @ApiModelProperty("总充电时长")
    private ItemVO totalChargeTime;

    @ApiModelProperty("总充电次数")
    private ItemVO totalChargeTimes;
}
