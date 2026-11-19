package com.xny.system.model.vo;

import com.xny.common.utils.tree.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermsTreeVO implements TreeNode<Long, PermsTreeVO> {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("权限标识")
    private String permsFlag;

    @ApiModelProperty("排序序号")
    private Integer sortNum;

    @ApiModelProperty("是否有权限")
    private boolean havePerm;

    @ApiModelProperty("子菜单集合")
    private List<PermsTreeVO> children = new ArrayList<>();
}
