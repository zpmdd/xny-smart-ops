package com.xny.system.qd.model.vo;

import lombok.Data;

@Data
public class DevicetransientStatusVO {
    private String status;
    private String rated_Capacity;
    private String soc_lower_limit;
    private String metering_point;
    private String soc_upper_limit;
    private String soc;
    private String rated_Power;
}
