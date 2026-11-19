package com.xny.system.yzn.test;


import com.xny.system.yzn.model.dto.EnergyDTO;
import com.xny.system.yzn.utils.RequestUtils;
import com.xny.system.yzn.model.vo.EnergyVO;

/**
 * 能耗数据-用能明细
 */
public class EnergyTest {
    public static void main(String[] args) {
        EnergyDTO energyDTO = new EnergyDTO();
        energyDTO.setAggregationCycle(12);
        energyDTO.setObjectLabel("secondlevel");
        energyDTO.setObjectId(3);
        energyDTO.setStartTime(1702224000000L);
        energyDTO.setEndTime(1702310400000L);

        EnergyVO send = RequestUtils.doSend(energyDTO, EnergyVO.class);
    }

}
