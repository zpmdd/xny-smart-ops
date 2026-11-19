package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BpvInverterMonitoringDTO {
    @ApiModelProperty("子阵(默认#01子阵)")
    private String submatrix = "#01子阵";
}
