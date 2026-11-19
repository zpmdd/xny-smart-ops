package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DatalogDetailVO {
    @ApiModelProperty(value = "数据ID")
    private Integer dataId;

    @ApiModelProperty(value = "数据类型ID(常用类型1-实时值，2-最大值，3-最小值，4-平均值，5-95概率值)")
    private Integer dataTypeId;

    @ApiModelProperty(value = "设备ID")
    private Integer deviceId;

    @ApiModelProperty(value = "回路号(单回路设备节点，传1即可。多回路，查询时传对应的回路号即可)")
    private Integer logicalId;

    @ApiModelProperty(value = "dataList集合")
    private List<DatalogValueVO> dataList;
}
