package com.xny.system.yzn.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TotalEnergyVO extends BaseVO{
    @ApiModelProperty(value = "结果集(如果有用电量，第一个记录将返回总用电量，如果无用电数据，将为空)")
    List<Double> data;
}

