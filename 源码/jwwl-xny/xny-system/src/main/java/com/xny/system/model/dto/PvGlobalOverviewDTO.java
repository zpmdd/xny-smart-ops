package com.xny.system.model.dto;

import com.xny.common.enums.ProjectTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PvGlobalOverviewDTO {
    @ApiModelProperty(value = "项目类型")
    private ProjectTypeEnum projectType;
}
