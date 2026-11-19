package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MapPowerPlantVO extends MapDataCountVO{
    @ApiModelProperty("社会效益")
    private ItemVO socialBenefit;

    @ApiModelProperty("总发电量")
    private ItemVO grossGeneration;

    @ApiModelProperty("总装机容量")
    private ItemVO grossCapacity;

    @ApiModelProperty("总收益")
    private ItemVO totalRevenue;;

    @ApiModelProperty("总上网")
    private ItemVO totalAccess;

    @ApiModelProperty("图表集合")
    private List<ChartVO> chartList;
}
