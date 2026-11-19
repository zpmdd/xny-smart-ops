package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListParamSimulateDTO {
    @ApiModelProperty(value = "数据编号集合")
    private List<String> dataCodeList;

    @ApiModelProperty(value = "子阵区间")
    private Integer submatrixSection;
}
