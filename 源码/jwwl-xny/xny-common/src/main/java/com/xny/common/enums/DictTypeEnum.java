package com.xny.common.enums;

import lombok.Getter;

/**
 * 字典数据
 */
@Getter
public enum DictTypeEnum {
    project_type("project_type","项目类型");


    private final String value;
    private final String remark;

    DictTypeEnum(String value, String remark){
        this.value = value;
        this.remark = remark;
    }

}








