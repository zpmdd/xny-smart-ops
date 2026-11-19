package com.xny.system.service;

import cn.hutool.json.JSONObject;


public interface YkcService {
    JSONObject queryToken(JSONObject obj);

    JSONObject stationStatus(JSONObject obj);

    JSONObject chargeStatus(JSONObject obj);

    JSONObject chargeOrderInfo(JSONObject obj);

    void getYkcStationInfo();
}
