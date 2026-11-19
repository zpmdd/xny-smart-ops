package com.xny.system.yzn.test;

import cn.hutool.core.collection.CollUtil;
import com.xny.system.yzn.model.dto.DatalogDTO;
import com.xny.system.yzn.model.dto.MeterConfigs;
import com.xny.system.yzn.model.vo.DatalogVO;
import com.xny.system.yzn.utils.RequestUtils;


/**
 * 定时记录
 */
public class DataLogTest {
    public static void main(String[] args) {
        DatalogDTO datalogDTO = new DatalogDTO();
        datalogDTO.setStartTime("2023-12-11 00:00:00");
        datalogDTO.setEndTime("2023-12-11 23:59:59");
        datalogDTO.setInterval(5);

        MeterConfigs meterConfigs = new MeterConfigs();
        meterConfigs.setDataId(6000244);//参数ID
        meterConfigs.setDataTypeId(1);
        meterConfigs.setDeviceId(45);//设备ID
        meterConfigs.setLogicalId(1);
        datalogDTO.setMeterConfigs(CollUtil.newArrayList(meterConfigs));

        DatalogVO send = RequestUtils.doSend(datalogDTO, DatalogVO.class);
    }
}
