package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DetailEquipmentVO {
    @ApiModelProperty("设备详情")
    List<ItemVO> equipmentDetail;
}
