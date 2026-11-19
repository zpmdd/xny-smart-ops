package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BpvInverterMonitoringVO {
    @ApiModelProperty("逆变器数据统计")
    private ItemVO inverterDataStatistics;

    @ApiModelProperty("其他数据统计")
    private ItemVO otherStatistics;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;

    @ApiModelProperty("逆变器实时数据")
    private List<List<DeviceRealTimeVO>> realTimeData;
}
