package com.xny.system.service;

import com.xny.system.entity.DataHfqxEquipment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xny.system.model.dto.DetailEquipmentDTO;
import com.xny.system.model.dto.HfqxGlobalViewDTO;
import com.xny.system.model.vo.DetailEquipmentVO;
import com.xny.system.model.vo.HfqxGlobalViewVO;

/**
 * <p>
 * 红枫清洗设备数据 服务类
 * </p>
 *
 * @author jwwl
 * @since 2024-05-24
 */
public interface IDataHfqxEquipmentService extends IService<DataHfqxEquipment> {
    HfqxGlobalViewVO hfqxGlobalView(HfqxGlobalViewDTO dto);

    DetailEquipmentVO detailEquipment(DetailEquipmentDTO dto);
}
