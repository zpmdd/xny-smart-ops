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
 * 用户表
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户表")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("账号")
    private String accountNumber;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别(0:女,1:男)")
    private Integer sex;

    @ApiModelProperty("职位")
    private String position;

    @ApiModelProperty("是否停用(0:启用,1:停用)")
    private Integer whetherBlockUp;

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
