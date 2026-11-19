package com.xny.system.yzn.model.dto;

import com.xny.system.yzn.model.vo.EnergyVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EnergyDTO extends BaseDTO<EnergyVO>{
    @ApiModelProperty(value = "查询数据的时间间隔，（7=小时，12=天，13=周，14=月，17=年）",required = true)
    private Integer aggregationCycle;

    @ApiModelProperty(value = "查询节点的类型",required = true)
    private String objectLabel;

    @ApiModelProperty(value = "查询节点的id",required = true)
    private Integer objectId;

    @ApiModelProperty(value = "查询起始时刻（unix时间戳）",required = true)
    private Long startTime;

    @ApiModelProperty(value = "查询结束时刻（unix时间戳）",required = true)
    private Long endTime;

    @Override
    public String getInterface() {
        return "device/api/v1/query/energy";
    }
}