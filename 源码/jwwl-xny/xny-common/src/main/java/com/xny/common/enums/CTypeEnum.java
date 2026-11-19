package com.xny.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum CTypeEnum {
    mobile("mobile","手机端"),
    pad("pad","pad端"),
    pc("pc","pc端"),
    manage("manage","管理后台");

    @EnumValue
    private final String type;
    private final String remark;

    CTypeEnum(String type, String remark){
        this.type = type;
        this.remark = remark;
    }

    public static CTypeEnum getCTypeEnum(String type){
        for (CTypeEnum value : CTypeEnum.values()) {
            if(type.equals(value.getType())){
                return value;
            }
        }
        return null;
    }
}
