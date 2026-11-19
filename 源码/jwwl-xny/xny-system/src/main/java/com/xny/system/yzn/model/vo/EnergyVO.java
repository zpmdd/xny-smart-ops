package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class EnergyVO  extends BaseVO{
    @ApiModelProperty(value = "数据集合")
    private List<EnergyDetailVO> data;
}
