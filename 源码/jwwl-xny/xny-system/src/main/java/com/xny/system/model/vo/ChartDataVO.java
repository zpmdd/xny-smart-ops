package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChartDataVO {
    @ApiModelProperty("数据项值")
    private Double itemValue;

    @ApiModelProperty("数据项标签")
    private String itemLable;
}
