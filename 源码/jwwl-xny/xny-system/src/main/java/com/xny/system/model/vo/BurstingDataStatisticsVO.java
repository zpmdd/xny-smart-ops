package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BurstingDataStatisticsVO{
    @ApiModelProperty("顶部信息")
    private ItemVO topInfo;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
