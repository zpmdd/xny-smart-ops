package com.xny.common.enums;

import lombok.Getter;

@Getter
public enum ChartTypeEnum {
    FSD(201L,"辐射度","光电数据实时监测");

    private final Long id;
    private final String itemName;
    private final String remark;

    ChartTypeEnum(Long id,String itemName,String remark){
        this.id = id;
        this.itemName = itemName;
        this.remark = remark;
    }

    public static ChartTypeEnum getById(Long id){
        for (ChartTypeEnum value : ChartTypeEnum.values()) {
            if(value.getId().equals(id)){
                return value;
            }
        }
        throw new RuntimeException("枚举信息查询失败");
    }

}
