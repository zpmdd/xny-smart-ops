package com.xny.common.utils.tide.entity;

import lombok.Data;

/**
 * 数据实体类
 *
 * @author zhuqianwei
 */
@Data
public class TimeAndLevel {

    private String time;
    private Double value;

    public TimeAndLevel(){
        super();
    }
    public TimeAndLevel(String time,Double value){
        this.time = time;
        this.value = value;
    }

}
