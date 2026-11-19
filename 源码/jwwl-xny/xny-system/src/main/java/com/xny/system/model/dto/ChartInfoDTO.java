package com.xny.system.model.dto;


import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChartInfoDTO {

    @ApiModelProperty(value = "图表名称")
    private String chartName;

    @ApiModelProperty(value = "图表单位")
    private String chartUnit;

    @ApiModelProperty(value = "图表数据项参数集合")
    private List<ChartItemInfoDTO> chartItemList;

    @ApiModelProperty(value = "数据类型（hour:小时数据(默认),month:月数据）")
    private String dataType = "hour";


    public ChartInfoDTO setChartItem(ChartItemInfoDTO dto){
        if(CollectionUtil.isEmpty(this.chartItemList)){
            this.chartItemList  = new ArrayList<>();
        }
        this.chartItemList.add(dto);
        return this;
    }

}
