package com.xny.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.xny.common.utils.CTypeUtils;
import com.xny.common.utils.R;
import com.xny.system.model.dto.LoginDTO;
import com.xny.system.model.vo.TokenVO;
import com.xny.system.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/login")
@Api(tags = "登录管理")
public class LoginController {
    @Resource
    private ILoginService loginService;

    @ApiOperation(value = "图形验证码")
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws Exception{
        loginService.captcha(response);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/sysUser")
    public R<TokenVO> sysUserLogin(@RequestBody @Validated LoginDTO dto){
        if(CTypeUtils.ifPc() && StrUtil.isBlank(dto.getCaptcha())){
            return R.failed("验证码不能为空");
        }
        if(CTypeUtils.ifManage() && !dto.getAccountNumber().equals("admin")){
            return R.failed("账号错误");
        }
        return loginService.sysUserLogin(dto);
    }


    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public R<String> sysUserLogin(){
        StpUtil.logout();
        return R.ok("操作成功");
    }
}
