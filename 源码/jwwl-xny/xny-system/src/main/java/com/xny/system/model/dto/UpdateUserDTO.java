package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateUserDTO {
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "账号")
    private String accountNumber;

    @ApiModelProperty(value = "密码")
    private String password;

    @Range(min = 0,max = 1,message = "性别传值错误")
    @ApiModelProperty(value = "性别(0:女,1:男)")
    private Integer sex;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "权限id集合")
    private List<Long> permsList = new ArrayList<>();
}
