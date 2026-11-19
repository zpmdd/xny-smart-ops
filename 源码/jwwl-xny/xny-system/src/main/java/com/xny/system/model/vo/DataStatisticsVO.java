package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataStatisticsVO {
    @ApiModelProperty("顶部数据统计")
    private ItemVO topDataStatistics;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
