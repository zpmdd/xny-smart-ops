package com.xny.system.yzn.test;


import com.xny.system.yzn.model.dto.TotalEnergyDTO;
import com.xny.system.yzn.utils.RequestUtils;
import com.xny.system.yzn.model.vo.TotalEnergyVO;

/**
 * 能耗数据-用电量汇总
 */
public class TotalEnergyTest {
    public static void main(String[] args) {
        TotalEnergyDTO totalEnergyDTO = new TotalEnergyDTO();
        totalEnergyDTO.setAggregationCycle(12);
        totalEnergyDTO.setObjectLabel("secondlevel");
        totalEnergyDTO.setObjectId(3);
        totalEnergyDTO.setLogTime(1702224000000L);

        TotalEnergyVO send = RequestUtils.doSend(totalEnergyDTO, TotalEnergyVO.class);
    }

}
