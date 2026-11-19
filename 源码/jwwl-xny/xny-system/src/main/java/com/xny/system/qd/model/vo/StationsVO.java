package com.xny.system.qd.model.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class StationsVO {
    private Integer total;
    private List<JSONObject> rows;
}
