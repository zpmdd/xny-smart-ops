package com.xny.system.model.dto;

import com.xny.common.base.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListUserDTO extends BasePage {
    @ApiModelProperty(value = "用户姓名或账号")
    private String nameOrAccount;
}
