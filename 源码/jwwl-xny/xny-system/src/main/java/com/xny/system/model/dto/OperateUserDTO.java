package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class OperateUserDTO {
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id",required = true)
    private Long userId;

    @NotNull(message = "操作类型不能为空")
    @Range(min = 0,max = 2,message = "操作类型传值错误")
    @ApiModelProperty(value = "操作类型(0:启用,1:停用,2:删除)",required = true)
    private Integer operateType;
}
