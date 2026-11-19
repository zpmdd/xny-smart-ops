package com.xny.system.mapper;

import com.xny.system.entity.SysUserMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人员菜单关系表 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface SysUserMenuMapper extends BaseMapper<SysUserMenu> {
    List<Long> getProjectIdListHavePerms(@Param(value = "userId") Long userId);
}
