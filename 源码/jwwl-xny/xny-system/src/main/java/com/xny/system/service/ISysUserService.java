package com.xny.system.service;

import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xny.system.model.dto.*;
import com.xny.system.model.vo.ListUserVO;
import com.xny.system.model.vo.PermsTreeVO;
import com.xny.system.model.vo.UserMapVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
public interface ISysUserService extends IService<SysUser> {
    R<String> addUser(AddUserDTO dto);

    R<String> updateUser(UpdateUserDTO dto);

    R<String> operateUser(OperateUserDTO dto);
    
    R<PageInfo<ListUserVO>> listUser(ListUserDTO dto);

    Map<Long, UserMapVO> getUserMapByIds(UserMapDTO dto);

    R<List<PermsTreeVO>> getPermsTree(PermsTreeDTO dto);

    R<String> editPassword( EditPasswordDTO dto);
}
