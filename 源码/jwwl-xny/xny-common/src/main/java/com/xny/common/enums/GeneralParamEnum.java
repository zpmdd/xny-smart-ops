package com.xny.common.enums;

import lombok.Getter;

@Getter
public enum GeneralParamEnum {
    XMZS(0L,null,"项目总数","0"),
    ZFDL(110004L,"kWh","总发电量","1"),
    ZZJRL(100009L,"kW","总装机容量","2"),
    ZSY(100005L,"万元","总收益","3"),
    ZSW(100008L,"kWh","总上网","4"),
    JRCD(120035L,"kWh","今日充电量","5"),
    JRFD(120036L,"kWh","今日放电量","6"),
    TCD(120037L,"kWh","总充电量","7"),
    TFD(120038L,"kWh","总放电量","8"),
    ZCT(100006L,"小时","总充电时长","9"),
    ZCTS(100007L,"次","总充电次数","10"),
    JRZFDL(110003L,"kWh","今日总发电量","11"),
    BYZFDL(110041L,"kWh","本月总发电量","12"),
    NFDL(100001L,"kWh","年发电量","13");


    private final Long id;
    private final String unit;
    private final String remark;
    private final String icon;

    GeneralParamEnum(Long id,String unit, String remark, String icon){
        this.id = id;
        this.unit = unit;
        this.remark = remark;
        this.icon = icon;
    }
}
