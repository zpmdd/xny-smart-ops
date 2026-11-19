package com.xny.system.yzn.model.vo;

import lombok.Data;

@Data
public class BaseVO {
    private Integer code;
    private String msg;
    private Boolean success;
    private Integer total;
}
