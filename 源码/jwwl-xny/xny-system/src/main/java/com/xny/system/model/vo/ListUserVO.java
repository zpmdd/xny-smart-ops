package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class ListUserVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别(0:女,1:男)")
    private Integer sex;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("是否停用(0:启用,1:停用)")
    private Integer whetherBlockUp;

    @ApiModelProperty("更新人id")
    private Long updateBy;

    @ApiModelProperty("更新人姓名")
    private String updateName;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
