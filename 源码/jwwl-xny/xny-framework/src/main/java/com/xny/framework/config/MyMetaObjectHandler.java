package com.xny.framework.config;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @author mz
 * @date 2023/11/18 20:30
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", Date::new, Date.class); // 起始版本 3.3.3(推荐)

        try {
            Long loginId = StpUtil.getLoginIdAsLong();
            if (ObjectUtil.isNotNull(loginId)){
                this.strictInsertFill(metaObject, "createBy",  Long.class,loginId); // 起始版本 3.3.3(推荐)
            }
        }catch (Exception e) {
            //e.printStackTrace();
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class); // 起始版本 3.3.3(推荐)

        try {
            Long loginId =  StpUtil.getLoginIdAsLong();
            if (ObjectUtil.isNotNull(loginId)){
                this.strictUpdateFill(metaObject, "updateBy", Long.class,loginId); // 起始版本 3.3.3(推荐)
            }
        }catch (Exception e) {
            //e.printStackTrace();
        }
        
    }
}
