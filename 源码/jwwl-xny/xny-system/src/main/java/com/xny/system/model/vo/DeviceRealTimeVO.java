package com.xny.system.model.vo;

import cn.hutool.core.util.NumberUtil;
import com.xny.common.utils.CustomUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DeviceRealTimeVO {
    @ApiModelProperty("数据值")
    private String value;

    @ApiModelProperty("数据状态(0:正常,1:异常)")
    private String status;

    public DeviceRealTimeVO setValue(String value) {
        if(NumberUtil.isDouble(value)){
            this.value = new BigDecimal(value).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
        }else {
            this.value = value;
        }
        return this;
    }
}
