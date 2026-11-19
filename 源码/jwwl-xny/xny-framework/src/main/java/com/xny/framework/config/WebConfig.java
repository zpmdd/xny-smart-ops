package com.xny.framework.config;


import com.xny.common.config.XNYConfig;
import com.xny.framework.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Resource
  private XNYConfig iesConfig;

  /**
   * 跨域处理
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .allowedMethods("*")
            .allowCredentials(true);
  }

  /**
   * 配置拦截器
   * @param registry 相当于拦截器的注册中心
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getLoginInterceptor());
  }

  @Bean
  public LoginInterceptor getLoginInterceptor(){
      return new LoginInterceptor();
  }

  /**
   * 静态资源访问
   * 所有/pictures/开头的请求 都会去后面配置addResourceLocations的路径下查找资源-> /image/
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/image/**").addResourceLocations("file:"+ iesConfig.getFileStorageDir());
  }

}

