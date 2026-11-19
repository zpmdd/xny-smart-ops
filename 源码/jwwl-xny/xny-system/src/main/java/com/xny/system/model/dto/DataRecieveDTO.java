package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class DataRecieveDTO {
    @NotNull(message = "子阵区间不能为空")
    @Range(min = 1,max = 5,message = "子阵区间传值错误")
    @ApiModelProperty(value = "子阵区间（1: 0-9，2: 10-18，3: 19-27，4: 28-36，5:升压站）")
    private Integer area;

    @NotEmpty(message = "子阵区间不能为空")
    @ApiModelProperty(value = "数据信息")
    private Map<String,String> data;
}
