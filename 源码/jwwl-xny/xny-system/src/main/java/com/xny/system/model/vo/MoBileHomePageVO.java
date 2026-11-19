package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MoBileHomePageVO {
    @ApiModelProperty("发电量占比分析")
    private ChartVO powerScale;

    @ApiModelProperty("发电量统计")
    private ItemVO powerCount;

    @ApiModelProperty("运维数据")
    private ItemVO devOpsData;

    @ApiModelProperty("社会效益")
    private ItemVO socialBenefit;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
