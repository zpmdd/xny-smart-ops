package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermsTreeDTO {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "查询已有权限（默认：false）")
    private Boolean ifExist = false;
}
