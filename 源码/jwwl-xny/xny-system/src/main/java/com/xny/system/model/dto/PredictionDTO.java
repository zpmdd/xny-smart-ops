package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

@Data
@Accessors(chain = true)
public class PredictionDTO {
    @Range(min = 1,max = 3,message = "时间类型传值错误")
    @ApiModelProperty(value = "时间类型 1:日 2:周 3:月")
    private Integer type = 1;

    @ApiModelProperty(value = "数据项名称")
    private String itemName;

    @ApiModelProperty(value = "数据项风格")
    private String itemStyle;

    @ApiModelProperty(value = "数据项单位")
    private String itemUnit;

    @ApiModelProperty("参数基础值")
    private Double paramBaseValue;

    @ApiModelProperty("参数值浮动上限")
    private Double paramUpperLimit;

    @ApiModelProperty("参数值浮动下限")
    private Double paramLowerLimit;
}
