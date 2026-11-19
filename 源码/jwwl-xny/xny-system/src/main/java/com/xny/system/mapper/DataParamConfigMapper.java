package com.xny.system.mapper;

import com.xny.system.entity.DataParamConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.dto.DataChartDTO;
import com.xny.system.model.dto.DataCountDTO;
import com.xny.system.model.dto.GetConfigChartDataDTO;
import com.xny.system.model.vo.ChartDataVO;
import com.xny.system.model.vo.DataChartVO;
import com.xny.system.model.vo.DataCountVO;

import java.util.List;

/**
 * <p>
 * 设备参数配置 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2024-06-14
 */
public interface DataParamConfigMapper extends BaseMapper<DataParamConfig> {
    List<DataCountVO> listDataParamConfig(DataCountDTO dto);

    List<DataChartVO> listDataParamConfig2(DataChartDTO dto);

    List<ChartDataVO> getConfigChartData(GetConfigChartDataDTO dto);
}
