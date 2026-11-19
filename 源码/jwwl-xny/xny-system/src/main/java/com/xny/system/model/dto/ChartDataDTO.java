package com.xny.system.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChartDataDTO {
    @ApiModelProperty("项目类型id(字典id)")
    private Long projectTypeId;

    @ApiModelProperty("参数id")
    private Long paramId;

    @ApiModelProperty("设备id")
    private Long deviceId;

    @ApiModelProperty("设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty("参数id集合")
    private List<Long> paramIdList;

    @ApiModelProperty("参数名集合")
    private List<String> paramNameList;

    @ApiModelProperty("参数时间")
    private String paramTime;

    @ApiModelProperty("是否排除自定义参数")
    private boolean excludeCustomParam = false;

    @ApiModelProperty("查询开始行数")
    private Integer limitStart = 0;

    @ApiModelProperty("查询结束行数")
    private Integer limitEnd= 11;

    @ApiModelProperty("是否集中式光伏")
    private boolean ifBpv;

    @ApiModelProperty("子阵")
    private String submatrix;
}
