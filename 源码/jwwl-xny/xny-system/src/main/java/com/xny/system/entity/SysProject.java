package com.xny.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目信息表
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_project")
@ApiModel(value = "SysProject对象", description = "项目信息表")
public class SysProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("项目类型id(字典id)")
    private Long projectTypeId;

    @ApiModelProperty("项目类型名称")
    private String projectTypeName;

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

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建人")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新人")
    private Long updateBy;;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remarks;

    @TableLogic
    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
