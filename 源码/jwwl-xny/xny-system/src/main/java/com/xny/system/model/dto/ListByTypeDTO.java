package com.xny.system.model.dto;

import com.xny.common.enums.DictTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListByTypeDTO {
    @ApiModelProperty("字典类型(传 null 查所有)")
    private DictTypeEnum dictType;
}
