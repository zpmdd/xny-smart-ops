package com.xny.system.hfqx.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class LongTimeOffLineData {
    private List<HfEquipmentInfo> ThreeList;
    private List<HfEquipmentInfo> FourList;
    private List<HfEquipmentInfo> FiveList;
    private List<HfEquipmentInfo> OverFiveList;
}
