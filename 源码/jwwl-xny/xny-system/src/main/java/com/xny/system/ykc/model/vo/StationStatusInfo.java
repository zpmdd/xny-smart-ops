package com.xny.system.ykc.model.vo;

import com.xny.system.ykc.model.dto.ConnectorStatusInfo;
import lombok.Data;

import java.util.List;

@Data
public class StationStatusInfo {
    private String StationID;
    private List<ConnectorStatusInfo> ConnectorStatusInfos;
}
