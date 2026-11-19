package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ListDeviceVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("放置位置")
    private String place;

    @ApiModelProperty("参数值")
    private Double paramValue;

    @ApiModelProperty("其他数据")
    private ItemVO otherData;

    @ApiModelProperty("社会效益")
    private ItemVO socialBenefit;
}
