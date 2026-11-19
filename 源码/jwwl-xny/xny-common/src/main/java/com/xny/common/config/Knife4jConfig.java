package com.xny.common.config;

import com.xny.common.constant.CommonConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    @Resource
    private XNYConfig iesConfig;

    @Bean(value = "jwwl-xny")
    public Docket ies() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name(CommonConstants.TOKEN_NAME).description("token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(tokenPar.build());

        ParameterBuilder clientTypePar = new ParameterBuilder();
        clientTypePar.name(CommonConstants.CTYPE).description("客户端类型")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        pars.add(clientTypePar.build());

        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                .description("# swagger-bootstrap-ui-demo RESTful APIs")
                .termsOfServiceUrl("http://www.xx.com/")
                .version("1.0")
                .build()
            )
            //是否开启swagger
            .enable(iesConfig.getKnife4j())
            //分组名称
            .groupName("jwwl-xny")
            .globalOperationParameters(pars)
            .select()
            //这里指定Controller扫描包路径
            .apis(RequestHandlerSelectors.basePackage("com.xny.system.controller"))
            .paths(PathSelectors.any())
            .build();
    }
}
