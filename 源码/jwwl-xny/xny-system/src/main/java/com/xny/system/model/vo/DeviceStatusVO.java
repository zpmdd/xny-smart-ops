package com.xny.system.model.vo;

import lombok.Data;

@Data
public class DeviceStatusVO {
    private String sum_Discharge;
    private String sum_Charge;
    private String sum_e_positive;
    private String sum_e_reverse;
}
