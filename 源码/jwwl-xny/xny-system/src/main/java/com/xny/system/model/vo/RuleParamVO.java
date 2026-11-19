package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RuleParamVO {
    @ApiModelProperty("参数值记录时间")
    private Date paramTime;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数值")
    private Double paramValue;

    @ApiModelProperty("参数单位")
    private String paramUnit;

    @ApiModelProperty("状态量值 -1:非状态量 0:正常 1:异常")
    private int statusParamValue;

    @ApiModelProperty("是否使用配置数据")
    private boolean ifConfig;
}
