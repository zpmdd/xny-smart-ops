package com.xny.system;

import io.reactivex.annotations.SchedulerSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.xny.system.**.mapper"})
@ComponentScan({"com.xny.*"})
@EnableScheduling
public class SystemApplication {
    public static ConfigurableApplicationContext ac;
    public static void main(String[] args) {
        ac = SpringApplication.run(SystemApplication.class,args);
    }
}
