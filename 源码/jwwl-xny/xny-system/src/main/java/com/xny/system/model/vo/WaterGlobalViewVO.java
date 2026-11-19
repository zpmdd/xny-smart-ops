package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WaterGlobalViewVO {
    @ApiModelProperty("数据一览")
    private ItemVO dataView;

    @ApiModelProperty("实时数据")
    private ItemVO realTime;

    @ApiModelProperty("社会贡献")
    private ItemVO socialContribution;

    @ApiModelProperty("当日潮汐预报-高低潮")
    private List<List<DeviceRealTimeVO>> timeAndLevel;

    @ApiModelProperty("当日潮流预报-高低潮")
    private List<List<DeviceRealTimeVO>> velocityOfFlow;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;

}
