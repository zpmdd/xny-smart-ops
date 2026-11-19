package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WindGlobalViewVO {
    @ApiModelProperty("数据一览")
    private ItemVO dataView;

    @ApiModelProperty("发电量数据")
    private ItemVO fdData;

    @ApiModelProperty("收益数据")
    private ItemVO syData;

    @ApiModelProperty("社会贡献")
    private ItemVO socialContribution;

    @ApiModelProperty("风机监测")
    private ItemVO fanMonitor;

    @ApiModelProperty("当前数据")
    private ItemVO currentData;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
