package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectListVO {
    @ApiModelProperty("参数配置id")
    private Long id;

    @ApiModelProperty("参数名")
    private String paramName;
}
