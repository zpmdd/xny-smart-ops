package com.xny.system.hfqx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hfqxconfig")
public class HfqxConfig {
    //请求地址
    private String  hfqxUrl;
    //AppKey
    private String AppKey;
    //登录名
    private String LoginName;
    //登录密码
    private String LoginPassword;
}
