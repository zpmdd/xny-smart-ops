package com.xny.system.model.dto;

import com.xny.common.enums.ProjectTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class SelectListDTO {
    @NotNull(message = "项目类型不能为空")
    @ApiModelProperty(value = "项目类型",required = true)
    ProjectTypeEnum projectType;
}
