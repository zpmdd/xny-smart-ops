package com.xny.system.qd.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qdconfig")
public class QdConfig {
    private String qdUrl;
    private String platform;
    private String platformSecret;
}
