package com.xny.system.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class GetWarningsVO {
    private List<OfflineAlertVO> offlineAlert;
    private List<UnplannedOperationVO> unplannedOperation;
}
