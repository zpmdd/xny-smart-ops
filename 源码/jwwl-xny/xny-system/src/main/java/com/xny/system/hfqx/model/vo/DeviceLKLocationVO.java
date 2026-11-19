package com.xny.system.hfqx.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceLKLocationVO {
    private String Code;
    private String Message;
    private List<HfRealTimeLocation> Data;
}
