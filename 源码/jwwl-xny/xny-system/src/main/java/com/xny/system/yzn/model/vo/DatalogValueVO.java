package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DatalogValueVO {
    @ApiModelProperty(value = "时间戳格式化后的字符串")
    private String timeStr;

    @ApiModelProperty(value = "数据值对应时刻的时间戳")
    private Long time;

    @ApiModelProperty(value = "测点数据值")
    private Double value;
}
