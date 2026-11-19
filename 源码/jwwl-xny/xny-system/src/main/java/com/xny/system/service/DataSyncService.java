package com.xny.system.service;

import com.xny.system.model.dto.CompleDataDTO;
import com.xny.system.model.dto.RealTimeDataDTO;
import com.xny.system.model.dto.YznDataLogDTO;


public interface DataSyncService {
    /**
     * 历史数据同步
     * @param dto
     */
    void dataLog(YznDataLogDTO dto);

    /**
     * 实时数据同步
     * @param dto
     */
    void realTimeData(RealTimeDataDTO dto);


    /**
     * 补全数据
     * @param dto
     */
    void compleData(CompleDataDTO dto);



}
