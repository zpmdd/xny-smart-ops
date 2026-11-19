package com.xny.system.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DataCountVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数value")
    private String paramValue;

    @ApiModelProperty("参数单位")
    private String paramUnit;

    @ApiModelProperty("是否启用(0:停用,1:启用)")
    private Integer ifEnable;

    @ApiModelProperty("操作人")
    private String updateName;

    @ApiModelProperty("操作时间")
    private Date updateTime;

    @ApiModelProperty("是否数值")
    private Boolean ifNumber = true;

}
