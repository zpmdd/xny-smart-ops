package com.xny.system.yzn.model.dto;

import com.xny.system.yzn.model.vo.TotalEnergyVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TotalEnergyDTO extends BaseDTO<TotalEnergyVO>{
    @ApiModelProperty(value = "时间单位，与logtime参数配置（12=天，14=月，17=年）")
    private Integer aggregationCycle;

    @ApiModelProperty(value = "查询节点的类型")
    private String objectLabel;

    @ApiModelProperty(value = "查询节点的id")
    private Integer objectId;

    @ApiModelProperty(value = "当日/当月/当年的零点")
    private Long logTime;

    @Override
    public String getInterface() {
        return "/device/api/v1/query/totalEnergy";
    }
}