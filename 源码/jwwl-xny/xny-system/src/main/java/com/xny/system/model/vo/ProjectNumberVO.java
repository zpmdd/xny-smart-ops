package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectNumberVO {
    @ApiModelProperty(value = "项目名称")
    private String projectTypeName;

    @ApiModelProperty(value = "项目数量")
    private Double totalCount;
}
