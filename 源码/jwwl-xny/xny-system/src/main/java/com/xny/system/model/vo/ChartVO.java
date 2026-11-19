package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChartVO {
    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("样式")
    private String style;

    @ApiModelProperty("数值")
    private Double value;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("子集")
    private List<ChartVO> childs;

}