package com.xny.common.utils.tide.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xny.common.utils.HttpClientUtils;
import com.xny.common.utils.tide.entity.TideData;
import com.xny.common.utils.tide.entity.TimeAndLevel;
import com.xny.common.utils.tide.service.TideService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 国家海洋信息中心 实现类
 *
 * @author zhuqianwei
 */
@Service
public class NationalMarineInformationServiceImpl implements TideService {

    @Value("${third-party.tide.nmi-url}")
    private String url;

    @Value("${third-party.tide.tideLocation}")
    private String tideLocation;

    @Value("${third-party.tide.velocityOfFlowLocation}")
    private String velocityOfFlowLocation;

    @Override
    public TideData getTideData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        TideData tideData = new TideData();
        try {
            Map<String, String> data = new HashMap<>();
            data.put("ApiRequest", "{\"Server\":\"User\",\"Command\":\"GetData\",\"Data\":{\"code\":\""+ tideLocation +"\",\"date\":\""+ today +"\"}}");
            // 获取潮汐信息
            String result = HttpClientUtils.postForm(url, null, null, data, false);
            Map<String,List<TimeAndLevel>> mapResult = getList(result,"a","cs","cg");
            tideData.setDayTimeAndLevel(mapResult.get("day"));
            tideData.setTimeAndLevel(mapResult.get("warn"));

            // 获取潮流信息
            data = new HashMap<>();
            data.put("ApiRequest", "{\"Server\":\"User\",\"Command\":\"GetData\",\"Data\":{\"code\":\""+ velocityOfFlowLocation +"\",\"date\":\""+ today +"\"}}");
            // 获取潮汐信息
            result = HttpClientUtils.postForm(url, null, null, data, false);
            mapResult = getList(result,"s","sf","ls");
            tideData.setDayVelocityOfFlow(mapResult.get("day"));
            tideData.setVelocityOfFlow(mapResult.get("warn"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tideData;
    }

    private Map<String,List<TimeAndLevel>> getList(String result,String dayKey,String warnKeyName,String warnValueName){
        Map<String,List<TimeAndLevel>> mapReturn = new HashMap<>();
        List<TimeAndLevel> day = new ArrayList<>();
        List<TimeAndLevel> warn = new ArrayList<>();
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if ("true".equals(jsonObject.getStr("State"))) {
            jsonObject = jsonObject.getJSONObject("Data");
            jsonObject = jsonObject.getJSONObject("SubData");
            String keyName = "", valueName = "";
            for (int i = 0; i <= 23; i++) {
                if (i < 10) {
                    keyName = "0" + i + ":00";
                } else {
                    keyName = i + ":00";
                }
                valueName = dayKey + i;
                day.add(new TimeAndLevel(keyName, jsonObject.getDouble(valueName)));
            }
            for (int i = 0; i <= 500; i++) {
                keyName = warnKeyName + i;
                valueName = warnValueName + i;
                if(jsonObject.getStr(keyName) == null){
                    break;
                }
                warn.add(new TimeAndLevel(jsonObject.getStr(keyName), jsonObject.getDouble(valueName)));
            }
        }
        mapReturn.put("day",day);
        mapReturn.put("warn",warn);
        return mapReturn;
    }
}
