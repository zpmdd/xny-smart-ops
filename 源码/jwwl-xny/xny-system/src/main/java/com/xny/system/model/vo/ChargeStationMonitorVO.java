package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChargeStationMonitorVO {
    @ApiModelProperty("充电桩数据")
    private ItemVO stationData;

    @ApiModelProperty("综合数据")
    private ItemVO multipleData;

    @ApiModelProperty("使用数据")
    private ItemVO inUseData;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("充电桩状态数据")
    private ItemVO stationStatusData;

    @ApiModelProperty("充电枪状态数据")
    private ItemVO spearStatusData;

    @ApiModelProperty("图表集合数据")
    private List<ChartVO> chartList;
}
