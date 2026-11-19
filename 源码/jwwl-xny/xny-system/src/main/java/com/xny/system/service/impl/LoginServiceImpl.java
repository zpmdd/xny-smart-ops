package com.xny.system.service.impl;
import cn.dev33.satoken.stp.SaTokenInfo;
import com.google.common.collect.Lists;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xny.common.config.XNYConfig;
import com.xny.common.utils.CTypeUtils;
import com.xny.common.utils.R;
import com.xny.common.utils.RedisUtils;
import com.xny.system.entity.SysUser;
import com.xny.system.mapper.SysUserMapper;
import com.xny.system.model.dto.LoginDTO;
import com.xny.system.model.dto.PermsTreeDTO;
import com.xny.system.model.vo.PermsTreeVO;
import com.xny.system.model.vo.TokenVO;
import com.xny.system.service.ILoginService;
import com.xny.system.service.ISysUserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements ILoginService {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private XNYConfig xnyConfig;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void captcha(HttpServletResponse response) throws Exception {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        lineCaptcha.setGenerator(randomGenerator);
        lineCaptcha.createCode();

        redisUtils.set(lineCaptcha.getCode(),lineCaptcha.getCode(),60L);
        ServletOutputStream outputStream = response.getOutputStream();
        lineCaptcha.write(outputStream);
    }


    @Override
    public R<TokenVO> sysUserLogin(LoginDTO dto) {
        if(CTypeUtils.ifPc() && xnyConfig.getCaptchaValidate() && BeanUtil.isEmpty(redisUtils.get(dto.getCaptcha()))){
            return R.failed("无效验证码");
        }

        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getAccountNumber, dto.getAccountNumber())
        );

        if(BeanUtil.isEmpty(sysUser)){
            return R.failed("账号不存在");
        }

        if(!sysUser.getPassword().equals(dto.getPassword())){
            return R.failed("密码错误");
        }

        if(sysUser.getWhetherBlockUp()==1){
            return R.failed("账号已被禁用");
        }

        //登录
        StpUtil.login(sysUser.getId());
        //获取权限
        List<String> permsList = sysUserMapper.getPermsList(
                new PermsTreeDTO().setUserId(sysUser.getId()).setIfExist(true)
        ).stream().map(PermsTreeVO::getPermsFlag).collect(Collectors.toList());

        TokenVO tokenVO = new TokenVO();
        tokenVO.setTokenValue(StpUtil.getTokenInfo().getTokenValue());
        tokenVO.setLoginId(sysUser.getId());
        tokenVO.setUserName(sysUser.getUserName());
        tokenVO.setPermsList(permsList);
        return R.ok(tokenVO);
    }

}
