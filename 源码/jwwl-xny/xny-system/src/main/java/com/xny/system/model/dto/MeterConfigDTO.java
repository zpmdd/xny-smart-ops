package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class MeterConfigDTO {
    @NotNull(message = "参数id不能为空")
    @ApiModelProperty(value = "参数id")
    private Long paramId;


    @NotNull(message = "设备id不能为空")
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
}
