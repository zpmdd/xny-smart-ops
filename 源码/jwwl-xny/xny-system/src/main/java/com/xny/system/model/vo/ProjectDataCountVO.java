package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectDataCountVO {
    @ApiModelProperty(value = "项目类型id")
    private Long projectTypeId;

    @ApiModelProperty(value = "项目类型名称")
    private String projectTypeName;

    @ApiModelProperty(value = "参数id")
    private Long paramId;

    @ApiModelProperty(value = "参数名称")
    private String paramName;

    @ApiModelProperty(value = "参数累计值")
    private Double totalValue;

    @ApiModelProperty("是否使用配置数据")
    private boolean ifConfig;
}
