package com.xny.system.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CompleDataDTO {
    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间(格式yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间(格式yyyy-MM-dd HH:mm:ss)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
