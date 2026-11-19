package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ListProjectDTO {
    @ApiModelProperty(value = "项目类型id")
    private Long projectTypeId;

    @ApiModelProperty(value = "项目类型id集合")
    private List<Long> projectTypeIdList;

    @ApiModelProperty(value = "省级编码")
    private String provinceCode;
}
