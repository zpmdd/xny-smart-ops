package com.xny.system.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DetailEquipmentDTO {
    @NotNull(message = "设备编号不能为空")
    @ApiModelProperty(value = "设备编号",required = true)
    private String deviceNumber;
}
