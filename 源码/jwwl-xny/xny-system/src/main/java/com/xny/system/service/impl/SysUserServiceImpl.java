package com.xny.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.common.utils.SnowIdUtil;
import com.xny.common.utils.tree.BaseTreeHelper;
import com.xny.system.entity.SysUser;
import com.xny.system.entity.SysUserMenu;
import com.xny.system.mapper.SysUserMapper;
import com.xny.system.model.dto.*;
import com.xny.system.model.vo.ListUserVO;
import com.xny.system.model.vo.PermsTreeVO;
import com.xny.system.model.vo.UserMapVO;
import com.xny.system.service.ISysMenuService;
import com.xny.system.service.ISysUserMenuService;
import com.xny.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jwwl
 * @since 2023-12-19
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Resource
    private ISysUserMenuService sysUserMenuService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public R<String> addUser(AddUserDTO dto) {
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccountNumber, dto.getAccountNumber())
        );
        if(count>0){
            return R.failed("账号已存在");
        }

        //添加用户
        SysUser sysUser = BeanUtil.copyProperties(dto, SysUser.class);
        long userId = SnowIdUtil.nextId();
        sysUser.setId(userId);
        sysUser.setUpdateBy(StpUtil.getLoginIdAsLong());
        sysUser.setUpdateTime(new Date());
        this.save(sysUser);

        //添加权限
        if(CollectionUtil.isNotEmpty(dto.getPermsList())){
            List<SysUserMenu> userMenuList = new ArrayList<>();
            for (Long menuId : dto.getPermsList()) {
                SysUserMenu sysUserMenu = new SysUserMenu();
                sysUserMenu.setId(SnowIdUtil.nextId());
                sysUserMenu.setUserId(userId);
                sysUserMenu.setMenuId(menuId);
                userMenuList.add(sysUserMenu);
            }
            sysUserMenuService.saveBatch(userMenuList);
        }

        return R.ok("操作成功");
    }

    @Override
    public R<String> updateUser(UpdateUserDTO dto) {
        SysUser sysUser = this.getById(dto.getId());
        if(BeanUtil.isEmpty(sysUser)){
            return R.failed("用户不存在");
        }

        if(StrUtil.isNotBlank(dto.getAccountNumber()) && !dto.getAccountNumber().equals(sysUser.getAccountNumber())){
            long count = this.count(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getAccountNumber, dto.getAccountNumber())
            );
            if(count>0){
                return R.failed("账号已存在");
            }
        }

        //修改用户信息
        SysUser sysUserUpdate = BeanUtil.copyProperties(dto, SysUser.class);
        this.updateById(sysUserUpdate);

        //修改权限
        this.updatePerms(dto);

        return R.ok("操作成功");

    }

    /**
     * 修改用户信息
     * @param dto
     */
    private void updatePerms(UpdateUserDTO dto) {
        List<SysUserMenu> list = sysUserMenuService.list(new LambdaQueryWrapper<SysUserMenu>()
                .eq(SysUserMenu::getUserId, dto.getId())
        );
        List<Long> oldPermsList = list.stream().map(SysUserMenu::getMenuId).collect(Collectors.toList());

        List<SysUserMenu> listToBeDelete = list.stream().filter(s -> !dto.getPermsList().contains(s.getMenuId())).collect(Collectors.toList());
        List<Long> listToBeAdd = dto.getPermsList().stream().filter(s -> !oldPermsList.contains(s)).collect(Collectors.toList());

        if(CollectionUtil.isNotEmpty(listToBeDelete)){
            sysUserMenuService.removeBatchByIds(listToBeDelete);
        }

        if(CollectionUtil.isNotEmpty(listToBeAdd)){
            List<SysUserMenu> userMenuList = new ArrayList<>();
            for (Long menuId : listToBeAdd) {
                SysUserMenu sysUserMenu = new SysUserMenu();
                sysUserMenu.setId(SnowIdUtil.nextId());
                sysUserMenu.setUserId(dto.getId());
                sysUserMenu.setMenuId(menuId);
                userMenuList.add(sysUserMenu);
            }
            sysUserMenuService.saveBatch(userMenuList);
        }
    }

    @Override
    public R<String> operateUser(OperateUserDTO dto) {
        SysUser sysUser = new SysUser();
        sysUser.setId(dto.getUserId());

        if(dto.getOperateType() != 2){
            sysUser.setWhetherBlockUp(dto.getOperateType());
            this.updateById(sysUser);
        }else {
            this.removeById(sysUser);
        }

        String msg;
        if(dto.getOperateType()==0){
            msg = "账号启用成功";
        }else if(dto.getOperateType()==1){
            msg = "账号禁用成功";
        }else {
            msg = "账号删除成功";
        }

        return R.ok(msg);
    }

    @Override
    public R<PageInfo<ListUserVO>> listUser(ListUserDTO dto) {
        PageHelper.startPage(dto.getPageNum(),dto.getPageSize());
        List<ListUserVO> list = sysUserMapper.listUser(dto);
        return R.ok(new PageInfo<>(list));
    }

    @Override
    public Map<Long, UserMapVO> getUserMapByIds(UserMapDTO dto) {
        if(CollectionUtil.isEmpty(dto.getUserIdList())){
            return new HashMap<>();
        }

        List<SysUser> list = this.list(new LambdaQueryWrapper<SysUser>()
                .in(SysUser::getId, dto.getUserIdList())
        );

        return CglibUtil.copyList(list, UserMapVO::new)
                .stream().collect(Collectors.toMap(UserMapVO::getId, t -> t));
    }

    @Override
    public R<List<PermsTreeVO>> getPermsTree(PermsTreeDTO dto) {
        List<PermsTreeVO> permsList = sysUserMapper.getPermsList(dto);
        List<PermsTreeVO> permsTreeVOS = BaseTreeHelper.generateTrees(permsList);
        return R.ok(permsTreeVOS);
    }

    @Override
    public R<String> editPassword(EditPasswordDTO dto) {
        long loginId = StpUtil.getLoginIdAsLong();

        SysUser sysUser = this.getById(loginId);
        if(BeanUtil.isEmpty(sysUser)){
            return R.failed("未查询到用户");
        }

        if(!sysUser.getPassword().equals(dto.getOldPassword())){
            return R.failed("原密码错误");
        }

        this.updateById(new SysUser()
                .setId(loginId)
                .setPassword(dto.getNewPassword())
        );

        return R.ok("操作成功");
    }

}
