package com.xny.system.mapper;

import com.xny.system.entity.DataHfqxEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.dto.ListEquipmentDTO;
import com.xny.system.model.vo.CountEquipmentVO;
import com.xny.system.model.vo.ItemVO;
import com.xny.system.model.vo.ListEquipmentVO;

import java.util.List;

/**
 * <p>
 * 红枫清洗设备数据 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2024-05-24
 */
public interface DataHfqxEquipmentMapper extends BaseMapper<DataHfqxEquipment> {
    CountEquipmentVO countEquipment();

    List<ListEquipmentVO> listEquipment(ListEquipmentDTO dto);
}
