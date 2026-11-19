package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class PvGlobalOverviewVO {
    @ApiModelProperty("设备集合")
    private List<ListDeviceVO> deviceList;

    @ApiModelProperty("社会效益")
    private ItemVO socialBenefit;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("今日环境数据")
    private ItemVO envDataToday;

    @ApiModelProperty("数据一览")
    private ItemVO dataView;

    @ApiModelProperty("其他统计")
    private ItemVO otherStatistics;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;

}
