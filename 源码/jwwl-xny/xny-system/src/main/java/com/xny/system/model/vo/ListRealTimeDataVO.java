package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListRealTimeDataVO {
    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数名称")
    private String paramName;

    @ApiModelProperty("参数单位")
    private String paramUnit;

    @ApiModelProperty("参数值")
    private Double paramValue;

    @ApiModelProperty("状态量值 -1:非状态量 0:正常 1:异常")
    private int statusParamValue;

    @ApiModelProperty("设备备注")
    private String deviceRemark;

    @ApiModelProperty("放置位置")
    private Integer place;

}
