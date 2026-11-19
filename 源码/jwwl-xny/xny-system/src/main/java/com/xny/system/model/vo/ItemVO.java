package com.xny.system.model.vo;

import cn.hutool.json.JSONObject;
import com.xny.common.utils.CustomUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ItemVO {
    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "数值")
    private String value;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "扩展字段")
    private JSONObject ext;

    @ApiModelProperty(value = "子数据")
    private List<ItemVO> childs;

    public ItemVO setValue(String value) {
        this.value = CustomUtils.removeZeros(value);
        return this;
    }
}
