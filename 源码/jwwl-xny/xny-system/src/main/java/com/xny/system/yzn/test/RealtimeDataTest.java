package com.xny.system.yzn.test;



import com.xny.system.yzn.model.dto.RealtimeDataDTO;
import com.xny.system.yzn.utils.RequestUtils;
import com.xny.system.yzn.model.vo.RealtimeDataVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时数据
 */
public class RealtimeDataTest {
    public static void main(String[] args) {
        List<RealtimeDataDTO> list= new ArrayList<>();

        RealtimeDataDTO dto = new RealtimeDataDTO();
        dto.setChannelId(10);
        dto.setDeviceId(30);
        dto.setMeasureId(2549);
        dto.setStationId(4);
        list.add(dto);

        RealtimeDataVO send = RequestUtils.doSend(list, RealtimeDataVO.class);

    }
}
