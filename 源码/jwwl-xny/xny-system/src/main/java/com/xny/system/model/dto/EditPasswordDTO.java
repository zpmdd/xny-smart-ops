package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EditPasswordDTO {
    @NotBlank(message = "原密码不能为空")
    @ApiModelProperty(value = "原密码",required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码",required = true)
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty(value = "确认密码",required = true)
    private String confirmPassword;
}
