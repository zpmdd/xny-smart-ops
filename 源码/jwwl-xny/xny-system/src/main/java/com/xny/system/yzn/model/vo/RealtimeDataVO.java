package com.xny.system.yzn.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class RealtimeDataVO extends BaseVO{
    private List<RealtimeDetailVO> data;
}
