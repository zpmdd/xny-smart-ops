package com.xny.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePage {
    @ApiModelProperty("当前页码")
    private Integer pageNum = 1;

    @ApiModelProperty("每页数量")
    private Integer pageSize = 10;
}