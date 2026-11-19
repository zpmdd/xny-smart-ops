package com.xny.system.mapper;

import com.xny.system.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.dto.ListParamSimulateDTO;
import com.xny.system.model.dto.RealTimeDataDTO;
import com.xny.system.model.dto.UpdateAlarmCountDTO;
import com.xny.system.model.dto.YznDataLogDTO;
import com.xny.system.model.vo.DataSyncVO;
import com.xny.system.model.vo.ListParamSimulateVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备参数模拟量 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2024-01-03
 */
public interface SysParamSimulateMapper extends BaseMapper<SysParamSimulate> {
    List<DataSyncVO> listDataLogInfo(YznDataLogDTO dto);

    List<DataSyncVO> listRealTimeDataInfo(RealTimeDataDTO dto);

    void realTimeBatchInsertOrUpdate(@Param("list") List<DataDeviceRealTime> list);

    void realTimeBatchInsertOrUpdateAccrual(@Param("list") List<DataDeviceRealTime> list);

    void hourBatchInsertOrUpdate(@Param("list") List<DataDeviceHour> list);

    void dayBatchInsertOrUpdateAccrual(@Param("list") List<DataDeviceDay> list);

    void monthBatchInsertOrUpdate(@Param("list") List<DataDeviceMonth> list);

    void historyBatchInsertOrUpdate(@Param("list") List<DataParamHistory> list);

    void updateAlarmCount(UpdateAlarmCountDTO dto);

    List<ListParamSimulateVO> listParamSimulate(ListParamSimulateDTO dto);
}
