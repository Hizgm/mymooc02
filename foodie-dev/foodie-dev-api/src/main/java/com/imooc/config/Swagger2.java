package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by guoming.zhang on 2021/2/5.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    //http://localhost:8088/swagger-ui.html  源路径
    //http://localhost:8088/doc.html  新样式
    // 配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2) //指定api类型为swagger2
                .apiInfo(apiInfo())                    //用于定义api文档汇总信息
                .select().apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))  //指定controller包
                .paths(PathSelectors.any()) //所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")
                .contact(new Contact("zgm",
                        "https://www.zgm.com",
                        "abc@zgm.com"))
                .description("为天天吃货提供的api文档")
                .version("1.0.1")
                .termsOfServiceUrl("https://www.imooc.com")//网站地址
                .build();
    }
}
