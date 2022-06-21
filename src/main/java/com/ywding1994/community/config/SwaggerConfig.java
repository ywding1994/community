package com.ywding1994.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger的配置类
 * <p>
 * 访问地址（原生页面）：http://localhost:8080/community/swagger-ui/index.html
 * </p>
 * <p>
 * 访问地址（UI美化页面）：http://localhost:8080/community/doc.html
 * </p>
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("community项目接口文档")
                .description("community是一个基于SpringBoot的仿牛客网论坛项目，此处列出了项目相关的接口文档。")
                .version("1.0.0")
                .contact(new Contact("ywding1994", "https://github.com/ywding1994", "ywding1994@126.com"))
                .build();
    }

}
