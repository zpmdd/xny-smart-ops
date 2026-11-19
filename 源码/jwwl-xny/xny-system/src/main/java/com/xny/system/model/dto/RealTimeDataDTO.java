package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RealTimeDataDTO {
    @ApiModelProperty(value = "设备参数集合")
    private List<MeterConfigDTO> paramList;

    @ApiModelProperty(value = "测点id是否为空")
    private Boolean measureIdIfNull;

    @ApiModelProperty(value = "云综能参数id是否为空")
    private Boolean yznParamIdIfNull;

    @ApiModelProperty(value = "是否开发环境")
    private Boolean ifDev;

    @ApiModelProperty(value = "数据来源集合（数据来源（1:云综能,2:云快充,3:南瑞,4.模拟数据））")
    private List<Integer> dataSourceList;
}
