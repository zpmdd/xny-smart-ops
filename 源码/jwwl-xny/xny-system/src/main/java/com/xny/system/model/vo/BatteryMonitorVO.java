package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatteryMonitorVO {
    @ApiModelProperty("顶部数据统计")
    private ItemVO topInfo;

    @ApiModelProperty("运行监测")
    private ItemVO runningMonitor;

    @ApiModelProperty("异常告警")
    private ItemVO abnormalAlarm;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
