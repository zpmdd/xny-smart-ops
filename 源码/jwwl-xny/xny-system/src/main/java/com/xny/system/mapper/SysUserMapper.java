package com.xny.system.mapper;

import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xny.system.model.dto.ListUserDTO;
import com.xny.system.model.dto.PermsTreeDTO;
import com.xny.system.model.vo.ListUserVO;
import com.xny.system.model.vo.PermsTreeVO;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
   List<ListUserVO> listUser(ListUserDTO dto);

   List<PermsTreeVO> getPermsList(PermsTreeDTO dto);
}
