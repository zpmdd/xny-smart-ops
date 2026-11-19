package com.xny.system.mapper;

import com.xny.system.entity.DataYkcEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.vo.YkcEquipmentVO;

import java.util.List;

/**
 * <p>
 * 云快充设备数据 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2024-05-15
 */
public interface DataYkcEquipmentMapper extends BaseMapper<DataYkcEquipment> {
    List<YkcEquipmentVO> ykcEquipmentList();
}
