package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class DataChartVO{
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图表名")
    private String chartName;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数value")
    private String paramValue;

    @ApiModelProperty("参数单位")
    private String paramUnit;

    @ApiModelProperty("参数key")
    private String paramKey;

    @ApiModelProperty("操作人")
    private String updateName;

    @ApiModelProperty("操作时间")
    private Date updateTime;
}
