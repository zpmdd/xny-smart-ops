package com.xny.common.utils;


import cn.hutool.core.util.StrUtil;
import com.xny.common.constant.CommonConstants;
import com.xny.common.enums.CTypeEnum;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class CTypeUtils {
    /**
     * 获取客户端类型
     * @return
     */
    public static CTypeEnum getCType(){
        HttpServletRequest request = getRequest();
        String header = request.getHeader(CommonConstants.CTYPE);
        if(StrUtil.isBlank(header)){
            return null;
        }
        return CTypeEnum.valueOf(header);
    }

    /**
     * 判断是否pad端
     * @return
     */
    public static boolean ifPad(){
        CTypeEnum cType = getCType();
        return null != cType && cType.equals(CTypeEnum.pad);
    }

    /**
     * 判断是否手机端
     * @return
     */
    public static boolean ifMobile(){
        CTypeEnum cType = getCType();
        return null != cType && cType.equals(CTypeEnum.mobile);
    }

    /**
     * 判断是否pc端
     * @return
     */
    public static boolean ifPc(){
        CTypeEnum cType = getCType();
        return null != cType && cType.equals(CTypeEnum.pc);
    }

    /**
     * 判断是否pc端
     * @return
     */
    public static boolean ifManage(){
        CTypeEnum cType = getCType();
        return null != cType && cType.equals(CTypeEnum.manage);
    }

    /**
     * 获取request对象
     * @return
     */
    public static HttpServletRequest getRequest(){
        return  ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

}
