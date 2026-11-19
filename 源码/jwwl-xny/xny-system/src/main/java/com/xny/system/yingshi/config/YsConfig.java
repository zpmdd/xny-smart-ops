package com.xny.system.yingshi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ysconfig")
public class YsConfig {
    //莹石请求地址
    private String ysUrl;
    //莹石appKey
    private String appKey;
    //莹石密钥
    private String secret;
    //设备序列号
    private String deviceSerial;
}
