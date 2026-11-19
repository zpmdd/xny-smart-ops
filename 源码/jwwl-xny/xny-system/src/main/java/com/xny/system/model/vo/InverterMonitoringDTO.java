package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InverterMonitoringDTO {
    @ApiModelProperty("是否集中式光伏,默认false")
    private boolean ifBpv = false;
}
