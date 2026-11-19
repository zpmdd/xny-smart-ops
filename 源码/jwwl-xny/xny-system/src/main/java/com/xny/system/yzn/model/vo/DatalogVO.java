package com.xny.system.yzn.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class DatalogVO extends BaseVO{
    private List<DatalogDetailVO> data;
}

