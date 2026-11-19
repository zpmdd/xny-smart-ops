package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class LoginDTO {
    @NotBlank(message = "登陆账号不能为空")
    @ApiModelProperty(value = "登陆账号",required = true)
    private String accountNumber;

    @NotBlank(message = "登录密码不能为空")
    @ApiModelProperty(value = "登录密码",required = true)
    private String password;

    @ApiModelProperty(value = "验证码")
    private String captcha;

}
