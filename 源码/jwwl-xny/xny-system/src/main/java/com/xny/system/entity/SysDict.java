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
 * 数据字典
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_dict")
@ApiModel(value = "SysDict对象", description = "数据字典")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("字典Label")
    private String dictLabel;

    @ApiModelProperty("字典value")
    private String dictValue;

    @ApiModelProperty("字典类型")
    private String dictType;

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

    @ApiModelProperty("扩展字段")
    private String ext;

    @TableLogic
    @ApiModelProperty("删除标志(0:正常,1:已删除)")
    private Integer delFlag;
}
