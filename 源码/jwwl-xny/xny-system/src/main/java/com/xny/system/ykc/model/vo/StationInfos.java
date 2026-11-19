package com.xny.system.ykc.model.vo;


import lombok.Data;
import java.util.List;

@Data
public class StationInfos {
    private Integer PageNo;
    private Integer PageCount;
    private Integer ItemSize;
    private List<StationInfo> StationInfos;
}
