package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TokenVO {
    @ApiModelProperty(value = "token值")
    public String tokenValue;

    @ApiModelProperty(value = "登录id")
    public Long loginId;

    @ApiModelProperty(value = "用户姓名")
    public String userName;

    @ApiModelProperty(value = "权限标识集合")
    private List<String> permsList;
}
