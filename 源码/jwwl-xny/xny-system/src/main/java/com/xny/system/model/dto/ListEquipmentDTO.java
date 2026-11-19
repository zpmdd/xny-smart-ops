package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListEquipmentDTO {
    @ApiModelProperty("离线状态(0:离线,1:在线,2:长时间离线)")
    private Integer offlineStatus;
}
