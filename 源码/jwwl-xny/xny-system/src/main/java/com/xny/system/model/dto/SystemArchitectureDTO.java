package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SystemArchitectureDTO {
    @ApiModelProperty("是否集中式光伏,默认false")
    private boolean ifBpv = false;
}
