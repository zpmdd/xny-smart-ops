package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateDataCountDTO {
    @NotNull(message = "参数配置id不能为空")
    @ApiModelProperty(value = "参数配置id",required = true)
    private Long id;

    @ApiModelProperty("参数value")
    private String paramValue;

    @ApiModelProperty("是否启用(0:停用,1:启用)")
    private Integer ifEnable;
}
