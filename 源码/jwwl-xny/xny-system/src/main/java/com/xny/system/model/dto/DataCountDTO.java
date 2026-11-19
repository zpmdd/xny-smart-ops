package com.xny.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xny.common.base.BasePage;
import com.xny.common.enums.ProjectTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class DataCountDTO extends BasePage {
    @NotNull(message = "项目类型不能为空")
    @ApiModelProperty(value = "项目类型",required = true)
    ProjectTypeEnum projectType;

    @ApiModelProperty("参数配置id")
    private Long id;

    @ApiModelProperty(value = "开始时间(格式yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间(格式yyyy-MM-dd)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "项目id",hidden = true)
    private Long projectId;

    @ApiModelProperty(value = "参数配置id集合",hidden = true)
    private List<Long> idList;
}
