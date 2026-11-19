package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EnergyDetailVO {
    @ApiModelProperty(value = "时间")
    private Long time;

    @ApiModelProperty(value = "数值")
    private Double value;

    @ApiModelProperty(value = "时间戳格式化后的字符串")
    private String timeStr;
}
