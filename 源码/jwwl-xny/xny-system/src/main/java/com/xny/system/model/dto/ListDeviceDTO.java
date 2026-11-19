package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListDeviceDTO {
    @ApiModelProperty("项目类型id(字典id)")
    private Long projectTypeId;

    @ApiModelProperty("项目类型id集合")
    private List<Long> projectTypeIdList;

    @ApiModelProperty("参数id逗号分隔")
    private String projectIds;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数id集合")
    private List<Long> paramIdList;

    @ApiModelProperty("设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty("参数id逗号分隔")
    private String paramIds;
}
