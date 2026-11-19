package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class ProjectVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("项目类型id(字典id)")
    private Long projectTypeId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("负责人姓名")
    private String headName;

    @ApiModelProperty("联系方式")
    private String contactInfo;

    @ApiModelProperty("项目地址-省")
    private String province;

    @ApiModelProperty("项目地址-市")
    private String city;

    @ApiModelProperty("项目地址-区")
    private String area;

    @ApiModelProperty("项目地址-详细地址")
    private String address;

    @ApiModelProperty("项目地址-省行政编码")
    private String provinceCode;

    @ApiModelProperty("项目地址-市行政编码")
    private String cityCode;

    @ApiModelProperty("项目地址-经度")
    private String longitude;

    @ApiModelProperty("项目地址-纬度")
    private String latitude;
}
