package com.xny.system.mapper;

import com.xny.system.entity.SysDevice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.dto.ListDeviceDTO;
import com.xny.system.model.dto.ChartDataDTO;
import com.xny.system.model.vo.ListDeviceVO;
import com.xny.system.model.vo.ChartDataVO;
import com.xny.system.model.vo.ListRealTimeDataVO;
import com.xny.system.model.vo.RuleParamVO;

import java.util.List;

/**
 * <p>
 * 设备信息表 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface SysDeviceMapper extends BaseMapper<SysDevice> {
    List<ListDeviceVO> listDeviceByPojectType(ListDeviceDTO dto);

    List<RuleParamVO> ruleParam(ListDeviceDTO dto);

    List<ChartDataVO> listHourData(ChartDataDTO dto);

    List<ChartDataVO> listDayData(ChartDataDTO dto);

    List<ChartDataVO> listBenYueData(ChartDataDTO dto);

    List<ChartDataVO> listMonthData(ChartDataDTO dto);

    List<ListRealTimeDataVO> listRealTimeData(ChartDataDTO dto);
}
