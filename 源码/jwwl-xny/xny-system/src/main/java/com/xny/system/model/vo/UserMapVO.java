package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMapVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String userName;
}
