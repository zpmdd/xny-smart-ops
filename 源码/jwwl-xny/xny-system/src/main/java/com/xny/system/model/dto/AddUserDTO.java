package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddUserDTO {
    @NotBlank(message = "用户姓名不能为空")
    @ApiModelProperty(value = "用户姓名",required = true)
    private String userName;

    @NotBlank(message = "账号不能为空")
    @ApiModelProperty(value = "账号",required = true)
    private String accountNumber;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @NotNull(message = "性别能为空")
    @Range(min = 0,max = 1,message = "性别传值错误")
    @ApiModelProperty(value = "性别(0:女,1:男)",required = true)
    private Integer sex;

    //@NotBlank(message = "职位不能为空")
    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "权限id集合")
    private List<Long> permsList;
}
