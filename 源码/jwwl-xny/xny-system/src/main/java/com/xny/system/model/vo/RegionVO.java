package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RegionVO {
    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("项目地址-省行政编码")
    private String provinceCode;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("项目地址-市行政编码")
    private String cityCode;

    @ApiModelProperty("项目数量")
    private Integer projectCount = 0;

    @ApiModelProperty("项目列表")
    private List<ProjectVO> projectList;
}
