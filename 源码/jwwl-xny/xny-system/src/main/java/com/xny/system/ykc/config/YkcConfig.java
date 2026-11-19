package com.xny.system.ykc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ykcconfig")
public class YkcConfig {
    //云快充接口请求地址
    private String ykcUrl;

    //云快充组织机构代码
    private String ykcOperatorID;
    //云快充运营商秘钥
    private String ykcOperatorSecret;
    //云快充签名秘钥
    private String ykcSigSecret;
    //云快充数据加密秘钥
    private String ykcDataSecret;
    //云快充初始化向量
    private String ykcDataSecretIV;

    //中能组织机构代码
    private String znOperatorID;
    //中能运营商秘钥
    private String znOperatorSecret;
    //中能签名秘钥
    private String znSigSecret;
    //中能数据加密秘钥
    private String znDataSecret;
    //中能初始化向量
    private String znDataSecretIV;
}
