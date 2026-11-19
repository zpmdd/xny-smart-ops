package com.xny.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.system.entity.SysDict;
import com.xny.system.mapper.SysDictMapper;
import com.xny.system.model.dto.ListByTypeDTO;
import com.xny.system.service.ISysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public List<SysDict> listByType(ListByTypeDTO dto) {
        List<SysDict> list = this.list(new LambdaQueryWrapper<SysDict>()
                .eq(null != dto.getDictType(), SysDict::getDictType, dto.getDictType())
        );
        return list;
    }
}
