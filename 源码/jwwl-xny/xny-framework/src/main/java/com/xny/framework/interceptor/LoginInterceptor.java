package com.xny.framework.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.xny.common.config.XNYConfig;
import com.xny.common.constant.CommonConstants;
import com.xny.common.enums.CTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 登录检查
 * 1.配置到拦截器要拦截哪些请求
 * 2.把这些配置放在容器中
 *
 * 实现HandlerInterceptor接口
 */

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private XNYConfig xnyConfig;

    //token校验放行的接口
    private static final List<String> ignoreUrl = Arrays.asList(
            "/webjars/**",
            "/v2/api-docs/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**",
            "/favicon.ico",
            "/error",
            "/file/view/**",//图片访问
            "/file/download/**",//图片下载
            "/login/**", //登录相关
            "/getEquipChargeStatus/**",
            "/nanRui/**"

    );

    /**
     * 目标方法执行之前
     * 登录检查写在这里，如果没有登录，就不执行目标方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String path = request.getServletPath();
        log.info("===请求路径:"+path);
        if (request.getMethod().equals("OPTIONS") || !xnyConfig.getTokenValidate() || ifIgnore(path)) {
            return true;
        }

        String cType = request.getHeader(CommonConstants.CTYPE);
        log.info("===客户端类型:"+cType);
        if(StrUtil.isBlank(cType)){
            throw new RuntimeException("客户端类型不能为空");
        }

        CTypeEnum cTypeEnum = CTypeEnum.getCTypeEnum(cType);
        if(BeanUtil.isEmpty(cTypeEnum)){
            throw new RuntimeException("客户端类型错误");
        }

        //token校验
        StpUtil.checkLogin();

        return true;
    }

    /**
     * 检查是否忽略url
     *
     * @param path 路径
     * @return boolean
     */
    private boolean ifIgnore(String path) {
        return ignoreUrl.stream()
            .map(url -> url.replace("/**", ""))
            .anyMatch(path::startsWith);
    }
 

}