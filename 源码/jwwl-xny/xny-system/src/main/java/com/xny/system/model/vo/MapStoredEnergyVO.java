package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MapStoredEnergyVO extends MapDataCountVO{
    @ApiModelProperty("今日充电量")
    private ItemVO chargeToday;

    @ApiModelProperty("今日放电量")
    private ItemVO currentDischarge;

    @ApiModelProperty("总充电量")
    private ItemVO totalCharge;

    @ApiModelProperty("总放电量")
    private ItemVO totalDischarge;
}
