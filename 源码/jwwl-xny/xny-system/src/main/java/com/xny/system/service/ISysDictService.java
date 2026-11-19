package com.xny.system.service;

import com.xny.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xny.system.model.dto.ListByTypeDTO;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface ISysDictService extends IService<SysDict> {
    List<SysDict> listByType(ListByTypeDTO dto);
}
