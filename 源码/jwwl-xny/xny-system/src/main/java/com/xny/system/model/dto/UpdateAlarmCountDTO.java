package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateAlarmCountDTO {
    @ApiModelProperty("告警次数")
    private Integer alramCount;

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("类型 1:修改 2：累计")
    private Integer type = 1;
}
