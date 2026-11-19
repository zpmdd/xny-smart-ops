package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class YznDataLogDTO {
    @ApiModelProperty(value = "设备参数集合")
    private List<MeterConfigDTO> paramList;;

    @ApiModelProperty(value = "开始时间(格式yyyy-MM-dd HH:mm:ss)")
    private String startTime;

    @ApiModelProperty(value = "结束时间(格式yyyy-MM-dd HH:mm:ss)")
    private String endTime;
}
