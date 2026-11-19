package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserMapDTO {
    @ApiModelProperty(value = "用户id集合")
    private List<Long> userIdList;
}
