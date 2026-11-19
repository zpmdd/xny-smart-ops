package com.xny.system.ykc.model.dto;

import lombok.Data;

@Data
public class ConnectorStatusInfo {
    private String StationID;
    private String ConnectorID;
    private Integer LockStatus;
    private Integer ParkStatus;
    private Integer Status;
}
