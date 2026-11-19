package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class BurstingDataStatisticsDTO {
    @Range(min = 1,max = 3,message = "时间类型传值错误")
    @ApiModelProperty(value = "时间类型 1:日 2:周 3:月")
    private Integer type = 1;
}
