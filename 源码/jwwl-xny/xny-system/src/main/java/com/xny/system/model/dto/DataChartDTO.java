package com.xny.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xny.common.base.BasePage;
import com.xny.common.enums.ProjectTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DataChartDTO extends BasePage {
    @NotNull(message = "项目类型不能为空")
    @ApiModelProperty(value = "项目类型",required = true)
    ProjectTypeEnum projectType;

    @NotNull(message = "图表id不能为空")
    @ApiModelProperty(value = "图表id",required = true)
    private Long chartId;

    @ApiModelProperty(value = "开始时间(格式yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间(格式yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "项目id",hidden = true)
    private Long projectId;
}
