package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BlowerMonitorVO {
    @ApiModelProperty("顶部信息")
    private ItemVO topInfo;

    @ApiModelProperty("风机信息")
    private ItemVO blowerInfo;
}
