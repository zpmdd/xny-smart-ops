package com.xny.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class XNYConfig {
    //是否开启校验图形验证码
    @Value("${xny.captchaValidate:true}")
    private Boolean captchaValidate;

    //是否使用模拟数据
    @Value("${xny.useSimulateData:false}")
    private Boolean useSimulateData;

    //设备历史数据库名
    @Value("${xny.historyDataBase}")
    private String historyDataBase;

    //是否开启knife4j,默认false
    @Value("${xny.knife4j:false}")
    private Boolean knife4j;

    //是否开启token校验
    @Value("${xny.tokenValidate:true}")
    private Boolean tokenValidate;

    //文件存储地址
    @Value("${xny.fileStorageDir}")
    private String fileStorageDir;

    //文件临时地址
    @Value("${xny.fileTempDir}")
    private String fileTempDir;

}
