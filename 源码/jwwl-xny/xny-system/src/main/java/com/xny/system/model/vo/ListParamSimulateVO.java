package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ListParamSimulateVO {
    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("数据编号")
    private String dataCode;
}
