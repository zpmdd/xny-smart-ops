package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BurstingGlobalViewVO {
    @ApiModelProperty("设备集合")
    private List<ListDeviceVO> deviceList;

    @ApiModelProperty("发电数据")
    private ItemVO fdData;

    @ApiModelProperty("收益数据")
    private ItemVO syData;

    @ApiModelProperty("其他数据")
    private ItemVO otherData;

    @ApiModelProperty("环境数据")
    private ItemVO envData;

    @ApiModelProperty("逆变器数据")
    private ItemVO inverterData;

    @ApiModelProperty("故障数据")
    private ItemVO failureData;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("社会效益")
    private ItemVO socialBenefit;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
