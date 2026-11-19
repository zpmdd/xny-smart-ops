package com.xny.system.controller;

import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.system.model.dto.*;
import com.xny.system.model.vo.ListUserVO;
import com.xny.system.model.vo.PermsTreeVO;
import com.xny.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户管理")
public class SysUserController {
    @Resource
    private ISysUserService sysUserService;

    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public R<String> addUser(@RequestBody @Validated AddUserDTO dto){
        return sysUserService.addUser(dto);
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    public R<String> updateUser(@RequestBody @Validated UpdateUserDTO dto){
        return sysUserService.updateUser(dto);
    }

    @ApiOperation(value = "停用/启用/删除用户")
    @PostMapping("/operate")
    public R<String> operateUser(@RequestBody @Validated OperateUserDTO dto){
        return sysUserService.operateUser(dto);
    }

    @ApiOperation(value = "用户列表")
    @PostMapping("/list")
    public R<PageInfo<ListUserVO>> listUser(@RequestBody @Validated ListUserDTO dto){
        return sysUserService.listUser(dto);
    }

    @ApiOperation(value = "权限树")
    @PostMapping("/getPermsTree")
    public R<List<PermsTreeVO>> getPermsTree(@RequestBody @Validated PermsTreeDTO dto){
        return sysUserService.getPermsTree(dto);
    }

    @ApiOperation(value = "修改登录密码")
    @PostMapping("/editPassword")
    public R<String> editPassword(@RequestBody @Validated EditPasswordDTO dto){
        if(!dto.getNewPassword().equals(dto.getConfirmPassword())){
            return R.failed("新密码与确认密码不一致");
        }
        if(dto.getNewPassword().equals(dto.getOldPassword())){
            return R.failed("新密码与原密码一致");
        }
        return sysUserService.editPassword(dto);
    }
}
