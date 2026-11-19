package com.xny.system.model.dto;

import com.xny.common.base.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListQuartzJobDTO extends BasePage {

    @ApiModelProperty("任务类名")
    private String jobClassName;

    @ApiModelProperty("描述")
    private String description;
}
