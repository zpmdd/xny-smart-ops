package com.xny.common.enums;

import lombok.Getter;

@Getter
public enum ProjectTypeEnum {
    FBSGF(1L,"1","分布式光伏"),
    JZSGF(2L,"2","集中式光伏"),
    CNXM(3L,"3","储能项目"),
    CDXM(4L,"4","充电项目"),
    FDXM(5L,"5","风电项目"),
    SDXM(6L,"6","水电项目");


    private final Long id;
    private final String icon;
    private final String remark;

    ProjectTypeEnum(Long id,String icon, String remark){
        this.id = id;
        this.icon = icon;
        this.remark = remark;
    }

    public static ProjectTypeEnum getById(Long id){
        for (ProjectTypeEnum value : ProjectTypeEnum.values()) {
            if(value.getId().equals(id)){
                return value;
            }
        }
        throw new RuntimeException("枚举信息查询失败");
    }
}
