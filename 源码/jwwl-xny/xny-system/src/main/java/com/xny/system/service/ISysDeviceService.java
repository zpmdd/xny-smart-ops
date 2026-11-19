package com.xny.system.service;

import com.xny.common.utils.R;
import com.xny.system.entity.SysDevice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xny.system.model.dto.*;
import com.xny.system.model.vo.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface ISysDeviceService extends IService<SysDevice> {
    PvGlobalOverviewVO pvGlobalOverview(PvGlobalOverviewDTO dto);

    SystemArchitectureVO systemArchitecture(SystemArchitectureDTO dto);

    InverterMonitoringVO inverterMonitoring(InverterMonitoringDTO dto);

    DataStatisticsVO dataStatistics(DataStatisticsDTO dto);

    DeviceMonitorVO deviceMonitor(DeviceMonitorDTO dto);

    BatteryMonitorVO batteryMonitor(BatteryMonitorDTO dto);

    NewDeviceMonitorVO newDeviceMonitor(DeviceMonitorDTO dto);

    ChargeStationMonitorVO chargeStationMonitor(ChargeStationMonitorDTO dto);

    PilesMonitorVO pilesMonitor(PilesMonitorDTO dto);

    WindGlobalViewVO windGlobalView(WindGlobalViewDTO dto);

    BlowerMonitorVO blowerMonitor(BlowerMonitorDTO dto);

    WaterGlobalViewVO waterGlobalView(WaterGlobalViewDTO dto);

    BurstingGlobalViewVO burstingGlobalView(BurstingGlobalViewDTO dto);

    BpvInverterMonitoringVO bpvInverterMonitoring(BpvInverterMonitoringDTO dto);

    BurstingDataStatisticsVO burstingDataStatistics(BurstingDataStatisticsDTO dto);

    List<String> listSubmatrix();

    ItemVO ruleParam(List<Long> paramIdList,List<String> paramNameList,List<Long> projectTypeIdList);

    ItemVO ruleParam(List<Long> paramIdList, List<String> paramNameList, List<Long> projectTypeIdList,String projectIds,List<Long> deviceIdList);

    MoBileHomePageVO moBileHomePage(MapDataCountDTO dto);
}
