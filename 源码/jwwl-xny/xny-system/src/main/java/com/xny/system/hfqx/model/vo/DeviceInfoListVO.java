package com.xny.system.hfqx.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceInfoListVO {
    private String Code;
    private String Message;
    private List<HfEquipmentInfo> Data;
}
